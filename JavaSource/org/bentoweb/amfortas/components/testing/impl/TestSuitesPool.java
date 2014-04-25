package org.bentoweb.amfortas.components.testing.impl;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bentoweb.amfortas.components.om.AbstractTestSuite;
import org.bentoweb.amfortas.components.persistence.DatasourceAdapter;
import org.bentoweb.amfortas.components.testing.AbstractTestSuiteFactory;
import org.bentoweb.amfortas.components.testing.TestSuitesPoolable;
import org.bentoweb.amfortas.hibernate.om.TestSuite;

public class TestSuitesPool implements TestSuitesPoolable, Initializable, Serviceable, Disposable, ThreadSafe
{
    private ConcurrentHashMap testSuitesMap = new ConcurrentHashMap();    
    private static Log log = LogFactory.getLog(TestSuitesPool.class);    
    private ServiceManager manager = null;  
    private boolean initialized=false;    
    
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.TestSuitesPoolable#service(org.apache.avalon.framework.service.ServiceManager)
     */
    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }
    /*
     *  (non-Javadoc)
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.TestSuitesPoolable#initialize()
     */
    public void initialize() throws Exception 
    {         
    	log.trace("Start initializing test suites");
        AbstractTestSuiteFactory tsFactory = null;
        DatasourceAdapter datasourceAdapter = null;
        try 
        {            
        	datasourceAdapter = (DatasourceAdapter)this.manager.lookup(DatasourceAdapter.ROLE);
            tsFactory = (AbstractTestSuiteFactory)this.manager.lookup(AbstractTestSuiteFactory.ROLE);
            // get active test suites from db
            List activeTestSuitesList = datasourceAdapter.getActiveTestSuites();
            ListIterator iter = activeTestSuitesList.listIterator();
            while(iter.hasNext())
            {
                TestSuite ts = (TestSuite)iter.next();
                System.out.println("Initializing testsuite: " + ts.getTestSuiteId());
                String tsId = ts.getTestSuiteId().toString();
                //generate current test suite
                AbstractTestSuite testSuite = tsFactory.generate(tsId);
                //pool it
                this.testSuitesMap.put(tsId,testSuite);
            }           
            this.initialized=true;
            System.out.println("Done initializing test suites");
        } 
        catch (ServiceException ex) 
        {
            log.debug(ex.getMessage());
            ex.printStackTrace();
        } 
        finally 
        {
            this.manager.release(tsFactory);  
            this.manager.release(datasourceAdapter);  
        }       
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.TestSuitesPoolable#getActiveTestSuites()
     */
    public ConcurrentHashMap getActiveTestSuites()
    {
        return this.testSuitesMap;
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.TestSuitesPoolable#getTestSuite(java.lang.String)
     */
    public AbstractTestSuite getTestSuite(String tsId) throws Exception
    {
        if(!this.initialized)
            this.initialize();
        AbstractTestSuite ts = null;
        if(this.testSuitesMap!=null && this.testSuitesMap.size()>0 && this.testSuitesMap.containsKey(tsId))
            ts = (AbstractTestSuite) this.testSuitesMap.get(tsId);  
        return ts;
    }
    public void putTestSuite(AbstractTestSuite ts)
    {
        String tsId = ts.getTestSuiteId();
        this.testSuitesMap.put(tsId,ts);
    }
    public boolean isInitialized()
    {
        return this.initialized;
    }
	public void dispose() 
    {        
	    this.initialized = false;
        this.testSuitesMap = null;
        this.manager = null;
        System.out.println(TestSuitesPool.class.toString() + "disposed");        
	}
    public void recycle() 
    {        
        this.initialized = false;
        this.testSuitesMap = null;
        this.manager = null;
        System.out.println(TestSuitesPool.class.toString() + "recycled");        
    }

}
