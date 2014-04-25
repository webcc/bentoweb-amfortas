package org.bentoweb.amfortas.components.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.mail.MailSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bentoweb.amfortas.components.om.AbstractTestSuite;
import org.bentoweb.amfortas.components.om.TestCaseScenario;
import org.bentoweb.amfortas.components.om.impl.TCDLTestCaseScenario;
import org.bentoweb.amfortas.components.persistence.DatasourceAdapter;
import org.bentoweb.amfortas.components.testing.impl.Constants4Mapping;
import org.bentoweb.amfortas.hibernate.om.TestProfile;
import org.bentoweb.amfortas.hibernate.om.TestProfileHasUserProfile;
import org.bentoweb.amfortas.hibernate.om.TestProfileHasUserProfileId;
import org.bentoweb.amfortas.hibernate.om.TestSuite;
import org.bentoweb.amfortas.hibernate.om.TestcasePool;
import org.bentoweb.amfortas.hibernate.om.TestcaseSet;
import org.bentoweb.amfortas.hibernate.om.User;
import org.bentoweb.amfortas.hibernate.om.UserProfileHasDisability;
import org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology;
import org.bentoweb.amfortas.hibernate.om.UserUsesDevice;
import org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent;
import org.hibernate.Query;
import org.hibernate.Session;

public class HibernateAdapterImpl implements DatasourceAdapter,ThreadSafe,Disposable,Contextualizable,Serviceable,Initializable {

	private static Log log = LogFactory.getLog(HibernateAdapterImpl.class);
	private ServiceManager manager = null;
	private Session session = null;
	public int getCountCreditForProfileNTestSuite(TestProfile tp, TestSuite ts)
	{
		String q = "select count (*) from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testProfile=? and tp.finished='1' and tp.testcaseSet.testSuite=?";
		Long credit = (Long) getSession().createQuery(q).setEntity(0, tp).setEntity(1, ts).iterate().next();
		return credit.intValue();
	}
	public int getCountTODOForProfileNTestSuite(TestProfile tp, TestSuite ts)
	{
		String q = "select count (*) from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testProfile=? and tp.finished='0' and tp.testcaseSet.testSuite=?";
		Long todo = (Long) getSession().createQuery(q).setEntity(0, tp).setEntity(1, ts).iterate().next();
		return todo.intValue();
	}
	public List<TestcasePool> getCreditForProfileNTestSuite(TestProfile tp, TestSuite ts)
	{
		String q = "from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testProfile=? and tp.finished='1' and tp.testcaseSet.testSuite=?";
		List l = getSession().createQuery(q).setEntity(0, tp).setEntity(1, ts).list();
		endTransaction();
		return l;
	}
	public List<TestcasePool> getTODOForProfileNTestSuite(TestProfile tp, TestSuite ts)
	{
		String q = "from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testProfile=? and tp.finished='0' and tp.testcaseSet.testSuite=?";
		List l = getSession().createQuery(q).setEntity(0, tp).setEntity(1, ts).list();
		endTransaction();
		return l;
	}	
	
