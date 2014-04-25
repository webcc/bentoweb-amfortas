package org.bentoweb.amfortas.components.testing.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.mail.MailSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.apache.xpath.XPathAPI;
import org.bentoweb.amfortas.components.om.AbstractTestSuite;
import org.bentoweb.amfortas.components.om.TestCase;
import org.bentoweb.amfortas.components.om.TestCaseScenario;
import org.bentoweb.amfortas.components.om.impl.TCDLTestCase;
import org.bentoweb.amfortas.components.om.impl.TCDLTestCaseScenario;
import org.bentoweb.amfortas.components.om.impl.TCDLTestSuite;
import org.bentoweb.amfortas.components.persistence.DatasourceAdapter;
import org.bentoweb.amfortas.components.testing.AbstractTestSuiteFactory;
import org.bentoweb.amfortas.hibernate.om.TestSuite;
import org.bentoweb.amfortas.util.Constants;
import org.bentoweb.amfortas.util.XmlHelper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
public class TCDLTestSuiteFactory implements AbstractTestSuiteFactory, Contextualizable,Serviceable,Initializable, Disposable, Parameterizable
{         
    private ServiceManager manager = null;
    private MailSender mailSender = null;
    private Parameters config = null;
    private DatasourceAdapter datasourceAdapter = null;
    private Log log = LogFactory.getLog(TCDLTestSuiteFactory.class);
              
    /* (non-Javadoc)
	 * @see org.bentoweb.amfortas.components.testing.impl.aa#generate(java.lang.String)
	 */
    public AbstractTestSuite generate(String testSuiteId)
            throws IOException, ParserConfigurationException, SAXException 
    {
        Properties testSuiteProps = getTestSuiteProperties(testSuiteId);
        TCDLTestSuite tCDLTestSuite = new TCDLTestSuite();
        tCDLTestSuite.setTestSuiteId(testSuiteId);
        tCDLTestSuite.setProps(testSuiteProps);
        tCDLTestSuite = loadTestCases(tCDLTestSuite);
        return tCDLTestSuite;
    }
    /* (non-Javadoc)
	 * @see org.bentoweb.amfortas.components.testing.impl.aa#getTestSuiteProperties(java.lang.String)
	 */
    public Properties getTestSuiteProperties(String testSuiteId) 
    {
        //get config from database TestSuite table
        Properties testSuiteProps = new Properties();    
        Iterator iter = this.datasourceAdapter.select_Testsuite_where_ActiveNId(Integer.parseInt(testSuiteId)).iterator(); 
        if(iter.hasNext())
        {
            TestSuite ts = (TestSuite) iter.next();
            testSuiteProps.put(Constants4Mapping.TS_PROP_TEST_SUITE,ts);
        }
        return testSuiteProps;
    }
    
