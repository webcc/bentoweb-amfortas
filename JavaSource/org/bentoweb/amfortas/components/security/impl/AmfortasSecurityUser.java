package org.bentoweb.amfortas.components.security.impl;

import org.bentoweb.amfortas.util.Constants;
import org.osoco.cowarp.User;

import java.util.Iterator;
import java.util.HashMap;

public class AmfortasSecurityUser implements User {
	
	String userId = null;
	HashMap userAttributes = null;
	
	public AmfortasSecurityUser() {
		userAttributes = new HashMap();
	}
	
	public void setId(Integer userId) {
		this.userId = userId.toString();
	}
	
    /**
     * Return the unique id of this user.
     * @return The identifier.
     */
    public String getId() {
    	return userId;
    }

    /**
     * Set an information about the user.
     * For session replication the value of the attribute should
     * be {@link java.io.Serializable}.
     * @param key   The key identifying the information.
     * @param value The value of the information.
     */
    public void setAttribute(String key, Object value) {
    	userAttributes.put(key, value);
    }

    /**
     * Remove an information about the user.
     * @param key The key identifying the information.
     */
    public void removeAttribute(String key) {
    	userAttributes.remove(key);
    }

    /**
     * Get information about the user.
     * @param key The key identifying the information.
     * @return The value or null.
     */
    public Object getAttribute(String key) {
    	return userAttributes.get(key);
    }

    /**
     * Return all available names.
     * @return An Iterator for the names (Strings).
     */
    public Iterator getAttributeNames() {
    	return userAttributes.keySet().iterator();
    }

    /**
     * Check if the user is in a given role.
     * This method can't check for a role handled by the servlet engine,
     * it only handles indendently specified roles.
     * Therefore, it is advisable to not call this method directly, but
     * use the provided methods from the {@link ApplicationUtil} instead.
     *
     * @param role The role to test.
     * @return Returns true if the user has the role, otherwise false.
     */
    public boolean isUserInRole(String role) {
    	//TODO: extend to handle multiple roles
    	String user_role_name = (String) this.getAttribute(Constants.USER_ROLE_NAME); 
    	//System.out.println("User role: " +  user_role_name);
    	if(role.equalsIgnoreCase(user_role_name))
    		return true;
    	else
    		return false;
    }
    
	
}