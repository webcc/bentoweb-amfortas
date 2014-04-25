cocoon.load("resources/flow/general_helperfunctions.js");
importClass (Packages.org.bentoweb.amfortas.hibernate.om.User);
var testSuite = null;
var userProfile = null;
var testProfile = null;
var datasourceAdapter = null;
	
function storetest() {
	openHibernateSession();
	datasourceAdapter = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.persistence.DatasourceAdapter.ROLE);
	
	var testcaseId = cocoon.request.getParameter("testcaseId");
	var scenarioId = cocoon.request.getParameter("scenarioId");
	testSuite = getTestSuite(cocoon.request.getParameter("testsuiteId"));
	testProfile = getActiveTestProfile();
	userProfile = getActiveUserProfile(); 
	if(cocoon.request.getParameter("dontknow")!=null)
	{
		var dontknow_reason = cocoon.request.getParameter("dontknow_reason");
		try
		{	
			//get user email
			var theuserId = getTheUserId();
			java.lang.System.err.println("userid" + theuserId);	
			var theuser =  getUser(theuserId); 
			//java.lang.System.err.println(theuser.getUserId());	
			//java.lang.System.err.println(theuser.getEmail());	
			//java.lang.System.err.println(theuser.getNameFirst());	
			//java.lang.System.err.println(theuser.getNameLast());	
			//java.lang.System.err.println(theuser.getSex().getNameDefault());	
			//java.lang.System.err.println(getAge(theuserId));	

			var body="UserId: "+theuser.getUserId();
			body = body +"\n" + "Email: "+ theuser.getEmail() ;
			body = body +"\n" + "NameFirst: "+ theuser.getNameFirst() ;
			body = body +"\n" + "NameLast: "+ theuser.getNameLast() ;
			body = body +"\n" + "Gender: "+ theuser.getSex().getNameDefault();
			body = body +"\n" + "Age range: "+ getAge(theuserId) ;
			body = body +"\n" + "Testcase URL: http://www.bentoweb.org/ts/XHTML1_TestSuite2/metadata/"+testcaseId;;
			body = body +"\n" + "ScenarioId: "+scenarioId;
			body = body +"\n" + "DONT_KNOW Reason: "+ dontknow_reason;
			//send mail to admin
			var mailer = cocoon.getComponent(Packages.org.apache.cocoon.mail.MailSender.ROLE);
			mailer.setFrom("amfortas@bentoweb.org");//amfortas@bentoweb.org
			mailer.setTo("amfortas@bentoweb.org"); 
			mailer.setBody(body);
			mailer.setSubject("Amfortas - Test Case Dont know reply");
			mailer.send();
			cocoon.releaseComponent(mailer);
			cocoon.log.debug("Mail when a user replies dont know to test case question!");

		}
		catch(e)
		{
			cocoon.releaseComponent(mailer); 
			//java.lang.System.err.println("error sending mail when a user selects other disability"+ e.toString());
			cocoon.log.error("error sending mail when a user replies dont know to test case question!" + e.toString());
		}
		dontknowTestcase(testcaseId, scenarioId);//delete from pool
		cocoon.sendPage("start-test");
		return;
	}
	/*
	if ((isTcStoredDb(testcaseId, scenarioId)) && isOpenTc()) {
		//send test already done
		cocoon.sendPage("start-test");
		return;
	}
	*/
	
	//check answer type
	var answertype = cocoon.request.getParameter("questType");	
	if (answertype=='yesnoQuestion')
		storeyesnoQuestion(testcaseId, scenarioId);
	if (answertype=='likertScale')
		storelikertScale(testcaseId, scenarioId);
	if (answertype=='multipleChoice')
		storemultipleChoice(testcaseId, scenarioId);
	if (answertype=='openQuestion')
		storeopenQuestion(testcaseId, scenarioId);
	if (answertype=='yesnoOpenQuestion')
		storeyesnoOpenQuestion(testcaseId, scenarioId);
	//hs.flush();
	//java.lang.System.err.println("fgggggg " + isOpenTc());
	//check if was the last open for this session
	if (isOpenTc()) {
		cocoon.sendPage("start-test");
	} else {
		closeTcSet();
		cocoon.sendPage("runtest-finished-auth.html");
	}	
} 

function storeyesnoQuestion(testcaseId, scenarioId) {
	//fetch results before calling openHibernateSession()
	var choice = cocoon.request.getParameter("yesOrNo");
	if (choice != null) {
		var testresult = storetestresult(testcaseId, scenarioId);
		var yesno = new Packages.org.bentoweb.amfortas.hibernate.om.Yesnoquestion();
		yesno.setChoice(parseInt(choice));
		yesno.setTestresult(testresult);
		hs.save(yesno);
		//log result
		storeHTTPHeader(testresult);
		//set testcase done
		setTestcaseDone(testcaseId, scenarioId);
	}
}
function getTheUserId() {
	//get user_id from co-warp
	var appMan = cocoon.getComponent("org.osoco.cowarp.ApplicationManager");
	var userApp = appMan.login("AmfortasApp", null); //if already logged-in, User object is returned
	cocoon.releaseComponent(appMan);
	return userApp.getId();
}
function storelikertScale(testcaseId, scenarioId) {
	var choice = cocoon.request.getParameter("likertScale");
	if (choice != null) {
		var testresult = storetestresult(testcaseId, scenarioId);
		var likert = new Packages.org.bentoweb.amfortas.hibernate.om.Likertscale;
		likert.setLevel(parseInt(choice));
		likert.setTestresult(testresult);
		hs.save(likert);
		//log result
		storeHTTPHeader(testresult);
		//set testcase done
		setTestcaseDone(testcaseId, scenarioId);
	}
}