    private TCDLTestSuite loadTestCases(TCDLTestSuite testSuite) throws IOException, ParserConfigurationException 
    {      
        // load the index file
        TestSuite ts = (TestSuite)testSuite.getProps().get(Constants4Mapping.TS_PROP_TEST_SUITE);
        String indexFileUri = ts.getIndexFileUri();
        String metadataUri = ts.getMetadataFilesUri();
        String status_ready = "";
        boolean tcdl_validate = false;
        try {
			status_ready = config.getParameter(Constants.TESTCASE_STATUS_READY);
			tcdl_validate = Boolean.parseBoolean(config.getParameter(Constants.TESTCASE_SCHEMA_VALIDATE));
		} catch (ParameterException e1) {
			log.warn("No config param for test case status: set to default: "+Constants.TESTCASE_STATUS_READY_DEFAULT);
			status_ready = Constants.TESTCASE_STATUS_READY_DEFAULT;
		}
		
		Vector errorMsgs = new Vector();
        ConcurrentHashMap<String,TestCase> testCasesMap = new ConcurrentHashMap<String, TestCase>();         
        ConcurrentHashMap<String,TestCaseScenario> testCaseScenarioMap = new ConcurrentHashMap<String, TestCaseScenario>();
                
        Element docRoot = loadTestSuiteIndexDoc(indexFileUri).getDocumentElement();
        System.out.println("Start loading tcdl files");
        log.info("Start loading tcdl files");
        String xpath = "//*[local-name()='directory']//*[local-name()='file']"; 
        NodeList testCaseFiles=null;
		try {
			testCaseFiles = XPathAPI.selectNodeList(docRoot, xpath);
		} catch (TransformerException e) {
			log.error("Test case index file TransformerException");
			errorMsgs.add("Test case index file TransformerException");
		}
		int scenariosCount=0;
        for(int i=0; i<testCaseFiles.getLength();i++)
        {
            NamedNodeMap fileElement = testCaseFiles.item(i).getAttributes();
            String tcdlId = fileElement.getNamedItem("name").getTextContent(); 
            long lastModified = Long.parseLong(fileElement.getNamedItem("lastModified").getTextContent());                    
            Date date=null;;
			try {
				date = Constants.dateFormat.parse(fileElement.getNamedItem("date").getTextContent());
			} catch (DOMException e2) {
				log.warn("lastModified DOMException");
			} catch (ParseException e2) {
				log.warn("lastModified ParseException");
			}
            InputSource input=null;
            Document tcdlDoc=null;
            TestCase testCase = new TCDLTestCase();
			try {
				input = resolveInputSource(metadataUri + tcdlId);
				tcdlDoc = XmlHelper.createDom(input, tcdl_validate);	            
	            testCase.setId(tcdlId);
	            testCase.setDate(date);
	            testCase.setLastModified(lastModified);                
	            testCase.setDom(tcdlDoc);                
	            if(testCase.isCandidate(status_ready))
	            {
	            	log.debug("Loading candidate test case: " + testCase.getId());	            	
	            	System.out.print("\nTest case " + testCase.getId() + " added. Scenarios: ");
	            	log.info("\nTest case " + testCase.getId() + " added. Scenarios: ");
	            	testCasesMap.put(testCase.getId(),testCase);                	
	            	NodeList scenarios = testCase.getScenarioNodeList();                		
	            	for(int k=0;k<scenarios.getLength();k++)
	                {         
	            		TestCaseScenario tcs=new TCDLTestCaseScenario();
						try {
							tcs.loadScenario(testCase, scenarios.item(k));
							testCaseScenarioMap.put(tcs.getUID(),tcs);
	                        log.debug("Test case " + tcs.getTestCaseId() + " added to candidates!");
	                        System.out.print(tcs.getScenarioId() + ", ");
	                        log.info(tcs.getScenarioId() + ", ");
	                        scenariosCount++;
						} catch (TransformerException e) {
							log.error("cannot load scenario " + testCase.getId() + " / " + tcs.getScenarioId());
						}                        
	                }
	            	testSuite.setTestCases(testCasesMap);
	                testSuite.setTestCasesScenarios(testCaseScenarioMap);                    
	            }
	            else
	            	log.debug("Ignoring (not approapriate status) : " + tcdlDoc.getDocumentElement().getAttribute("id"));				
			} catch (ServiceException e1) {
				log.error("cannot resolve tcdl file");
			} catch (SAXException e) {
				errorMsgs.add(testCase.getId());
				e.printStackTrace();
				log.error("cannot resolve tcdl file");
			}
        }
       
        try
        {
        	boolean mailwarn = Boolean.parseBoolean(config.getParameter("mailwarn"));
        	if(mailwarn && errorMsgs.size()>0)
        	{
        		mailSender.setFrom(config.getParameter("mailfrom"));
                mailSender.setTo(config.getParameter("mailto"));
                StringBuffer s = new StringBuffer();
                s.append("While starting up Amfortas the following test cases didnt validate:\n");
                Enumeration enumer = errorMsgs.elements();
                while(enumer.hasMoreElements())
                	s.append("\t"+(String)enumer.nextElement()+"\n");
                mailSender.setBody(s.toString());
                mailSender.setSubject("Amfortas Startup Warning");
                mailSender.send();
        	}
        }
        catch(Exception e)
        {
        	log.debug("mail for validation status not sent: " + e.getMessage());
        }
        
        System.out.println("tcdls done: Scenarios:"+ scenariosCount);
        log.info("tcdls done: Scenarios:"+ scenariosCount);
        return testSuite;        
    }
    
    
    private Document loadTestSuiteIndexDoc(String indexFileUri) throws ParserConfigurationException, IOException
    {
    	 // load index file                   
        InputSource is = null;
        try {
            is = resolveInputSource(indexFileUri);
        } catch (ServiceException e1) {
            log.warn("Error while getting test cases index file: " + e1.getMessage());
        } catch (MalformedURLException e1) {
            log.warn("Error while getting test cases index file: " + e1.getMessage());
        } catch (IOException e1) {
            log.warn("Error while getting test cases index file: " + e1.getMessage());
        } 
        Document doc=null;
		try {
			doc = XmlHelper.createDom(is, true);
			System.out.println("TestSuite index file loaded successfully");
		} catch (SAXException e1) {
			log.error("Error parsing test case index file");
		}
		return doc;
    }

