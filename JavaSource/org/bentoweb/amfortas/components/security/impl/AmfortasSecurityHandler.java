package org.bentoweb.amfortas.components.security.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.sitemap.SitemapParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bentoweb.amfortas.components.persistence.DatasourceAdapter;
import org.bentoweb.amfortas.hibernate.om.UserHasRole;
import org.bentoweb.amfortas.util.Constants;
import org.bentoweb.amfortas.util.EncryptionService;
import org.osoco.cowarp.SecurityHandler;
import org.osoco.cowarp.User;


public class AmfortasSecurityHandler implements SecurityHandler, Serviceable, Initializable, Disposable {
	
	private static Log log = LogFactory.getLog(AmfortasSecurityHandler.class);
	private ServiceManager manager = null;
	private String uniqueId = null;
	DatasourceAdapter datasourceAdapter = null;
    /**
     * Try to authenticate the user.
     * @param context The context for the login operation.
     * @return The user if the authentication is successful, null otherwise.
     * @throws Exception If something goes wrong.
     */
    public org.osoco.cowarp.User login(Map context) throws Exception {
    	
    	 //get log-in values
    	SitemapParameters params = (SitemapParameters)context.get(Constants.SITEMAPPARAMETER_PARAMETERS);
    	
    	//check database for username / password
    	String encPw = EncryptionService.getInstance().encrypt(params.getParameter(Constants.SITEMAPPARAMETER_PASSWORD));
    	Iterator iter = datasourceAdapter.select_User_where_userpass(params.getParameter(Constants.SITEMAPPARAMETER_EMAIL), encPw).iterator();
   	
    	//if exists set AmfortasSecurityUser class
    	AmfortasSecurityUser secUser = null;
    	if (iter.hasNext()) {
    		//create object set attributes
    		org.bentoweb.amfortas.hibernate.om.User user = (org.bentoweb.amfortas.hibernate.om.User) iter.next();
            // if the account has been activated
            String userstatus = String.valueOf(user.getStatusUser());
            //System.out.println("status: " + userstatus);
            
            List userhasroles = datasourceAdapter.select_UserHasRole_where_Username(user.getEmail());
   	
            String user_role = null;
        	if (userhasroles!=null && userhasroles.size()>0)
        	{
        		UserHasRole ur = (UserHasRole)userhasroles.get(0);
        		user_role = ur.getRole().getNameKey();
        	}
            // TODO: When a user is activated the status is cached and need restart for login.. 
            if(userstatus.equalsIgnoreCase(Constants.USER_ACCOUNT_STATUS_ACCEPTED))
            { 
                secUser = new AmfortasSecurityUser();
                secUser.setId(user.getUserId());  
                secUser.setAttribute(Constants.USER_ROLE_NAME,user_role);
            }
    	} 	
    	return secUser;
    }

    /**
     * This notifies the security-handler that a user logs out.
     * @param context The context for the login operation.
     * @param user    The user object.
     */
    public void logout(Map context, User user) {
    	//nothing to do.
    }

    /**
     * Return a unique identifier for this security handler.
     * For session replication to work, a security handler must deliver
     * the same identifier across systems!
     * @return A unique identifier.
     */
    public String getId() {
    	return this.uniqueId;
    }
    
	/** 
	 * 
	 */
	public void service(ServiceManager manager) throws ServiceException {
		this.manager = manager;
	}
	
	public void initialize() throws Exception {
		
		this.uniqueId = new java.rmi.dgc.VMID().toString();
	    try {
	    	datasourceAdapter = (DatasourceAdapter)this.manager.lookup(DatasourceAdapter.ROLE);
		} catch (ServiceException ex) {
			log.debug(ex.getMessage());
		} finally {
			this.manager.release(datasourceAdapter);
		}
	}
	
	public void dispose() {

	}
}