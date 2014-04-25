package org.bentoweb.amfortas.components.om.impl;

import java.util.Iterator;

import org.apache.avalon.framework.activity.Disposable;
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
import org.bentoweb.amfortas.components.persistence.DatasourceAdapter;
import org.bentoweb.amfortas.components.testing.impl.Constants4Mapping;
import org.bentoweb.amfortas.hibernate.om.TestProfile;

public class DefaultTestProfile implements AbstractTestProfile, Serviceable, Initializable, Disposable, Contextualizable
{
    private static Log log = LogFactory.getLog(DefaultTestProfile.class);
    private TestProfile testProfile = null;
    private ServiceManager manager = null;
    private int testProfileId = 0;
    private DatasourceAdapter datasourceAdapter = null;    

    public void setTestProfile(Object testProfile) 
    {
        this.testProfile = (TestProfile) testProfile;         
    }

    public void contextualize(Context context) throws ContextException 
    {
        Request r = ContextHelper.getRequest(context);
        this.testProfileId = Integer.parseInt(String.valueOf(r.getAttribute(Constants4Mapping.TESTPROFILEID)));
        if(log.isDebugEnabled())
            log.debug(this.getClass().getName() +  " got param from request context" + this.testProfileId);         
    }
	/** 
	 * 
	 */
	public void service(ServiceManager manager) throws ServiceException {
		this.manager = manager;
	}
	public void initialize() throws Exception 
	{
	    try {
	    	datasourceAdapter = (DatasourceAdapter)this.manager.lookup(DatasourceAdapter.ROLE);
	    	Iterator iter = datasourceAdapter.select_TestProfile_where_TpId(testProfileId).iterator();
	        if(iter.hasNext())
	            this.testProfile = (TestProfile) iter.next();
		} catch (ServiceException ex) {
			log.debug(ex.getMessage());
		} finally {
			this.manager.release(datasourceAdapter);
		}
	}    
    public void dispose() 
    {
        this.testProfile=null;
    }

    public Object getTestProfile() throws NullPointerException
    {
        return this.testProfile;
    }


}