    /* (non-Javadoc)
	 * @see org.bentoweb.amfortas.components.testing.impl.aa#resolveInputSource(java.lang.String)
	 */
    public InputSource resolveInputSource(String uri) throws ServiceException, MalformedURLException, IOException
    {
        Source source = null;
        SourceResolver sourceResolver = null;
        InputSource inputSource = null;
        sourceResolver = (SourceResolver)manager.lookup(SourceResolver.ROLE);            
        source = sourceResolver.resolveURI(uri);
        manager.release(sourceResolver);
        inputSource = new InputSource(source.getInputStream());
        return inputSource;
    }      
    /* (non-Javadoc)
	 * @see org.bentoweb.amfortas.components.testing.impl.aa#resolveUri(java.lang.String)
	 */
    public String resolveUri(String uri) throws ServiceException, MalformedURLException, IOException
    {
        Source source = null;
        SourceResolver sourceResolver = null;
        sourceResolver = (SourceResolver)manager.lookup(SourceResolver.ROLE);            
        source = sourceResolver.resolveURI(uri);
        return source.getURI().substring(source.getScheme().length()+2);
    }
    
    
	/* (non-Javadoc)
	 * @see org.bentoweb.amfortas.components.testing.impl.aa#parameterize(org.apache.avalon.framework.parameters.Parameters)
	 */
	public void parameterize(Parameters params) throws ParameterException {
		config = params;
		//check params
		String tmp=null;
        try {
			tmp = config.getParameter(Constants.TESTCASE_STATUS_READY);
		} catch (ParameterException e1) {
			log.warn("No config param for test case status: set to default: "+Constants.TESTCASE_STATUS_READY_DEFAULT);
			tmp = Constants.TESTCASE_STATUS_READY_DEFAULT;
			config.setParameter(Constants.TESTCASE_STATUS_READY, tmp);
		}
		tmp=null;
	}
    /* (non-Javadoc)
	 * @see org.bentoweb.amfortas.components.testing.impl.aa#contextualize(org.apache.avalon.framework.context.Context)
	 */
    public void contextualize(Context context) throws ContextException 
    {
       
    }    
	/* (non-Javadoc)
	 * @see org.bentoweb.amfortas.components.testing.impl.aa#service(org.apache.avalon.framework.service.ServiceManager)
	 */
	public void service(ServiceManager manager) throws ServiceException {
		this.manager = manager;
	}
	
	/* (non-Javadoc)
	 * @see org.bentoweb.amfortas.components.testing.impl.aa#initialize()
	 */
	public void initialize() throws Exception 
	{
	    try {
	    	datasourceAdapter = (DatasourceAdapter)this.manager.lookup(DatasourceAdapter.ROLE);
			mailSender = (MailSender)this.manager.lookup(MailSender.ROLE);
		} catch (ServiceException ex) {
			log.debug(ex.getMessage());
		} finally {
			this.manager.release(datasourceAdapter);
		}	
	}      
    /* (non-Javadoc)
	 * @see org.bentoweb.amfortas.components.testing.impl.aa#dispose()
	 */
    public void dispose() 
    {
    } 
}
