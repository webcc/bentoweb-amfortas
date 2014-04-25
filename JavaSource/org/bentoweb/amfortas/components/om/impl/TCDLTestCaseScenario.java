package org.bentoweb.amfortas.components.om.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.bentoweb.amfortas.components.om.TestCase;
import org.bentoweb.amfortas.components.om.TestCaseScenario;
import org.bentoweb.amfortas.pojo.AnswerLikertscale;
import org.bentoweb.amfortas.pojo.AnswerMultiple;
import org.bentoweb.amfortas.pojo.AnswerOpen;
import org.bentoweb.amfortas.pojo.AnswerType;
import org.bentoweb.amfortas.pojo.AnswerYesno;
import org.bentoweb.amfortas.pojo.AnswerYesnoopen;
import org.bentoweb.amfortas.pojo.AssistiveTechnologyProduct;
import org.bentoweb.amfortas.pojo.DeviceProduct;
import org.bentoweb.amfortas.pojo.Product;
import org.bentoweb.amfortas.pojo.UserAgentProduct;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.bentoweb.amfortas.util.XmlHelper;

public class TCDLTestCaseScenario implements TestCaseScenario {
	private String testCaseId = null;
	private String scenarioId = null;
	private int grade = 0;
	private int poolId = 0;
	private AnswerType answer = null;
	private String expResult = null;
	private List<Product> assistiveTechnologies = new ArrayList<Product>();
	private List<String> assistiveTechnologyTypes = new ArrayList<String>();
	private List<Product> userAgents = new ArrayList<Product>();
	private List<String> userAgentsTypes = new ArrayList<String>();
	private List<Product> devices = new ArrayList<Product>();
	private List<String> devicesTypes = new ArrayList<String>();
	private String[] disabilities = null;
	private Node scenarioNode = null;
	
	public TCDLTestCaseScenario() {

	}
	public String toString()
	{
		return XmlHelper.elementToString((Element) this.getScenarioNode());
	}
	public void loadScenario(TestCase testCase, Node scenario)
			throws TransformerException {
		this.setScenarioNode(scenario);
		this.loadDisabilities();
		this.loadAssistiveTechnologies();
		this.loadAssistiveTechnologiesTypes();
		this.loadUserAgents();
		this.loadUserAgentsTypes();
		this.loadDevices();
		this.loadDevicesTypes();

		String expectedResult = testCase.getExpectedResult();
		this.setTestCaseId(testCase.getId());
		this.setScenarioId(scenario.getAttributes().getNamedItem("id")
				.getTextContent());
		// store answers from TCDL in testcasescenarios for later analysis
		// check answer type
		Node questNode = XPathAPI.selectSingleNode(scenario,
				"//*[local-name()='questions']/*[1]");
		Element a = (Element) ((Element) this.getScenarioNode()).getElementsByTagName("questionText").item(0);
		Node qtxtNode = a.getElementsByTagName("p").item(0);
		try
		{
			
			/*
			Node qtxtNode = XPathAPI
					.selectSingleNode(questNode,
							"//*[local-name()='questionText'][@xml:lang='en']/*[local-name()='p']");
			*/
			if (questNode.getNodeName().equals("yesNoOpenQuestion")) {
				AnswerYesnoopen answer = new AnswerYesnoopen();
				answer.setExpResult(expectedResult);
				answer.setQuestion(qtxtNode.getTextContent());
				Element questElem = (Element) questNode;
				answer.setAnswer(questElem.getElementsByTagName("optionYes")
						.item(0).getAttributes().getNamedItem("value")
						.getTextContent().replace("+", ""), "yes");
				answer.setAnswer(questElem.getElementsByTagName("optionNo").item(0)
						.getAttributes().getNamedItem("value").getTextContent(),
						"no");
				Node optNode = XPathAPI
						.selectSingleNode(questNode,
								"//*[local-name()='optionOther'][@xml:lang='en']//*[local-name()='p']");
				answer.setOptionother(optNode.getTextContent());
				this.setAnswer(answer);
			}
			if (questNode.getNodeName().equals("multipleChoice")) {
				AnswerMultiple answer = new AnswerMultiple();
				answer.setExpResult(expectedResult);
				answer.setQuestion(qtxtNode.getTextContent());
				NodeList multNode = XPathAPI.selectNodeList(questNode,
						"//*[local-name()='choice']");
				for (int j = 0; j < multNode.getLength(); j++) {
					Element elem = (Element) multNode.item(j);
					answer
							.setAnswer(elem.getElementsByTagName("value").item(0)
									.getTextContent().replace("+", ""), elem
									.getElementsByTagName("label").item(0)
									.getTextContent());
				}
				this.setAnswer(answer);
			}
			if (questNode.getNodeName().equals("likertScale")) {
				AnswerLikertscale answer = new AnswerLikertscale();
				answer.setExpResult(expectedResult);
				answer.setQuestion(qtxtNode.getTextContent());
				NodeList multNode = XPathAPI.selectNodeList(questNode,
						"//*[local-name()='likertLevel']");
				for (int j = 0; j < multNode.getLength(); j++) {
					Element elem = (Element) multNode.item(j);
					answer
							.setAnswer(elem.getElementsByTagName("value").item(0)
									.getTextContent().replace("+", ""), elem
									.getElementsByTagName("label").item(0)
									.getTextContent());
				}
				this.setAnswer(answer);
			}
			if (questNode.getNodeName().equals("yesNoQuestion")) {
				AnswerYesno answer = new AnswerYesno();
				answer.setQuestion(qtxtNode.getTextContent());
				Element questElem = (Element) questNode;
				answer.setAnswer(questElem.getElementsByTagName("optionYes")
						.item(0).getAttributes().getNamedItem("value")
						.getTextContent().replace("+", ""), "yes");
				answer.setAnswer(questElem.getElementsByTagName("optionNo").item(0)
						.getAttributes().getNamedItem("value").getTextContent(),
						"no");
				this.setAnswer(answer);
			}
			if (questNode.getNodeName().equals("openQuestion")) {
				AnswerOpen answer = new AnswerOpen();
				answer.setQuestion(qtxtNode.getTextContent());
				this.setAnswer(answer);
			}
		}
		catch(Exception e)
		{
			
		}
	}

