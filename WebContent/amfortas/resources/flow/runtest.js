cocoon.load("resources/flow/general_helperfunctions.js");
importClass (Packages.org.bentoweb.amfortas.components.om.impl.TCDLTestCase);
importClass (Packages.org.bentoweb.amfortas.hibernate.om.TestSuite);
importClass (Packages.org.bentoweb.amfortas.hibernate.om.Testresult);
importClass (Packages.java.lang.String);
importClass (java.util.Date);

var datasourceAdapter = null;
var userProfile = null;
var testProfile = null;
var testSuite = null;

function runtest() {
	openHibernateSession();
	//initialize
	var testSuiteId = cocoon.request.getParameter("tsid");
	userProfile = getActiveUserProfile();
	testProfile = getActiveTestProfile();
	//java.lang.System.err.println("testSuiteId: "+testSuiteId);	
	// 1. search all test suites the user are allowed to test
	// if more than one, show page to select search from active test suite
	var viewData = new Object();
	var tsview = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfileHasTestSuite as tphts where (tphts.testProfile.testProfileId='"+testProfile.getTestProfileId()+"') and (tphts.testSuite.isActive='1')");
	viewData.tsview = tsview;
	if(testSuiteId==null)
	{
		switch (tsview.size()) {
			case 0:
				cocoon.sendPage("testsuites-auth.html", viewData);
				return;
			//
			case 1:
				testSuiteId = tsview.get(0).getTestSuite().getTestSuiteId();
				break;
			//
			default: //active for >1
				cocoon.sendPageAndWait("testsuites-auth.html", viewData);
		}
	}

	//java.lang.System.err.println("testSuiteId: "+testSuiteId);	
   	var tsPool = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.testing.TestSuitesPoolable.ROLE);
   	testSuite = tsPool.getTestSuite(testSuiteId).getProps().get(Packages.org.bentoweb.amfortas.components.testing.impl.Constants4Mapping.TS_PROP_TEST_SUITE);
	var metadataURI = testSuite.getMetadataFilesUri(); //maybe need resolveUri
	var testfileURI = testSuite.getTestFilesUri(); //maybe need resolveUri
	//java.lang.System.err.println("testfileURI: "+testfileURI);	
	cocoon.request.setAttribute("testProfileId",testProfile.getTestProfileId());
	cocoon.request.setAttribute("testSuiteId",testSuiteId);   
	cocoon.request.setAttribute("tsid",testSuiteId); 	
	
    var testCase2TestProfileMapper = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.testing.AbstractTestCaseMapper.ROLE);
    var winnersList = testCase2TestProfileMapper.doMap();	
	
	datasourceAdapter = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.persistence.DatasourceAdapter.ROLE);
	
	//
	var testcaseId = null;
	var scenarioId = null;
	var poolId = null;
	
	if (winnersList.size()>0)
	{
		var testCaseScenario = winnersList.get(0);
		testcaseId = testCaseScenario.getTestCaseId();
		scenarioId = testCaseScenario.getScenarioId();
		poolId = testCaseScenario.getPoolId();
		datasourceAdapter.setTestCaseStarted(poolId);
		
		// questions counter
		var testcaseSetId = parseInt(hs.find("select testcaseSet.testcaseSetId from org.bentoweb.amfortas.hibernate.om.TestcasePool as tcp where tcp.testcaseSet.done='0' and tcp.testcaseSet.testProfile.testProfileId='" + testProfile.getTestProfileId() + "'").get(0));
		var done = parseInt(hs.find("select count(*) from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testcaseSetId='"+testcaseSetId+"' and tp.finished='1'").get(0));
		var all = done + winnersList.size(); 
		var q_counter = (done+1) + "/" + all;
		//java.lang.System.err.println("q_counter"+q_counter );

		setTestcaseStart(testcaseId, scenarioId)

		cocoon.sendPage(("testrun_"+testcaseId), {
			"q_counter" : q_counter, 
			"testcaseID" : testcaseId,
			"scenarioID" : scenarioId,
			"metadataURI" : metadataURI,
			"testfileURI" : testfileURI,
			"testsuiteID" : testSuiteId
		});

		//storing now is done in another page
		//datasourceAdapter.setTestCaseFinished(poolId);	
			
	} else {
	try
	{	
		//get user email
		var mailer = cocoon.getComponent(Packages.org.apache.cocoon.mail.MailSender.ROLE);
		mailer.setFrom("amfortas@bentoweb.org");
		mailer.setTo("amfortas@bentoweb.org");
		mailer.setBody("There are no more tests for user("+ testProfile.user.email+") with test profile id " + testProfile.testProfileId);
		mailer.setSubject("Amfortas: a test profile finished testing");
		mailer.send();
		cocoon.releaseComponent(mailer); 
		cocoon.log.debug("Mail when finish test!");
		//java.lang.System.err.println("mail sent ");
	}
	catch(e)
	{
			cocoon.releaseComponent(mailer);
			//java.lang.System.err.println("error sending mail when a user finish tests");
			cocoon.log.error("error sending mail when a user finish tests");
	}		
		//no test case available
		cocoon.sendPage("info-auth.html", {
			"titleKey":"amformas.auth.info.notc.title",
			"msgKey":"amformas.auth.info.notc.text"
		});
	}
}

function setTestcaseStart(testcaseId, scenarioId) {
	var tcPool = hs.find("from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseId='"+testcaseId+"' and tp.scenarioId='"+scenarioId+"' and tp.testcaseSet.testSuite.testSuiteId='"+testSuite.getTestSuiteId()+"' and tp.testcaseSet.testProfile.testProfileId='"+testProfile.getTestProfileId()+"'");
	if (tcPool.size()>0) {
		var tc = tcPool.get(0);
		tc.setDateStart(new Date());
		hs.update(tc);
	}
}