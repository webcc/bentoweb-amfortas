package org.bentoweb.amfortas.components.om.impl;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.avalon.framework.activity.Disposable;
import org.bentoweb.amfortas.components.om.AbstractTestSuite;
import org.bentoweb.amfortas.components.om.TestCase;
import org.w3c.dom.Element;

public class TCDLTestSuite implements AbstractTestSuite, Disposable
{
    private Properties props = new Properties();
    ConcurrentHashMap tcdlTestCases = new ConcurrentHashMap();
    String testSuiteId = null;
    ConcurrentHashMap testCasesScenarios = new ConcurrentHashMap();
    Element ruleSetElement = null;
    
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestSuite#getProps()
     */
    public Properties getProps() {
        return props;
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestSuite#setProps(java.util.Properties)
     */
    public void setProps(Properties props) {
        this.props = props;
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestSuite#getTestCases()
     */
    public ConcurrentHashMap getTestCases() 
    {
        return this.tcdlTestCases;
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestSuite#setTestCases(java.util.HashMap)
     */
    public void setTestCases(ConcurrentHashMap testCases) {
        this.tcdlTestCases = testCases;
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestSuite#getTestCase(java.lang.String)
     */
    public TestCase getTestCase(String testCaseId) throws NullPointerException
    {        
        return (TestCase) this.tcdlTestCases.get(testCaseId);
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestSuite#setTestCase(java.lang.String, java.lang.Object)
     */
    public void setTestCase(String testCaseId, Object testCase) {
        this.tcdlTestCases.put(testCaseId,testCase);
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestSuite#getTestSuiteId()
     */
    public String getTestSuiteId() 
    {
        return this.testSuiteId;
    }
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestSuite#setTestSuiteId(java.lang.String)
     */
    public void setTestSuiteId(String id) 
    {
        this.testSuiteId = id;
    }    
    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestSuite#recycle()
     */
    
    public String getProperty(String prop)
    {
        return (String)this.props.getProperty(prop);
    }
    /**
     * @return Returns the testCasesScenarios.
     */
    public ConcurrentHashMap getTestCasesScenarios() {
        return testCasesScenarios;
    }
    /**
     * @param testCasesScenarios The testCasesScenarios to set.
     */
    public void setTestCasesScenarios(ConcurrentHashMap testCasesScenarios) {
        this.testCasesScenarios = testCasesScenarios;
    }
    public void dispose() 
    {
        this.tcdlTestCases = null;
        this.testSuiteId = null;
        this.props = null;                
    }
	public Element getRuleSetElement() {
		return ruleSetElement;
	}
	public void setRuleSetElement(Element ruleSetElement) {
		this.ruleSetElement = ruleSetElement;
	}
}