function storemultipleChoice(testcaseId, scenarioId) {
	var choice = cocoon.request.getParameterValues("multiplechoice");
	if (choice != null) {
		var testresult = storetestresult(testcaseId, scenarioId);
		for (var i=0; i<choice.length;i++) {
			var multiple = new Packages.org.bentoweb.amfortas.hibernate.om.Multiplechoice();
			multiple.setLevel(parseInt(choice[i]));
			multiple.setTestresult(testresult);
			hs.save(multiple);
		}
		//log result
		storeHTTPHeader(testresult);
		//set testcase done
		setTestcaseDone(testcaseId, scenarioId);
	}
}

function storeopenQuestion(testcaseId, scenarioId) {
	var text = cocoon.request.getParameter("openTxt");
	if ((text != null) && (text!="...")) {
		var testresult = storetestresult(testcaseId, scenarioId);
		var openquestion = new Packages.org.bentoweb.amfortas.hibernate.om.Openquestion();
		openquestion.setText(text);
		openquestion.setTestresult(testresult);
		hs.save(openquestion);
		//log result
		storeHTTPHeader(testresult);
		//set testcase done
		setTestcaseDone(testcaseId, scenarioId);
	}
}

function storeyesnoOpenQuestion(testcaseId, scenarioId) {

	var choice = cocoon.request.getParameter("yesOrNo");
	if (choice != null) {
		var testresult = storetestresult(testcaseId, scenarioId);
		var yesnoopen = new Packages.org.bentoweb.amfortas.hibernate.om.Yesnoopenquestion();
		yesnoopen.setChoice(parseInt(choice));
		yesnoopen.setTestresult(testresult);
		var text = cocoon.request.getParameter("yesOrNoTxt");
		if ((text!=null) && (text!="...")) {
			yesnoopen.setText(text);
		}
		hs.save(yesnoopen);
		//java.lang.System.err.println("saved");
		//log result
		storeHTTPHeader(testresult);
		//set testcase done
		setTestcaseDone(testcaseId, scenarioId);
	}
}

function storetestresult(testcaseId, scenarioId) {
	var testresult = new Packages.org.bentoweb.amfortas.hibernate.om.Testresult();
	//
	testresult.setTestProfile(testProfile);
	testresult.setUserProfile(userProfile);
	testresult.setTestSuite(testSuite);
	//
	testresult.setTestcaseId(testcaseId);
	testresult.setScenarioId(scenarioId);
	testresult.setDate(new Date());
	hs.save(testresult);
	return testresult;
}

function setTestcaseDone(testcaseId, scenarioId) {
	var tcPool = hs.find("from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseId='"+testcaseId+"' and tp.scenarioId='"+scenarioId+"' and tp.testcaseSet.testSuite.testSuiteId='"+testSuite.getTestSuiteId()+"' and tp.testcaseSet.testProfile.testProfileId='"+testProfile.getTestProfileId()+"'");
	datasourceAdapter.setTestCaseFinished(tcPool.get(0).getTestcasePoolId().intValue());
}


function storeHTTPHeader(testresult) {
	var httpHeader = getHTTPHeader();
	for (var key in httpHeader) {
		var header = new Packages.org.bentoweb.amfortas.hibernate.om.HeaderLog();
		header.setTestresult(testresult);
		header.setHeaderKey(key);
		header.setHeaderValue(httpHeader[key]);
		hs.save(header);
	}
}

function isTcStoredDb (testcaseId, scenarioId) {
	var tr = hs.find("from org.bentoweb.amfortas.hibernate.om.Testresult as ts where ts.testcaseId='"+testcaseId+"' and ts.scenarioId='"+scenarioId+"' and ts.testProfile.testProfileId='"+testProfile.getTestProfileId()+"' and ts.userProfile.userProfileId='"+userProfile.getUserProfileId()+"' and ts.testSuite.testSuiteId='"+testSuite.getTestSuiteId()+"'");
	if (tr.size()>0)
		return true;
	else
		return false;
}

function isOpenTc() {
	var nrArr = hs.find("select count(tp.testcasePoolId) from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testProfile.testProfileId='"+testProfile.getTestProfileId()+"' and tp.testcaseSet.testSuite.testSuiteId='"+testSuite.getTestSuiteId()+"' and tp.finished='0'");
	if (nrArr.get(0)>0)
		return true;
	else
		return false;
}

function closeTcSet() {
	var tsArr = hs.find("from org.bentoweb.amfortas.hibernate.om.TestcaseSet as ts where ts.testProfile.testProfileId='"+testProfile.getTestProfileId()+"' and  ts.testSuite.testSuiteId='"+testSuite.getTestSuiteId()+"' and ts.done='0'");
	if (tsArr.size()>0) {
		var ts = tsArr.get(0);
		ts.setDone(true);
		hs.update(ts);
	}
}

function dontknowTestcase(testcaseId, scenarioId) { 
	var tcPool = hs.find("from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseId='"+testcaseId+"' and tp.scenarioId='"+scenarioId+"' and tp.testcaseSet.testSuite.testSuiteId='"+testSuite.getTestSuiteId()+"' and tp.testcaseSet.testProfile.testProfileId='"+testProfile.getTestProfileId()+"'");
	if (tcPool.size()>0) {
		var tc = tcPool.get(0);		
		var q = "delete org.bentoweb.amfortas.hibernate.om.TestcasePool tp where tp.testcasePoolId="+tc.getTestcasePoolId();
		//java.lang.System.err.println(q);
		hs.createQuery(q).executeUpdate();
	}
}
	