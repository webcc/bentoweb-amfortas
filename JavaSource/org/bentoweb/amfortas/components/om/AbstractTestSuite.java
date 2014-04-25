package org.bentoweb.amfortas.components.om;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.avalon.framework.component.Component;
import org.w3c.dom.Element;

public interface AbstractTestSuite extends Component
{
    String ROLE = AbstractTestSuite.class.getName();
    public String getProperty(String prop);

    /**
     * @return Returns the props.
     */
    public Properties getProps();

    /**
     * @param props The props to set.
     */
    public void setProps(Properties props);

    public ConcurrentHashMap getTestCases();

    public void setTestCases(ConcurrentHashMap testCases);

    public TestCase getTestCase(String testCaseId)
            throws NullPointerException;

    public void setTestCase(String testCaseId, Object testCase);

    public String getTestSuiteId();

    public void setTestSuiteId(String id);

    public void dispose();
    public ConcurrentHashMap getTestCasesScenarios();
    public void setTestCasesScenarios(ConcurrentHashMap testCasesScenarios) ;
	public Element getRuleSetElement() ;
	public void setRuleSetElement(Element ruleSetElement) ;
}