	//String q = "select count (*) from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testProfile.user.userId='"+userId+"' and tp.finished='1' and tp.testcaseSet.testSuite.testSuiteId='"+"3"+"'"";
	public List<TestProfile> getTestProfiles(String userId)
	{
		String q = "from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where (tp.user.userId=?)";
		List l = getSession().createQuery(q).setInteger(0, Integer.parseInt(userId)).list();	
		endTransaction();
		return l;
	}
	public TestProfile getTestProfile(String tpid)
	{
		String q = "from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where (tp.testProfileId=?)";
		Iterator<TestProfile> tpIter = getSession().createQuery(q).setInteger(0, Integer.parseInt(tpid)).iterate();
		endTransaction();
		if(tpIter.hasNext())
			return (TestProfile) tpIter.next();
		return null;
	}
	public TestProfile getActiveTestProfile(String userId)
	{
		String q = "from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where (tp.user.userId=?) and (tp.isActive='1')";
		Iterator<TestProfile> tpIter = getSession().createQuery(q).setInteger(0, Integer.parseInt(userId)).iterate();
		endTransaction();
		if(tpIter.hasNext())
			return (TestProfile) tpIter.next();
		return null;
	}
	public List<TestSuite> getAssignedTestSuites(TestProfile tp)
	{
		String q = "select testSuite from org.bentoweb.amfortas.hibernate.om.TestProfileHasTestSuite as tp where (tp.testProfile.testProfileId=?)";
		List l = getSession().createQuery(q).setInteger(0, tp.getTestProfileId()).list();
		endTransaction();
		return l;
	}
	public void activateTestProfile(TestProfile tp)
	{
		Iterator<TestProfile> testProfilesIter = getTestProfiles(String.valueOf(tp.getUser().getUserId())).iterator();
		while(testProfilesIter.hasNext())
		{
			TestProfile testProfile = testProfilesIter.next();
			if(testProfile.getTestProfileId().intValue()!= tp.getTestProfileId().intValue())
				if(testProfile.isIsActive())
				{
					testProfile.setIsActive(false);
					getSession().update(testProfile);
				}
		}
		tp.setIsActive(true);
		getSession().update(tp);
		getSession().flush();
		endTransaction();
	}
	public boolean canDeleteTestProfile(TestProfile tp)
	{
		if(tp!=null && !tp.isIsActive())
		{
			List<TestProfile> tps = getTestProfiles(String.valueOf(tp.getUser().getUserId()));
			if(tps!=null && tps.size()>1)
				if(tp.getTestresults().isEmpty())
					return true;
		}
		endTransaction();
		return false;
	}
	public void deleteTestProfile(TestProfile tp)
	{
		if(canDeleteTestProfile(tp))
		{
			String q = "delete org.bentoweb.amfortas.hibernate.om.TestProfile where testProfileId=?";
			getSession().createQuery(q).setInteger(0,tp.getTestProfileId()).executeUpdate();
			q = "delete org.bentoweb.amfortas.hibernate.om.TestProfileHasTestSuite where testProfile.testProfileId=?";
			getSession().createQuery(q).setInteger(0,tp.getTestProfileId()).executeUpdate();
			q = "delete org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology where testProfile.testProfileId=?";
			getSession().createQuery(q).setInteger(0,tp.getTestProfileId()).executeUpdate();
			q = "delete org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent where testProfile.testProfileId=?";
			getSession().createQuery(q).setInteger(0,tp.getTestProfileId()).executeUpdate();
			q = "delete org.bentoweb.amfortas.hibernate.om.TestProfileHasUserProfile where testProfile.testProfileId=?";
			getSession().createQuery(q).setInteger(0,tp.getTestProfileId()).executeUpdate();
			getSession().flush();
			endTransaction();
		}
	}
	public boolean cloneTestProfile(String tpid)
	{
		TestProfile tp = getTestProfile(tpid);
		if(tp!=null)
		{
			try
			{
				//getSession().beginTransaction();
				TestProfile tp_new = new TestProfile();
				tp_new.setDateCreation(new Date());
				tp_new.setUser(tp.getUser());
				tp_new.setUserUsesDevice(tp.getUserUsesDevice());
				tp_new.setUserUsesOs(tp.getUserUsesOs());
				getSession().save(tp_new);
	
				Set<TestProfileHasUserProfile> tpupSet = new HashSet<TestProfileHasUserProfile>();
				TestProfileHasUserProfile tpup = (TestProfileHasUserProfile) tp.getTestProfileHasUserProfiles().iterator().next();
				TestProfileHasUserProfile tpup_n = new TestProfileHasUserProfile();
				tpup_n.setTestProfile(tp_new);
				tpup_n.setUserProfile(tpup.getUserProfile());
				TestProfileHasUserProfileId tid = new TestProfileHasUserProfileId();
				tid.setTestProfileId(tp_new.getTestProfileId());
				tid.setUserProfileId(tpup.getUserProfile().getUserProfileId());
				tpup_n.setId(tid);
				getSession().save(tpup_n);
				tpupSet.add(tpup_n);
				tp_new.setUserUsesAssistiveTechnologies(tpupSet);
				
				Iterator<UserUsesAssistiveTechnology> uuas_iter = tp.getUserUsesAssistiveTechnologies().iterator();
				Set<UserUsesAssistiveTechnology> uuasSet = new HashSet<UserUsesAssistiveTechnology>();
				while(uuas_iter.hasNext())
				{
					UserUsesAssistiveTechnology uuas = (UserUsesAssistiveTechnology) uuas_iter.next();
					UserUsesAssistiveTechnology uuas_n = new UserUsesAssistiveTechnology();
					uuas_n.setAssistiveTechnology(uuas.getAssistiveTechnology());
					uuas_n.setAssistiveTechnologyOther(uuas.getAssistiveTechnologyOther());
					uuas_n.setAtExperience(uuas.getAtExperience());
					uuas_n.setUserUsesAtId(uuas.getUserUsesAtId());
					uuas_n.setVersion(uuas.getVersion());
					uuas_n.setTestProfile(tp_new);
					getSession().save(uuas_n);
					uuasSet.add(uuas_n);
				}
				tp_new.setUserUsesAssistiveTechnologies(uuasSet);
				
				Iterator<UserUsesUserAgent> uuua_iter = tp.getUserUsesUserAgents().iterator();
				Set<UserUsesUserAgent> uuuaSet = new HashSet<UserUsesUserAgent>();
				while(uuua_iter.hasNext())
				{
					UserUsesUserAgent uuua =  uuua_iter.next();
					UserUsesUserAgent uuua_n = new UserUsesUserAgent();
					uuua_n.setBrowserOther(uuua.getBrowserOther());
					uuua_n.setUserAgent(uuua.getUserAgent());
					uuua_n.setUserUsesUaId(uuua.getUserUsesUaId());
					//uuua_n.setUserUsesUaPlugins(uuua.getUserUsesUaPlugins());
					
					//uuua_n.setUserUsesUaSettings(uuua.getUserUsesUaSettings());
					
					uuua_n.setAtExperience(uuua.getAtExperience());
					uuua_n.setVersion(uuua.getVersion());
					uuua_n.setTestProfile(tp_new);
					getSession().save(uuua_n);
					uuuaSet.add(uuua_n);
				}
				tp_new.setUserUsesUserAgents(uuuaSet);
				getSession().update(tp_new);
				//getSession().getTransaction().commit();
				getSession().flush();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		endTransaction();
		return true;
	}
	
	/** 
	 * return a testprofile given the id
	 */
	public List select_TestProfile_where_TpId(int testProfileId) {
		String q = "from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where tp.testProfileId=?";
		List<TestProfile> result = getSession().createQuery(q).setInteger(0,testProfileId).list();
		endTransaction();
		return result;
	}

	/**
	 * return a user if exists
	 */
	public List<User> select_User_where_userpass(String username,String password) {
		String q = "from org.bentoweb.amfortas.hibernate.om.User as user where user.email=? and user.password=?";
		List<User> result = getSession().createQuery(q).setString(0, username).setString(1, password).list();
		endTransaction();
		return result;
	}

	/**
	 * user has role?
	 */
	public List select_UserHasRole_where_Username(String username) { 
		String q = "from org.bentoweb.amfortas.hibernate.om.UserHasRole as ur where ur.user.email=?";
		List l = getSession().createQuery(q).setString(0, username).list();
        endTransaction();
		return l;
	}

	/**
	 * return UserUsesDevice_where_TpId
	 * 
	 * @throws Exception
	 */
	public List select_UserUsesDevice_where_TpId(int testProfileId) {
		String q = "select userUsesDevice from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where tp.testProfileId=?";
		List<UserUsesDevice> results = getSession().createQuery(q).setInteger(0,	testProfileId).list();
        endTransaction();
		return results;
	}
	/**
	 * return UserUsesDevice_where_TpId
	 * 
	 * @throws Exception
	 */
	public List select_UserUsesUserAgent_where_TpId(int testProfileId) {
		String q = "from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as tp where tp.testProfile.testProfileId=?";
		List<UserUsesUserAgent> results = getSession().createQuery(q).setInteger(0,	testProfileId).list();
        endTransaction();
		return results;
	}	
	/**
	 * get all types of AT in db for current testprofile 
	 * @throws Exception
	 */
	public List select_ATtypes_where_TpIda(int testProfileId) {
		String q = "select DISTINCT assistiveTechnology.assistiveTechnologyTypes.nameDefault from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as tp where (tp.testProfile.testProfileId=?) and (tp.assistiveTechnology.assistiveTechnologyTypes.nameKey!='amformas.recruit.ans.assistive_technology_types_id.0')";
		List<String> results = getSession().createQuery(q).setInteger(0,testProfileId).list();
        endTransaction();
		return results;
	}
	
	/**
	 * get AT a user uses given a test profile id
	 * @throws Exception
	 */
	public List select_AT_where_TpId(int testProfileId)
	{
        //get profile
        String q="from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as tp where tp.testProfile.testProfileId=?";
        List<UserUsesAssistiveTechnology> results = getSession().createQuery(q).setInteger(0,testProfileId).list();
        endTransaction();
        return results;
	}
	
	/**
	 * get disabilities for profile
	 * @throws Exception
	 */
	public List select_UserProfileHasDisability_where_UserProfileId(int userProfileId)
	{
		String q="from org.bentoweb.amfortas.hibernate.om.UserProfileHasDisability as tp where tp.userProfile.userProfileId=?)";
		List<UserProfileHasDisability> results =  getSession().createQuery(q).setInteger(0,userProfileId).list();
        endTransaction();
		return results;
	}
	/**
	 * get testsuite when active with id=?
	 * @param testSuiteId
	 * @return
	 */
	public List select_Testsuite_where_ActiveNId(int testSuiteId)
	{
        String q  = "from org.bentoweb.amfortas.hibernate.om.TestSuite as ts where ts.testSuiteId=? and ts.isActive=?";
        List<TestSuite> results = getSession().createQuery(q).setInteger(0,testSuiteId).setBoolean(1,true).list();
        endTransaction();
        return results;
	}

    public List getActiveTestSuites()
    {
        String q = "from org.bentoweb.amfortas.hibernate.om.TestSuite as ts where ts.isActive=?";
        List l = getSession().createQuery(q).setBoolean(0,true).list();
        endTransaction();
        return l;
    } 
    
    public boolean hasTestCaseSet(int testProfileId, int testSuiteId) //TODO: add in q the testprofileid
    {
        boolean hasTC = false;
        //lookup table 
        String q = "from org.bentoweb.amfortas.hibernate.om.TestcasePool as testcasepool " +
        		"where testcasepool.testcaseSet.testProfile.testProfileId=? " +
        		"and testcasepool.finished=? " + 
        		"and testcasepool.testcaseSet.testSuite.testSuiteId=?";
        Iterator iter = getSession().createQuery(q).setInteger(0, testProfileId).setBoolean(1, false).setInteger(2,testSuiteId).iterate();
        if (iter.hasNext()) 
                hasTC = true;
        endTransaction();
        return hasTC; 
    }   
    public boolean hasTestProfile(int userId) 
    {
        Iterator iter_tp = getSession().createQuery("from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where (tp.user.userId=?) and (tp.isActive=?)").setInteger(0, userId).setBoolean(1, true).iterate();
        if (iter_tp.hasNext()) 
        {
            //TestProfile testProfile = (TestProfile)iter_tp.next();
            return true;
        } 
        endTransaction();
        return false; 
    }
    public void setTestCaseFinished(int pool_id) 
    {
    	Iterator iter_tp = getSession().createQuery("from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where (tp.testcasePoolId=?)").setInteger(0, pool_id).iterate();
        if (iter_tp.hasNext()) 
        {
        	TestcasePool tcp = (TestcasePool) iter_tp.next();
        	tcp.setFinished(true);
        	tcp.setDateFinished(new Date());
        	getSession().save(tcp);
        	getSession().flush();
        }
        endTransaction();
    }
    public void setTestCaseStarted(int pool_id) 
    {
    	Iterator iter_tp = getSession().createQuery("from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where (tp.testcasePoolId=?)").setInteger(0, pool_id).iterate();
        if (iter_tp.hasNext()) 
        {
        	TestcasePool tcp = (TestcasePool) iter_tp.next();
        	tcp.setDateStart(new Date());
        	getSession().save(tcp);
        	getSession().flush();
        }
        endTransaction();
    }
    
    public List getAllocatedTestCasesScenariosList(Integer testSuiteId, Integer testProfileId)
    {
        List l = new ArrayList();
        ListIterator iter = this.getAllocatedTestcasePoolsList(testSuiteId,testProfileId).listIterator();
        while(iter.hasNext())
        {
            TestcasePool tcp = (TestcasePool) iter.next();
            TestCaseScenario atcs = new TCDLTestCaseScenario();
            atcs.setScenarioId(tcp.getScenarioId());
            atcs.setTestCaseId(tcp.getTestcaseId());
            l.add(atcs);                           
        }
        endTransaction();
        return l;
    }  
    public List getAllocatedTestCasesScenariosList(Integer testSuiteId, Integer testProfileId, boolean finished)
    {
        List l = new ArrayList();
        ListIterator iter = this.getAllocatedTestcasePoolsList(testSuiteId,testProfileId).listIterator();
        while(iter.hasNext())
        {
            TestcasePool tcp = (TestcasePool) iter.next();
            if(tcp.isFinished()==finished)
            {
                TestCaseScenario atcs = new TCDLTestCaseScenario();
                atcs.setScenarioId(tcp.getScenarioId());
                atcs.setTestCaseId(tcp.getTestcaseId());
                l.add(atcs);
            }                             
        }
        return l;
    }     
    public List getAllocatedTestcasePoolsList(Integer testSuiteId, Integer testProfileId)
    {
        String q = "from org.bentoweb.amfortas.hibernate.om.TestcasePool " +
                "as tcp where (tcp.testcaseSet.testProfile.testProfileId=?) and " +
                "(tcp.testcaseSet.testSuite.testSuiteId=?)";           
        Query query = getSession().createQuery(q).setInteger(0, testProfileId.intValue()).setInteger(1,testSuiteId.intValue());
        endTransaction();
        return query.list();
    }
    
    public List getNotAllocatedTestCasesScenariosList(AbstractTestSuite testSuite, int testProfileId)
    {
        List notAllocatedList = new ArrayList();
        //get from db the allocated scenarios
        TestSuite ts = (TestSuite)testSuite.getProps().get(Constants4Mapping.TS_PROP_TEST_SUITE);        
        int timesPerTestCaseScenario = ts.getTimesPerTestCase().intValue();
     
        //bad routine - better using subqueries somehow
        HashSet allocated = new HashSet();
        String query1 = "select distinct tp.testcaseId, tp.scenarioId from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where (tp.testcaseSet.testSuite.testSuiteId=?) and (tp.finished=1)";
        Iterator tpIter = getSession().createQuery(query1).setInteger(0, ts.getTestSuiteId()).iterate();
        while (tpIter.hasNext())  {
        	Object[] obj = (Object[]) tpIter.next();
        	String query2 = "select count(tp.testcaseId) from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where (tp.testcaseId=?) AND (tp.scenarioId=?) and (tp.testcaseSet.testSuite.testSuiteId=?) and (tp.finished=1)";
        	Iterator tpCount = getSession().createQuery(query2).setString(0, (String)obj[0]).setString(1, (String)obj[1]).setInteger(2, ts.getTestSuiteId()).iterate();
        	if (tpCount.hasNext()) {
        		Long intCount = (Long)tpCount.next();
				//String testcaseId = (String)obj[0];
				//String scenarioId = (String)obj[1];        		

				String testcaseId = null;
				String scenarioId = null;
				if(obj[0]!=null)
					testcaseId = String.valueOf(obj[0]);
				if(obj[1]!=null)
					scenarioId = String.valueOf(obj[1]);
					
				
				
				if (intCount.intValue()>=timesPerTestCaseScenario) {
    			// if this is not in the exclude list
        			if(!isInExcludedTestScenarios((String)obj[0],(String)obj[1]))
        			{

        				// send email to evaluators
        				String b="Test case scenario :" +testcaseId + " / " + scenarioId + " evaluation " +
        						"has been completed and added to excluded list";
    			    	try
    			        {
    			    		MailSender mailSender = (MailSender)this.manager.lookup(MailSender.ROLE);
    			    		mailSender.setFrom("amfortas@bentoweb.org");
    			            mailSender.setTo("amfortas@bentoweb.org");
    			            mailSender.setBody(b);
    			            mailSender.setSubject("Test Case done");
    			            mailSender.send();
    			            this.manager.release(mailSender);           
    			        }
    			        catch(Exception e)
    			        {
    			        	log.debug("mail for exlude tests status not sent: " + e.getMessage());
    			        }

        				//System.out.println(b);
    					// add to exclude list
        				addtoExcludedTestScenarios(testcaseId,scenarioId);
        				
        			}
        			allocated.add(TCDLTestCaseScenario.makeUID(testcaseId,scenarioId));
        		}
        		//System.err.println("tc UID: "+ TestCaseScenario.makeUID((String)obj[0],(String)obj[1]));
        	}
        }
        //
        
       /* String que1 = "select tp.testcaseId, tp.scenarioId from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where COUNT(DISTINCT tp.testcaseId, tp.scenarioId)>2";
        String que2 = "";
        String query = que1+que2;
        Iterator tpIter = getSession().createQuery(query).setInteger(0, 1).iterate();
        while (tpIter.hasNext())  {
        	Object[] obj = (Object[]) tpIter.next();
        	System.err.println("obj0:"+obj[0]);
        	System.err.println("obj1:"+obj[1]);
        }*/
        
        //get from testsuite all scenarios
        ConcurrentHashMap allTestCasesScenariosMap = testSuite.getTestCasesScenarios();
        //System.err.println("tc all: "+ allTestCasesScenariosMap.size());
        // remove done
        Iterator iter = allTestCasesScenariosMap.keySet().iterator();
        if((allocated==null || allocated.size()==0))
        {
            while(iter.hasNext())
            {
            	TestCaseScenario tcs = (TestCaseScenario)allTestCasesScenariosMap.get(iter.next());
                notAllocatedList.add(tcs);
            }
        }
        else
        {
            while(iter.hasNext())
            {
                TCDLTestCaseScenario tcs = (TCDLTestCaseScenario)allTestCasesScenariosMap.get(iter.next());         
                String scenarioId = tcs.getScenarioId();
                String testCaseId = tcs.getTestCaseId();
                String uid = TCDLTestCaseScenario.makeUID(testCaseId,scenarioId);
                if(!allocated.contains(uid))
                    notAllocatedList.add(tcs);
            }
        }  
        List pastAllocatedToProfile = this.getPastAllocatedProfile(ts.getTestSuiteId().intValue(),testProfileId);
        notAllocatedList.removeAll(pastAllocatedToProfile);
        //System.err.println("return: "+ notAllocatedList.size());
        //return new ArrayList();
        // Remove excluded
        try
        {
        	notAllocatedList = removeExludedTestScenarios(notAllocatedList);
        }
        catch(Exception e)
        {
        	
        }
        endTransaction();
        return notAllocatedList;        
    }
    
    private List getPastAllocatedProfile(int testSuiteId, int testProfileId)
    {
    	List l = new ArrayList();
        String q = "from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where (tp.testcaseSet.testSuite.testSuiteId=?) and (tp.testcaseSet.testProfile.testProfileId=?)";        
        Iterator iter = getSession().createQuery(q).setInteger(0,testSuiteId).setInteger(1,testProfileId).iterate();
        while(iter.hasNext())
        {
        	TCDLTestCaseScenario tcs = new TCDLTestCaseScenario();
        	TestcasePool tp = (TestcasePool)iter.next();
        	tcs.setPoolId(tp.getTestcasePoolId().intValue());
        	tcs.setScenarioId(tp.getScenarioId());
        	tcs.setTestCaseId(tp.getTestcaseId());
        	l.add(tcs);
        }
        endTransaction();
        return l;
    }

    public List getTestcaseSetList4TestSuiteNTestProfile(AbstractTestSuite testSuite, TestProfile testProfile) 
    {    	
    	List l = null;
        String q1 = "from org.bentoweb.amfortas.hibernate.om.TestcaseSet as ts where ts.testProfile.testProfileId=?";
        Query query1 = getSession().createQuery(q1).setInteger(0, testProfile.getTestProfileId().intValue());
        Iterator iter1 = query1.iterate();
        if(iter1.hasNext())
        {
        	TestcaseSet testcaseSet = (TestcaseSet) iter1.next();
        	int setId = testcaseSet.getTestcaseSetId().intValue();
            String q2 = "SELECT COUNT(*) from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testcaseSetId=?";
            Query query2 = getSession().createQuery(q2).setInteger(0, setId);            
        	l = query2.list();
        }
        endTransaction();
        return l;
    }    
    public List newTestCasePool(TestProfile tp, List testCaseScenarios, AbstractTestSuite testSuite)
    {
        List newTestCaseScenarios = new ArrayList();
        TestSuite ts = (TestSuite)testSuite.getProps().get(Constants4Mapping.TS_PROP_TEST_SUITE);
        try
        {
            ListIterator iter = testCaseScenarios.listIterator();
            getSession().beginTransaction();
            TestcaseSet tcs = new TestcaseSet();
            tcs.setDateCreation(new Date());
            tcs.setTestProfile(tp);
            tcs.setTestSuite(ts);
            getSession().save(tcs);
            while(iter.hasNext())
            {
                TestCaseScenario tcscen = (TestCaseScenario) iter.next(); 
                TestcasePool tcp = new TestcasePool();
                tcp.setScenarioId(tcscen.getScenarioId());
                tcp.setTestcaseId(tcscen.getTestCaseId());
                tcp.setTestcaseSet(tcs);
                getSession().save(tcp);            
                tcscen.setPoolId(tcp.getTestcasePoolId().intValue());
                newTestCaseScenarios.add(tcscen);
            }
            getSession().getTransaction().commit();
        }
        catch(Exception e)
        {            
        }
        endTransaction();
        return newTestCaseScenarios;
    }

    public List removeExludedTestScenarios(List notAllocatedList)
    {
    	List l = new ArrayList();
    	l.addAll(notAllocatedList);
    	DataSourceComponent datasource = null;

        // When creating a session, use a connection from
        // cocoon's connection pool
        try
        {
            ServiceSelector dbselector = (ServiceSelector) manager
                    .lookup(DataSourceComponent.ROLE + "Selector");
            datasource = (DataSourceComponent) dbselector.select("amfortas");
            // name as defined in cocoon.xconf
        }
        catch (Exception e)
        {
            log.error("sds: " + e.getMessage());
        }
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = datasource.getConnection();
            stmt = conn.createStatement();
            
            // testcase scenarios
            ResultSet rs = stmt.executeQuery("select * from tests_exclude");
            //res.
            while (rs.next()) {
                String testCaseId = rs.getString(2);
                String scenarioId = rs.getString(3);
                
                if(!scenarioId.equals("*"))
                {
                	TCDLTestCaseScenario tcs = new TCDLTestCaseScenario();
                    tcs.setScenarioId(scenarioId);
                	tcs.setTestCaseId(testCaseId);
                	l.remove(tcs);
                	//System.out.println("excluding " + tcs.getUID());
                }
                else
                {
                	Iterator it = notAllocatedList.iterator();
                	while(it.hasNext())
                	{
                		TCDLTestCaseScenario tcs = (TCDLTestCaseScenario) it.next();
                		if(tcs.getTestCaseId().equalsIgnoreCase(testCaseId))
                		{
                			l.remove(tcs);
                            //System.out.println("excluding " + tcs.getUID());
                		}
                	}
                }
   
              }
            stmt.close();
            stmt = null;

            conn.close();
            conn = null;
        } 
        catch (Exception e)
        {
        	
        }
        finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlex) {
                    // ignore -- as we can't do anything about it here
                }

                stmt = null;
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlex) {
                    // ignore -- as we can't do anything about it here
                }