	/** *********************************************************************** */
	/**
	 * @return a list of disabilities in a scenario
	 */
	private void loadDisabilities() {		
		NodeList disabilitiesList = ((Element) this.getScenarioNode())
				.getElementsByTagName("disability");
		String[] disabilities = new String[disabilitiesList.getLength()];
		for (int i = 0; i < disabilitiesList.getLength(); i++) {
			disabilities[i] = disabilitiesList.item(i).getTextContent();
		}
		this.disabilities = disabilities;
	}
	
	private Element getExperience()
	{
		return (Element)((Element) this.getScenarioNode()).getElementsByTagName("experience").item(0);
	}

	private void loadAssistiveTechnologies() throws TransformerException {
		NodeList productList = getExperience().getElementsByTagName("AssistiveTechnology");
		List<Product> atList = new ArrayList<Product>(productList.getLength());
		for (int i = 0; i < productList.getLength(); i++) {
			Element productE = (Element) productList.item(i);
			AssistiveTechnologyProduct p = new AssistiveTechnologyProduct();
			p.create(productE);
			atList.add(p);
		}
		this.assistiveTechnologies = atList;
	}

	private void loadUserAgents() throws TransformerException {
		NodeList productList = getExperience().getElementsByTagName("UserAgent");
		List<Product> uaList = new ArrayList<Product>(productList.getLength());
		for (int i = 0; i < productList.getLength(); i++) {
			Element productE = (Element) productList.item(0);
			UserAgentProduct p = new UserAgentProduct();
			p.create(productE);
			uaList.add(p);
		}
		this.userAgents = uaList;
	}

	private void loadDevices() throws TransformerException {
		NodeList productList = getExperience().getElementsByTagName("Device");
		List<Product> deviceList = new ArrayList<Product>(productList
				.getLength());
		for (int i = 0; i < productList.getLength(); i++) {
			Element productE = (Element) productList.item(0);
			DeviceProduct p = new DeviceProduct();
			p.create(productE);
			deviceList.add(p);
		}
		this.devices = deviceList;
	}

	/**
	 * load all distinct types of AT in tcdl file
	 * 
	 * @throws TransformerException
	 */
	private void loadAssistiveTechnologiesTypes() throws TransformerException {
		Iterator<Product> iter = this.getAssistiveTechnologies().iterator();
		while (iter.hasNext()) { 
			AssistiveTechnologyProduct product = (AssistiveTechnologyProduct) iter
					.next();
			String type = (String) product.getType();
			if (!this.getAssistiveTechnologyTypes().contains(type))
				this.getAssistiveTechnologyTypes().add(type);
		}
	}

	private void loadUserAgentsTypes() throws TransformerException {
		Iterator<Product> iter = this.getUserAgents().iterator();
		while (iter.hasNext()) {
			UserAgentProduct product = (UserAgentProduct) iter.next();
			String type = (String) product.getType();
			if (!this.getUserAgentsTypes().contains(type))
				this.getUserAgentsTypes().add(type);
		}
	}

