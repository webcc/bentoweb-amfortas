package org.bentoweb.amfortas.pojo;

import org.w3c.dom.Element;

public class Product {
	String name = null;
	String type = null;
	String version = "";
	String experience = "";
	
	public Product()
	{
		
	}
	public void create(Element productE)
	{
    	this.setExperience(productE.getAttribute("experience"));
    	this.setName(productE.getAttribute("product"));
    	this.setType(productE.getAttribute("type"));
    	this.setVersion(productE.getAttribute("version"));
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