                conn = null;
            }
        }
    	return l;
    }
    private boolean addtoExcludedTestScenarios(String testCaseId, String scenarioId)
    {
    	DataSourceComponent datasource = null;
    	boolean ok=false;
        // When creating a session, use a connection from
        // cocoon's connection pool
        try
        {
            ServiceSelector dbselector = (ServiceSelector) manager
                    .lookup(DataSourceComponent.ROLE + "Selector");
            datasource = (DataSourceComponent) dbselector.select("amfortas");
            // name as defined in cocoon.xconf
        }
        catch (Exception e)
        {
            log.error("sds: " + e.getMessage());
        }
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = datasource.getConnection();
            stmt = conn.createStatement();
            
            // testcase scenarios
            ok = stmt.execute("INSERT INTO tests_exclude (testCaseId, scenarioId)" +
            		" VALUES ('"+testCaseId+"', '"+ scenarioId + "') ");
            
            stmt.close();
            stmt = null;

            conn.close();
            conn = null;
        } 
        catch (Exception e)
        {
        	
        }
        finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlex) {
                    // ignore -- as we can't do anything about it here
                }

                stmt = null;
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlex) {
                    // ignore -- as we can't do anything about it here
                }

                conn = null;
            }
        }
    	return ok;
    }
    private boolean isInExcludedTestScenarios(String testCaseId, String scenarioId)
    {
    	DataSourceComponent datasource = null;
    	boolean ok=false;
        // When creating a session, use a connection from
        // cocoon's connection pool
        try
        {
            ServiceSelector dbselector = (ServiceSelector) manager
                    .lookup(DataSourceComponent.ROLE + "Selector");
            datasource = (DataSourceComponent) dbselector.select("amfortas");
            // name as defined in cocoon.xconf
        }
        catch (Exception e)
        {
            log.error("sds: " + e.getMessage());
        }
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = datasource.getConnection();
            stmt = conn.createStatement();
            
            // testcase scenarios
            String q = "select COUNT(*) from tests_exclude where " +
    		"testCaseId='" + testCaseId + "' and scenarioId='" + scenarioId + "'";
            //System.out.println(q);
            ResultSet rs = stmt.executeQuery(q); 
            rs.next();
            if(rs.getInt(1)>0)
            	ok = true;
            stmt.close();
            stmt = null;

            conn.close();
            conn = null;
        } 
        catch (Exception e)
        {
        	
        }
        finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlex) {
                    // ignore -- as we can't do anything about it here
                }

                stmt = null;
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlex) {
                    // ignore -- as we can't do anything about it here
                }

                conn = null;
            }
        }
    	return ok;
    }   
    private Session getSession() 
    {
    	if(!session.isOpen())
    	{
    		this.session.getSessionFactory().openSession();
    	}
    	return this.session;
    }
    private void endTransaction()
    {
    	session.flush();
    	//session.disconnect();
    	
    }
	public void initialize() throws Exception {
		HibernateFactory hibFactory = null;
		try {
			// get hibernate session
			hibFactory = (HibernateFactory) this.manager
					.lookup(HibernateFactory.ROLE);
			session = (Session) hibFactory.createSession();
			
		} catch (ServiceException ex) {
			log.debug(ex.getMessage());
		} finally {
			this.manager.release(hibFactory);
		}
	}

    public static final String QUERY_ASSISTIVE_TECHNOLOGY_TYPE = "select nameDefault from org.bentoweb.amfortas.hibernate.om.AssistiveTechnologyTypes as at where at.nameDefault<>''";
    public static final String QUERY_ASSISTIVE_TECHNOLOGY_PRODUCT = "select assistiveTechnology.nameDefault from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as tp where (tp.testProfile.testProfileId=?) and (tp.assistiveTechnology.assistiveTechnologyTypes.nameDefault=?)";
    public static final String QUERY_ASSISTIVE_TECHNOLOGY_VERSION = "select version from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as tp where tp.testProfile.testProfileId=?";
    public static final String QUERY_ASSISTIVE_TECHNOLOGY_EXPERIENCE = "select atExperience.atExperienceId from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as tp where tp.testProfile.testProfileId=?";
    //public static final String QUERY_ASSISTIVE_TECHNOLOGY_TYPE_OF_PROFILE = "select DISTINCT assistiveTechnology.assistiveTechnologyTypes.nameDefault from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as tp where (tp.testProfile.testProfileId=?)";
    
    public static final String QUERY_ASSISTIVE_TECHNOLOGY_TYPE_OTHER_OF_PROFILE = "select assistiveTechnologyOther from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as tp where (tp.testProfile.testProfileId=?)";
    
    public static final String QUERY_USER_AGENT_TYPE = "select nameDefault from org.bentoweb.amfortas.hibernate.om.UserAgentTypes as ua where ua.nameDefault<>''";
    public static final String QUERY_USER_AGENT_PRODUCT = "select userAgent.nameDefault from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as tp where (tp.testProfile.testProfileId=?) and (tp.userAgent.userAgentTypes.nameDefault=?)";
    public static final String QUERY_USER_AGENT_VERSION = "select version from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as tp where tp.testProfile.testProfileId=?";
    public static final String QUERY_USER_AGENT_EXPERIENCE = "select atExperience.atExperienceId from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as tp where tp.testProfile.testProfileId=?";
    
    public static final String QUERY_DEVICE_TYPE = "select userUsesDevice.device.nameDefault from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where tp.testProfileId=?";
    public static final String QUERY_DEVICE_PRODUCT = "select userUsesDevice.device.nameDefault from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where tp.testProfileId=?";
    public static final String QUERY_DEVICE_EXPERIENCE = "select userUsesDevice.atExperience.atExperienceId from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where tp.testProfileId=?";

	public void contextualize(Context arg0) throws ContextException {
		// TODO Auto-generated method stub
		
	}

	public void service(ServiceManager manager) throws ServiceException {
		this.manager = manager;
		
	}
	public void dispose() {
		this.session.close();
		this.session = null;
	} 
    
}
