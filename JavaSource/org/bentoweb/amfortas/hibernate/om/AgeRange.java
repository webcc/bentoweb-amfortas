package org.bentoweb.amfortas.hibernate.om;

import java.util.Set;


/**
 * AgeRange generated by hbm2java
 */

public class AgeRange  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 7808476420636401333L;
	private Integer ageRangeId;
     private String nameKey;
     private String nameDefault;
     private Set userProfiles;


    // Constructors

    /** default constructor */
    public AgeRange() {
    }
    
    /** constructor with id */
    public AgeRange(Integer ageRangeId) {
        this.ageRangeId = ageRangeId;
    }

    

   
    // Property accessors

    public Integer getAgeRangeId() {
        return this.ageRangeId;
    }
    
    public void setAgeRangeId(Integer ageRangeId) {
        this.ageRangeId = ageRangeId;
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

    public Set getUserProfiles() {
        return this.userProfiles;
    }
    
    public void setUserProfiles(Set userProfiles) {
        this.userProfiles = userProfiles;
    }
   








}