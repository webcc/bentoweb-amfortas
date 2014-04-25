package org.bentoweb.amfortas.components.om;

import java.util.List;

import javax.xml.transform.TransformerException;

import org.bentoweb.amfortas.pojo.AnswerType;
import org.bentoweb.amfortas.pojo.Product;
import org.w3c.dom.Node;

public interface TestCaseScenario {
	public static final String UID = "\t\t";

	public abstract void loadScenario(TestCase testCase, Node scenario)
			throws TransformerException;
	public abstract List<Product> getAssistiveTechnologyProductsOfType(
			String type);
	public abstract List<Product> getUserAgentProductsOfType(String type);
	public abstract List<String> getAssistiveTechnologyTypes();
	public abstract void setAssistiveTechnologyTypes(
			List<String> assistiveTechnologyTypes);
	public abstract List<String> getAssistiveTechnologiesProducts(
			String scenarioId, String type) throws TransformerException;
	public abstract boolean equals(Object o);
	public abstract int getPoolId();
	public abstract void setPoolId(int poolId);
	public abstract String getUID();
	public abstract int getGrade();
	public abstract void setGrade(int grade);
	public abstract String getScenarioId();
	public abstract void setScenarioId(String scenarioId);
	public abstract String getTestCaseId();
	public abstract void setTestCaseId(String testCaseId);
	public abstract void setAnswer(AnswerType answer);
	public abstract AnswerType getAnswer();
	public abstract void setExpResult(String expResult);
	public abstract String getExpresult();
	public abstract List<Product> getAssistiveTechnologies();
	public abstract void setAssistiveTechnologies(
			List<Product> assistiveTechnologies);
	public abstract String[] getDisabilities();
	public abstract void setDisabilities(String[] disabilities);
	public abstract Node getScenarioNode();
	public abstract void setScenarioNode(Node scenarioNode);
	public abstract List<Product> getUserAgents();
	public abstract void setUserAgents(List<Product> userAgents);
	public abstract List<String> getUserAgentsTypes();
	public abstract void setUserAgentsTypes(List<String> userAgentsTypes);
	public abstract List<Product> getDevices();
	public abstract void setDevices(List<Product> devices);
	public abstract List<String> getDevicesTypes();
	public abstract void setDevicesTypes(List<String> devicesTypes);
}