	private void loadDevicesTypes() throws TransformerException {
		Iterator<Product> iter = this.getDevices().iterator();
		while (iter.hasNext()) {
			DeviceProduct product = (DeviceProduct) iter.next();
			String type = (String) product.getType();
			if (!this.getDevicesTypes().contains(type))
				this.getDevicesTypes().add(type);
		}
	}

	public List<Product> getAssistiveTechnologyProductsOfType(String type) {
		List<Product> pList = new ArrayList<Product>();
		Iterator<Product> iter = this.getAssistiveTechnologies().iterator();
		while (iter.hasNext()) {
			AssistiveTechnologyProduct product = (AssistiveTechnologyProduct) iter
					.next();
			if (product.getType().equalsIgnoreCase(type))
				pList.add(product);
		}
		return pList;
	}
	public List<Product> getUserAgentProductsOfType(String type) {
		List<Product> pList = new ArrayList<Product>();
		Iterator<Product> iter = this.getUserAgents().iterator();
		while (iter.hasNext()) {
			UserAgentProduct product = (UserAgentProduct) iter.next();
			if (product.getType().equalsIgnoreCase(type))
				pList.add(product);
		}
		return pList;
	}
	public List<String> getAssistiveTechnologiesProducts(String scenarioId,
			String type) throws TransformerException {
		List<Product> ats = this.getAssistiveTechnologies();
		Iterator<Product> iter = ats.iterator();
		List<String> products = new ArrayList<String>();
		while (iter.hasNext()) {
			AssistiveTechnologyProduct p = (AssistiveTechnologyProduct) iter
					.next();
			if (p.getType().equalsIgnoreCase(type)) {
				String product = p.getName();
				if (!products.contains(product))
					products.add(product);
			}

		}
		return products;
	}

	public boolean equals(Object o) {
		TCDLTestCaseScenario t = (TCDLTestCaseScenario) o;
		if (t.getScenarioId().equals(this.getScenarioId())
				&& t.getTestCaseId().equals(this.getTestCaseId()))
			return true;
		else
			return false;
	}

	
	/***************************** Setters and Getters ***************************************************/
	public List<String> getAssistiveTechnologyTypes() {
		return assistiveTechnologyTypes;
	}
	public void setAssistiveTechnologyTypes(
			List<String> assistiveTechnologyTypes) {
		this.assistiveTechnologyTypes = assistiveTechnologyTypes;
	}
	
	public int getPoolId() {
		return poolId;
	}
	public void setPoolId(int poolId) {
		this.poolId = poolId;
	}
	public String getUID() {
		return this.getTestCaseId() + UID + this.getScenarioId();
	}

	public static String getScenarioId(String uids) {
		return uids.substring(uids.indexOf(UID) + UID.length(), uids.length());
	}

	public static String getTestCaseId(String uids) {
		return uids.substring(0, uids.indexOf(UID) + UID.length());
	}

	public static String makeUID(String testCaseId, String scenarioId) {
		return testCaseId + UID + scenarioId;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(String scenarioId) {
		this.scenarioId = scenarioId;
	}

	public String getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}
	public void setAnswer(AnswerType answer) {
		this.answer = answer;
	}

	public AnswerType getAnswer() {
		return this.answer;
	}
	public void setExpResult(String expResult) {
		this.expResult = expResult;

	}
	public String getExpresult() {
		return this.expResult;
	}
	public List<Product> getAssistiveTechnologies() {
		return assistiveTechnologies;
	}
	public void setAssistiveTechnologies(List<Product> assistiveTechnologies) {
		this.assistiveTechnologies = assistiveTechnologies;
	}
	public String[] getDisabilities() {
		return disabilities;
	}
	public void setDisabilities(String[] disabilities) {
		this.disabilities = disabilities;
	}
	public Node getScenarioNode() {
		return scenarioNode;
	}
	public void setScenarioNode(Node scenarioNode) {
		this.scenarioNode = scenarioNode;
	}
	public List<Product> getUserAgents() {
		return userAgents;
	}
	public void setUserAgents(List<Product> userAgents) {
		this.userAgents = userAgents;
	}
	public List<String> getUserAgentsTypes() {
		return userAgentsTypes;
	}
	public void setUserAgentsTypes(List<String> userAgentsTypes) {
		this.userAgentsTypes = userAgentsTypes;
	}
	public List<Product> getDevices() {
		return devices;
	}
	public void setDevices(List<Product> devices) {
		this.devices = devices;
	}
	public List<String> getDevicesTypes() {
		return devicesTypes;
	}
	public void setDevicesTypes(List<String> devicesTypes) {
		this.devicesTypes = devicesTypes;
	}
}
