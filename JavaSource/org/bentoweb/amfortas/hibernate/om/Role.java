package org.bentoweb.amfortas.hibernate.om;

import java.util.Set;


/**
 * Role generated by hbm2java
 */

public class Role  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = -7182073086070187218L;
	private Integer roleId;
     private String nameKey;
     private String nameDefault;
     private Set userHasRoles;


    // Constructors

    /** default constructor */
    public Role() {
    }
    
    /** constructor with id */
    public Role(Integer roleId) {
        this.roleId = roleId;
    }

    

   
    // Property accessors

    public Integer getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getNameKey() {
        return this.nameKey;
    }
    
    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getNameDefault() {
        return this.nameDefault;
    }
    
    public void setNameDefault(String nameDefault) {
        this.nameDefault = nameDefault;
    }

    public Set getUserHasRoles() {
        return this.userHasRoles;
    }
    
    public void setUserHasRoles(Set userHasRoles) {
        this.userHasRoles = userHasRoles;
    }
   








}