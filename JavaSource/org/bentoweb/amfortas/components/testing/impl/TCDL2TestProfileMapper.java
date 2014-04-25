package org.bentoweb.amfortas.components.testing.impl;
/**
 * Mapping algorithm implementation 
 * 
 * A test case matches a user profile if the user profile contains 
 * one of the user agents (if any are specified in TCDL) AND one of 
 * the assistive technologies (if any are specified in TCDL) AND one 
 * of the devices (if any are specified in TCDL).
 * 
 * <ul>
 * 	<li>
 * 		If the TCDL specifies only a type but no products, any product of 
 *		that type in the user's profile is allowed to match.
 *	</li>
 * 	<li>
 * 		If the TCDL specifies a product but not a product version, any version 
 * 		of that product in the user profile is allowed to match.
 * 	</li>
 * 	<li>
 * 		The user may have a higher version but not a lower version of the 
 * 		product specified in TCDL.
 * 	</li>
 * 	<li>
 * 		For each user agent, assistive technology or device listed in the 
 * 		scenario, the user must have an experience level equal to or greater 
 * 		than the level specified in the TCDL.
 * 	</li>
 * </ul>
 * 
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.components.ContextHelper;
import org.apache.cocoon.environment.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bentoweb.amfortas.components.om.AbstractTestProfile;
import org.bentoweb.amfortas.components.om.AbstractTestSuite;
import org.bentoweb.amfortas.components.om.TestCase;
import org.bentoweb.amfortas.components.om.TestCaseScenario;
import org.bentoweb.amfortas.components.om.impl.TCDLTestCaseScenario;
import org.bentoweb.amfortas.components.om.impl.TCDLTestSuite;
import org.bentoweb.amfortas.components.persistence.DatasourceAdapter;
import org.bentoweb.amfortas.components.testing.AbstractTestCaseMapper;
import org.bentoweb.amfortas.components.testing.TestSuitesPoolable;
import org.bentoweb.amfortas.hibernate.om.TestProfile;
import org.bentoweb.amfortas.hibernate.om.TestProfileHasUserProfile;
import org.bentoweb.amfortas.hibernate.om.TestSuite;
import org.bentoweb.amfortas.hibernate.om.UserProfileHasDisability;
import org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology;
import org.bentoweb.amfortas.hibernate.om.UserUsesDevice;
import org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent;
import org.bentoweb.amfortas.pojo.Product;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TCDL2TestProfileMapper implements AbstractTestCaseMapper, Contextualizable, Serviceable, Initializable
{
	private static Log log = LogFactory.getLog(TCDL2TestProfileMapper.class);
	public static final float MAX_VERSION=1000f;
	public static final float MIN_VERSION=0f;
    private ServiceManager manager = null;  
    private AbstractTestSuite  testSuite = null; 
    private TestProfile testProfile = null;
    private String profileId = null;
    private String testSuiteId = null;
    private int testProfileId = 0;
    private DatasourceAdapter datasourceAdapter = null;
    private boolean initialized=false;
    private Context context = null;
    private TestCaseScenario testCaseScenario = null;
    
    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }
    public void contextualize(Context context) throws ContextException 
    {
        this.context = context;
        Request r = ContextHelper.getRequest(context);
        this.profileId = String.valueOf(r.getAttribute(Constants4Mapping.TESTPROFILEID));
        if(log.isDebugEnabled())
            log.debug("testProfileId got param from request context" + this.profileId);
        this.testSuiteId  = String.valueOf(r.getAttribute(Constants4Mapping.TS_PROP_ID));
        if(log.isDebugEnabled())
            log.debug("testSuiteId got param from request context" + this.testSuiteId);       
    }
    /*
     * Needs following attributes to be in request:
     * @param "profileId" for AbstractTestProfile
     *  (non-Javadoc)
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception 
    {
        TestSuitesPoolable tsPool = null;
        try 
        {
            tsPool = (TestSuitesPoolable)this.manager.lookup(TestSuitesPoolable.ROLE);
            datasourceAdapter = (DatasourceAdapter)this.manager.lookup(DatasourceAdapter.ROLE);
            this.testSuite = tsPool.getTestSuite(this.testSuiteId);
            this.initialized=true;
        } 
        catch (ServiceException ex) 
        {
            log.debug(ex.getMessage());
            ex.printStackTrace();
        } 
        finally 
        {
            this.manager.release(tsPool);  
            this.manager.release(datasourceAdapter);
        }       
    }
    
    public void dispose() 
    {
    	this.testSuite = null;
    }

	public List doMap() 
	{      
        if(!initialized) //in case not poolled for some reason
        {
            try 
            {
                this.contextualize(this.context);
                this.initialize();
            } 
            catch (Exception e1) 
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }            
        } 
        AbstractTestProfile defTestProfile = null;
        try 
        {
            defTestProfile = (AbstractTestProfile)this.manager.lookup(AbstractTestProfile.ROLE);
        } 
        catch (ServiceException e1) 
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        this.testProfile = (TestProfile) defTestProfile.getTestProfile();            
        this.manager.release(defTestProfile);        
        TestSuite ts = (TestSuite)testSuite.getProps().get(Constants4Mapping.TS_PROP_TEST_SUITE);                    
        // Actual Algorithm starts here
        log.trace("Starting the Map algorithm");
		List winnersTestCaseScenarioList = new ArrayList();
        testProfileId = this.testProfile.getTestProfileId().intValue();
        log.debug("For profile id: "+ testProfileId); 
        if(datasourceAdapter.hasTestCaseSet(testProfileId,ts.getTestSuiteId().intValue())) 
        {
            log.info("Test profile has test cases");
            return datasourceAdapter.getAllocatedTestCasesScenariosList(ts.getTestSuiteId(),this.testProfile.getTestProfileId(),false);
        }   
        List notAllocatedTestCasesScenariosList = datasourceAdapter.getNotAllocatedTestCasesScenariosList((TCDLTestSuite)testSuite,this.testProfileId);
        ConcurrentHashMap testCases = this.testSuite.getTestCases();       
        log.debug("Loaded not done testcaseScenarios:" + notAllocatedTestCasesScenariosList.size());  
        
        // for each available (not done) test case
        log.debug("For every not done test case:");
        for(ListIterator itTC = notAllocatedTestCasesScenariosList.listIterator(); itTC.hasNext(); )
        {            
            this.testCaseScenario = (TestCaseScenario) itTC.next();
            TestCase testCase = (TestCase)testCases.get(testCaseScenario.getTestCaseId());
            log.debug("+++++++++++++ Runing Algorithm for testcase/scenario : " + testCaseScenario.getUID()+"++++++++++++++++");
            //System.out.println("Scenario: " + this.testCaseScenario.getUID());

            //check for disability         
            if(disabillityPass())
            {
            	//System.out.println("Dis ok");
                if(assistiveTechnologyPass())
                {       
                	//System.out.println("ATTT ok");
                    if(userAgentPass())
                    {
                    	//System.out.println("UAAAAAAAAAAAA ok");
                    	if(devicePass())
                            winnersTestCaseScenarioList.add(testCaseScenario);                    	
                    }
                        
                }
            }
                  
		}// next test case
        log.debug(" _______________candidate test cases no after running all rules: " + winnersTestCaseScenarioList.size());
        DefaultTestCaseScenarioComparator testCaseScenarioComparator = new DefaultTestCaseScenarioComparator();
        //sort and get N first 
        Collections.sort(winnersTestCaseScenarioList, testCaseScenarioComparator);
        List arrangedList = this.arrange(winnersTestCaseScenarioList);
        int noOfWinners = ts.getTestCasesPerUser().intValue();
        if(arrangedList.size()>noOfWinners) 
        	arrangedList = arrangedList.subList(0,noOfWinners);   
        if(arrangedList.size()>0)
        	arrangedList = datasourceAdapter.newTestCasePool(testProfile,arrangedList,testSuite); 
        return arrangedList;
    }  
    
    
    public boolean devicePass()
    {
        int grade = 0;
        boolean devicePass = false;
        List dif_types_in_tcdl = this.testCaseScenario.getDevicesTypes();
        if(dif_types_in_tcdl==null || dif_types_in_tcdl.size()<1) // case tcdl not care
        	return true;  
        //get Devices types from db
//      get profile
        Iterator iter1 = this.datasourceAdapter.select_UserUsesDevice_where_TpId(this.testProfileId).iterator();
        HashMap devMap = new HashMap();
        while(iter1.hasNext())
        {
            UserUsesDevice uu = (UserUsesDevice) iter1.next();
            String dev_type = uu.getDevice().getNameDefault();
            devMap.put(dev_type,uu);        // does not make sense for a test profile to have same product more than once 
        } 
        
    	for(ListIterator lit=dif_types_in_tcdl.listIterator();lit.hasNext();)
    	{
    		boolean thisTypeisOK = false;
            String type = String.valueOf(lit.next());            
            Iterator productIter = this.testCaseScenario.getDevices().iterator();
			boolean thisInstanceOfTypeisOK = false;
			while(productIter.hasNext())
			{        
				Product p = (Product) productIter.next();
//                  even if one of the following combination is true ... break and return true                    
			    String minimumLevel_s = p.getExperience();
			    int minimumLevel = 0;
			    if(minimumLevel_s!=null && minimumLevel_s.length()>0)
			        minimumLevel = Integer.parseInt(minimumLevel_s); 
			    UserUsesDevice uu = null;
			    uu = (UserUsesDevice) devMap.get(type);
			    if(uu==null)// no such product
			        return false;
			    int productMinLevel = uu.getAtExperience().getAtExperienceId().intValue() - 1; // take out the 1st null value of the db
			    if(productMinLevel>=minimumLevel) //atExperienceId mean null :  for browser we dont have experience level in db
			    {
			        thisInstanceOfTypeisOK = true;
			        break;                      
			    }
			}
			if(thisInstanceOfTypeisOK)//if two or more elements of the same class (UA) but with same type occur, they are OR related
			{
			    thisTypeisOK=true;
			    grade++;
			}
            if(thisTypeisOK) //if two or more elements of the same class (AT) but with different type occur, they are AND related
            {
                devicePass = true;
                grade++;
            }
            else
                return false; // even if one type is not ok return false (AND for types)            
        }            
        this.testCaseScenario.setGrade(this.testCaseScenario.getGrade()+grade);
        return devicePass;                       
    }
 
    public boolean userAgentPass()
    {
        int grade = 0;
        boolean userAgentPass = false;
        // get all distinct types of UA in tcdl file       
        List dif_types_in_tcdl = this.testCaseScenario.getUserAgentsTypes();
        if(dif_types_in_tcdl==null || dif_types_in_tcdl.size()<1) // case tcdl not care
        	return true;       
        // get profile
        Iterator iter1 = this.datasourceAdapter.select_UserUsesUserAgent_where_TpId(this.testProfileId).iterator();
        HashMap uuMap = new HashMap();
        HashMap uuTypesMap = new HashMap();
        while(iter1.hasNext()) 
        {
            UserUsesUserAgent uu = (UserUsesUserAgent) iter1.next();
            String product = uu.getUserAgent().getNameDefault();
            uuMap.put(product,uu);        // does not make sense for a test profile to have same product more than once 
            uuTypesMap.put(product,uu.getUserAgent().getUserAgentTypes().getNameDefault());
        }    
       
        // for every type of UA: APPLY AND (need at least one product of each type)
        for(ListIterator lit=dif_types_in_tcdl.listIterator();lit.hasNext();)
    	{
    		boolean thisTypeisOK = false;
            String type = String.valueOf(lit.next());
            if(!uuTypesMap.containsValue(type))
                return false; // no such UA type in profile            
            NodeList sameTypeNodeList = null; // get UAs of same type: even one of these are in testProfile return true
            Iterator productIter = this.testCaseScenario.getUserAgentProductsOfType(type).iterator();
			boolean thisInstanceOfTypeisOK = false;
			while(productIter.hasNext())
			{       
				Product p = (Product) productIter.next();
			    // even if one of the following combination is true ... break and return true                    
			    String product = p.getName();
			    // check tcdl experience
			    String minimumLevel_s = p.getExperience();
			    int minimumLevel = 0;
			    if(minimumLevel_s!=null && minimumLevel_s.length()>0)
			        minimumLevel = Integer.parseInt(minimumLevel_s);
			    UserUsesUserAgent uu = null;
			    if(product!=null && product.length()>0)//care about product
			    {
			        uu = (UserUsesUserAgent) uuMap.get(product);
			        if(uu==null)// no such product
			            return false;
			    }
			    else  // get a product of this type of UA from the profile
			    {
			        Iterator iter_p = uuTypesMap.keySet().iterator();
			        while(iter_p.hasNext())
			        {
			            String _product = String.valueOf(iter_p.next());
			            String _type = String.valueOf(uuTypesMap.get(_product));
			            if(_type.equalsIgnoreCase(type))
			            {
			                uu = (UserUsesUserAgent) uuMap.get(_product);
			                break;
			            }
			        }                        
			    }
			    int productMinLevel = 6;
			    try
			    {
			        productMinLevel = uu.getAtExperience().getAtExperienceId().intValue() - 1; // take out the 1st null value of the db
			    }
			    catch(Exception e)
			    {
			        e.printStackTrace();
			    }
			    if(type.equalsIgnoreCase("browser"))
			        productMinLevel = 6; // set to max as for browser this is not store in db
			    if(productMinLevel>=minimumLevel) //atExperienceId mean null :  for browser we dont have experience level in db
			    {
			        // test version
			        float profileVersion = 0f;
			        float version = 0f;
			        try
			        { 
			            version = TCDL2TestProfileMapper.toVersion(p.getVersion(),MIN_VERSION);
			            profileVersion = TCDL2TestProfileMapper.toVersion(uu.getVersion(),MAX_VERSION);
			        }
			        catch(Exception e)
			        {
			        	thisInstanceOfTypeisOK = true; //FIXME: tcdl ver=decimal ,profile=string
			            log.warn(e.toString());   
			            break;
			        }
			        if(version==MIN_VERSION || profileVersion==MAX_VERSION || profileVersion==version)
			        {
			            thisInstanceOfTypeisOK = true;
			            break;
			        }                        
			    }
			    else
				{
					thisInstanceOfTypeisOK = true;
					break; // one instance is enough (OR)
				}
			}
			if(thisInstanceOfTypeisOK)//if two or more elements of the same class (UA) but with same type occur, they are OR related
			{
				thisTypeisOK=true;
				grade++;
			}
            if(thisTypeisOK) //if two or more elements of the same class (AT) but with different type occur, they are AND related
            {
            	userAgentPass = true;
            	grade++;
            }
            else
            	return false; // even if one type is not ok return false (AND for types)            
        }
        	
        this.testCaseScenario.setGrade(this.testCaseScenario.getGrade()+grade);
        return userAgentPass;                       
    }
    /**
     * if a tcdl has 1 or more ATs and test profile has no AT, amfortas will *not* allocate that test to the user
     * a tcdl without AT will not be allocated to a profile with AT
     * 
     * @param scenarioElement
     * @return
     */
    public boolean assistiveTechnologyPass()
    {
        int grade = 0;
        boolean assistiveTechnologyPass = false;
        List dif_types_in_tcdl = this.testCaseScenario.getAssistiveTechnologyTypes();
        // get all types of AT in db for current testprofile 
        List db_at_types_list = this.datasourceAdapter.select_ATtypes_where_TpIda(this.testProfileId);
        if (db_at_types_list==null || dif_types_in_tcdl.size()!=db_at_types_list.size())  // case not exact match to AT types
            return false;      
        if(dif_types_in_tcdl.size()==0 && db_at_types_list.size()==0) //otherwise wont go to loop
        	return true;
        for(ListIterator lt = db_at_types_list.listIterator(); lt.hasNext();)
        {
            String type_db = String.valueOf(lt.next());
            boolean match = false;            
            for(ListIterator lit = dif_types_in_tcdl.listIterator(); lit.hasNext();)
            {
                String type_tcdl = String.valueOf(lit.next());
                if(type_tcdl.equalsIgnoreCase(type_db))
                {
                    match = true; 
                    break;
                }
            }
            if(!match)
                return false;// if not exact match of AT types
        }
        //if AT type match one to one
        //get profile
        Iterator iter1 = this.datasourceAdapter.select_AT_where_TpId(testProfileId).iterator();
        HashMap atMap = new HashMap();
        HashMap atTypesMap = new HashMap();
        while(iter1.hasNext())
        {
            UserUsesAssistiveTechnology uu = (UserUsesAssistiveTechnology) iter1.next();
            //case other AT type
            //if(uu.getAssistiveTechnologyOther()!=null) //TODO:                 
              //  ;
            if(uu.getAssistiveTechnology().getAssistiveTechnologyId()==1) // ignore  other -- AT_id = null->1
            	continue;
            String product = uu.getAssistiveTechnology().getNameDefault();
            atMap.put(product,uu);        // does not make sense for a test profile to have same product more than once 
            atTypesMap.put(product,uu.getAssistiveTechnology().getAssistiveTechnologyTypes().getNameDefault());
        }        
        // for every type of AT: APPLY AND (need at least one product of each type)
        ListIterator iter_at_types = db_at_types_list.listIterator();       
        while(iter_at_types.hasNext()) // for every type of AT: APPLY AND (need at least one product of each type)
        {
        	boolean thisTypeisOK = false;  
            String type = String.valueOf(iter_at_types.next());                
            Iterator productIter = this.testCaseScenario.getAssistiveTechnologyProductsOfType(type).iterator();
			boolean thisInstanceOfTypeisOK = false;
			while(productIter.hasNext())
			{ 
				Product p = (Product) productIter.next();
			    String minimumLevel_s = p.getName();
			    int minimumLevel = 0;
			    if(minimumLevel_s!=null && minimumLevel_s.length()>0)
			        minimumLevel = Integer.parseInt(minimumLevel_s);
			    UserUsesAssistiveTechnology uu = null; 
			    if(p.getName()!=null && p.getName().length()>0)//care about product
			    {
			        uu = (UserUsesAssistiveTechnology) atMap.get(p.getName());
			        if(uu==null)// no such product
			            return false;
			    }
			    else  // get a product of this type of UA from the profile
			    {
			        Iterator iter_p = atTypesMap.keySet().iterator();
			        while(iter_p.hasNext())
			        {
			            String _product = String.valueOf(iter_p.next());
			            String _type = String.valueOf(atTypesMap.get(_product));
			            if(_type.equalsIgnoreCase(type))
			            {
			                uu = (UserUsesAssistiveTechnology) atMap.get(_product);
			                break;
			            }
			        }                        
			    }
			    int productMinLevel = uu.getAtExperience().getAtExperienceId().intValue() - 1; // take out the 1st null value of the db                 
			    if(productMinLevel>=minimumLevel) //atExperienceId mean null :  for browser we dont have experience level in db
			    {
			        // test version
			        float profileVersion = 0f;
			        float version = 0f;
			        try
			        { 
			            version = TCDL2TestProfileMapper.toVersion(p.getVersion(),MIN_VERSION);
			            profileVersion = TCDL2TestProfileMapper.toVersion(uu.getVersion(),MAX_VERSION);
			        }
			        catch(Exception e)
			        {
			        	thisInstanceOfTypeisOK = true; //FIXME: tcdl ver=decimal ,profile=string
			            log.warn(e.toString());   
			            break;
			        }
			        if(version==MIN_VERSION || profileVersion==MAX_VERSION || profileVersion==version)
			        {
			            thisInstanceOfTypeisOK = true;
			            break;
			        }                        
			    }
			    else
			    {
			        thisInstanceOfTypeisOK = true;
			        break; // one instance is enough (OR)
			    }
			}
			if(thisInstanceOfTypeisOK)//if two or more elements of the same class (UA) but with same type occur, they are OR related
			{
			    thisTypeisOK=true;
			    grade++;
			}
            if(thisTypeisOK) //if two or more elements of the same class (AT) but with different type occur, they are AND related
            {
                assistiveTechnologyPass = true;
                grade++;
            }
            else
                return false; // even if one type is not ok return false (AND for types)            
        }
        this.testCaseScenario.setGrade(this.testCaseScenario.getGrade()+grade);
        return assistiveTechnologyPass;                        
    }
    /**
     * 	-if tcdl cares about disabilities (there are entries) then
	 *		-- if a test profile has no dis AND if there is "no disability" entry in tcdl then (only then)
	 *			---amfortas will allocate that to the user
	 *	- otherwise, if tcdl does not care (no dis elements) .. very few (i think 2) cases
	 *		-- whatever the profile, amfortas will allocate that to the user
     * @param scenarioElement
     * @return
     */
    public boolean disabillityPass()
    {
        boolean disabilityPass = false;
        int grade = 0;
        String[] disabilities = this.testCaseScenario.getDisabilities();
        
        if(disabilities!=null && disabilities.length>0) //if tcdl cares about disability 
        {
        	//QUICK FIX!! pass userprofileid instead of testprofileid
        	TestProfileHasUserProfile te = (TestProfileHasUserProfile) this.testProfile.getTestProfileHasUserProfiles().iterator().next();
            Iterator iter_dis = this.datasourceAdapter.select_UserProfileHasDisability_where_UserProfileId(te.getUserProfile().getUserProfileId()).iterator();
            if(!iter_dis.hasNext()) //case no disability in profile // not at all efficient...
            {
            	for(int i=0;i<disabilities.length;i++)
                {
                    String disability = disabilities[i];
                    if(disability.equalsIgnoreCase("no disability"))
                        disabilityPass = true;
                }
            }
            while(iter_dis.hasNext())
            {
                UserProfileHasDisability up = (UserProfileHasDisability) iter_dis.next();
                String profileDisability = up.getDisability().getNameDefault();
                //String other_dis = up.getUserProfile().getDisabilityOther();//
                for(int i=0;i<disabilities.length;i++)
                {
                    String disability =  disabilities[i];
                    //if(disability.equalsIgnoreCase("other") || profileDisability.equalsIgnoreCase(disability))
                    if(profileDisability.equalsIgnoreCase(disability))
                    {
                        disabilityPass = true;
                        grade++;
                       // break;
                    }
                }
                if(disabilityPass) // disabillities OR related
                    break;
            }                      
        }
        else
            disabilityPass = true;
        this.testCaseScenario.setGrade(this.testCaseScenario.getGrade()+grade);
        return disabilityPass;
    }
    
    public static float toVersion(String ver, float def)
    {
    	float version = def;
    	if(ver!=null && ver.length()>0)
    	{
    		ver = ver.trim();
            ver = ver.replace(",",".");
            if(ver.indexOf(".")>0)
            {
                String tmp=null;
                int dec = ver.indexOf(".");
                tmp = ver.substring(0,dec);
                ver = tmp + "." + ver.charAt(dec+1);
            }
            try
            {
            	version = Float.parseFloat(ver);
            }
            catch(Exception e)
            {
            	
            }
    	}    
        return version;
    }
	private List arrange(List l)
	{
        Collections.sort(l, new IdTestCaseScenarioComparator()); 
        Iterator iter = l.iterator();
        List l1 = new ArrayList();
        String last = "";
        while(iter.hasNext())
        {
        	TCDLTestCaseScenario tc = (TCDLTestCaseScenario) iter.next();        	
        	String id = tc.getTestCaseId();
        	if(!last.equalsIgnoreCase(id))
        		l1.add(tc);
        	last=id;
        }
		return l1;
	}    
}
