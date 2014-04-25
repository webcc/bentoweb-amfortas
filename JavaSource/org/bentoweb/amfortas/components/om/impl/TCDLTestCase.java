package org.bentoweb.amfortas.components.om.impl;

import java.util.Date;

import org.bentoweb.amfortas.components.om.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class TCDLTestCase implements TestCase
{
    private String id = null; 
    private long lastModified = 0; 
    private Date date = null;   
    private Document dom;   
    
    
	public String getExpectedResult() {
    	Element rootElement = (Element) this.dom.getDocumentElement(); 
    	Element rules_element =  (Element) rootElement.getElementsByTagName("rules").item(0);
        NodeList rules = rules_element.getElementsByTagName("rule");
        String expectedResult = null;
        for(int k=0;k<rules.getLength();k++)
        {
        	Element rule = (Element) rules.item(k);
        	if(rule.getAttribute("primary").equalsIgnoreCase("yes"))
        	{
        		Element ee = (Element)(rule.getElementsByTagName("locations").item(0));
        		expectedResult = ee.getAttribute("expectedResult");
        		break;
        	}
        }
        return expectedResult;
	}

	public String getStatus() {
    	Element rootElement = this.dom.getDocumentElement();
		Element formalMetadata = (Element) rootElement.getElementsByTagName("formalMetadata").item(0);
        String tcStatus = formalMetadata.getElementsByTagName("status").item(0).getTextContent();
        return tcStatus;
	}

	/* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestCase#getDate()
     */
    public Date getDate() {
        return date;
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestCase#setDate(java.util.Date)
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestCase#getId()
     */
    public String getId() {
        return id;
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestCase#setId(java.lang.String)
     */
    public void setId(String id) {
        this.id = id;
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestCase#getLastModified()
     */
    public long getLastModified() {
        return lastModified;
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestCase#setLastModified(long)
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestCase#getTcdl()
     */
    public Node getTestCaseNode() {
        return this.dom.getElementsByTagName("testCase").item(0);
    }
    public NodeList getScenarioNodeList()
    {
    	Element testcaseElement = (Element)getTestCaseNode();    	
    	return  testcaseElement.getElementsByTagName("scenario");
    }
    public Node getScenarioNode(String scenarioId)
    {
    	NodeList scenarios = getScenarioNodeList();
    	for(int i=0;i<scenarios.getLength();i++)
    	{
    		Node scenario = scenarios.item(0);
    		if(scenario.getAttributes().getNamedItem("id").getTextContent().equalsIgnoreCase(scenarioId))
    			return scenario;
    	}
    	return null;
    }  
	public void setDom(Document doc) {
		this.dom = doc;
		
	}    
	public boolean isCandidate(String statusReady) {
    	boolean iscandidate = false;
    	if(this.getStatus().equalsIgnoreCase(statusReady))
    	{
        	NodeList scenarioNodes = getScenarioNodeList(); 
            if(scenarioNodes!=null && scenarioNodes.getLength()>0)
        		iscandidate = true;
    	}
    	
		return iscandidate;
	}

}
