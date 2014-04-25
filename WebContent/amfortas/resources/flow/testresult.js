cocoon.load("resources/flow/general_helperfunctions.js");
cocoon.load("resources/flow/testingprofiles.js");

importClass (org.hibernate.Criteria);
importClass (org.hibernate.FetchMode);
importClass (java.lang.Integer);
importClass (org.hibernate.criterion.Restrictions);
importClass (Packages.org.bentoweb.amfortas.hibernate.om.Testresult);
importClass (Packages.org.bentoweb.amfortas.hibernate.om.Yesnoquestion);

var userSearch = null;

function testresults() {

	openHibernateSession();
	var viewData = new Object();
	viewData.users=null;
	viewData.result=null;
	var testSuiteList = new ArrayList();
	testSuiteList = get_active_testsuites();
	viewData.testSuiteList = testSuiteList;
	//
	if (cocoon.request.getParameter("subUser")!=null) {
		var searchcrit = cocoon.request.getParameter("searchcrit");
		var searchtxt = cocoon.request.getParameter("searchtxt");
		
		var query = "from org.bentoweb.amfortas.hibernate.om.TestProfile as tp ";
		if (searchtxt != "") {		
			if (searchcrit=="1")
				query += " where tp.user.nameLast like '%"+searchtxt+"%'";
			if (searchcrit=="2")
				query += " where tp.user.nameFirst like '%"+searchtxt+"%'";
			if (searchcrit=="3")
				query += " where tp.user.email like '%"+searchtxt+"%'";
		}
		var userArr = hs.find(query);
		if (userArr.size()>0) {
			viewData.users = userArr;
		}
	}
	//
	if (cocoon.request.getParameter("subResult")!=null) {
		var userArr = cocoon.request.getParameter("tProfileId");
		var guideline = cocoon.request.getParameter("guideline");
		var testsuite_Id = new Integer(parseInt(cocoon.request.getParameter("testsuite")));
		//fetch test from database
		var crit = hs.createCriteria(Testresult);
		var result = crit
						.setFetchMode("Yesnoquestion", FetchMode.JOIN)
						.setFetchMode("Likertscale", FetchMode.JOIN)
						.setFetchMode("Multiplechoice", FetchMode.JOIN)
						.setFetchMode("Openquestion", FetchMode.JOIN)
						.setFetchMode("Yesnoopenquestion", FetchMode.JOIN);
		crit.add( Restrictions.eq("testSuite.testSuiteId", new Integer(testsuite_Id)));
		//add user restriction criteria
		if ((userArr!=null) && (userArr.length>0)) { 
			/*crit.add( Restrictions.or(
				Restrictions.eq("testProfile.testProfileId", new Integer(userArr[0]))
				for (var i=1; i<userArr.length;i++) {		
					, Restrictions.eq("testProfile.testProfileId", new Integer(userArr[i]))
				}
				);
			*/
			crit.add( Restrictions.eq("testProfile.testProfileId", new Integer(userArr)));
		}
		
		//add guideline restriction
		if (guideline!="all") {
			crit.add( Restrictions.like("testcaseId", ("%_"+guideline+"_%")));
		}
		//add test suite restriction
	    var result = crit.list(); 
	    
	    // fetch test suite
	    var tsPool = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.testing.TestSuitesPoolable.ROLE);
   		var testSuite = tsPool.getTestSuite(testsuite_Id); 
   		var testcaseScenarios = testSuite.getTestCasesScenarios(); 
   		//FIXME: make this taking results only for db: no need to load test cases.
						
		var extractedResult = new Array();
		//java.lang.System.err.println("result.size() "+ result.size());
		for(var i=0; i<result.size();i++) 
		{
			var viewItem = new Array();
			var testprofile = result.get(i).getTestProfile();
			var userprofile = result.get(i).getUserProfile();
			var testcaseId = result.get(i).getTestcaseId();
			var scenarioId = result.get(i).getScenarioId();
			var date = result.get(i).getDate();
			
			//
			var answer = null;
			viewItem["UID"]= testprofile.getUser().getUserId();
			viewItem["TpID"]= testprofile.getTestProfileId();
			viewItem["age"]= userprofile.getAgeRange().getNameDefault();
			viewItem["sex"]= testprofile.getUser().getSex().getNameDefault();
			viewItem["testcaseid"]= testcaseId;
			viewItem["scenarioid"]= scenarioId;
			viewItem["date"] = date;
			//java.lang.System.err.println("test::::::::::: "+testcaseId+"\t\t"+scenarioId);
			// fetch testcase scenario
			var tcscenario = testcaseScenarios.get(testcaseId+"\t\t"+scenarioId);
			var answerTCDL = null;
			try
			{	
				answerTCDL = tcscenario.getAnswer();
			}
			catch(e)
			{
				//java.lang.System.err.println("error get answer object for "+testcaseId+"\t\t"+scenarioId+ ". Probably a rejected test case(??)");
				cocoon.log.error("error get answer object for "+testcaseId+"\t\t"+scenarioId+ ". Probably a rejected test case(??)");
			}
			if(answerTCDL==null)
				continue;
			try
			{
				//yesnoquestion
				answer = result.get(i).getYesnoquestions().iterator();
				if (answer.hasNext()) {
					viewItem["type"]="YesNo";
					var it = answer.next();
					viewItem["code"]= it.getChoice();
					viewItem["answer"]= answerTCDL.getAnswer(it.getChoice());
					viewItem["text"]="";
					viewItem["expresult"]=answerTCDL.getExpResult();
				}
				//likertscale
				answer = result.get(i).getLikertscales().iterator();
				if (answer.hasNext()) {
					viewItem["type"]="Likert";
					var it = answer.next();
					viewItem["code"]= it.getLevel();
					viewItem["answer"]= it.getLevel();
					viewItem["text"]="";
					viewItem["expresult"]=answerTCDL.getExpResult();
				}
				//multiplechoice
				answer = result.get(i).getMultiplechoices().iterator();
				if (answer.hasNext()) {
					var tcdltxt = "";
					viewItem["type"] ="Multiple";
					var code = null;
					var atext = null;
					//
					var it = answer.next();
					code = it.getLevel();
					atext = answerTCDL.getAnswer(code);
					while (answer.hasNext()) {
						var it = answer.next();
						code += "/"+it.getLevel();
						atext += "/"+answerTCDL.getAnswer(code);
					}
					viewItem["code"]=code;
					viewItem["answer"]=atext;
					viewItem["text"]="";
					viewItem["expresult"]=answerTCDL.getExpResult();
				}
				//openended
				answer = result.get(i).getOpenquestions().iterator();
				if (answer.hasNext()) {
					viewItem["type"]="Open";
					var it = answer.next();
					viewItem["code"]= "";
					viewItem["answer"]= "";
					viewItem["text"]=it.getText();
					viewItem["expresult"]=answerTCDL.getExpResult();
				}
				//openendedyesno
				answer = result.get(i).getYesnoopenquestions().iterator();
				if (answer.hasNext()) {
					viewItem["type"]="YesNoOpen";
					var it = answer.next();
					viewItem["code"]= it.getChoice();
					var ansStr = viewItem["answer"]= answerTCDL.getAnswer(it.getChoice());
					var tcdlTxt = answerTCDL.getOptionother();
					if (tcdlTxt!="")
						ansStr = ansStr + " (" +tcdlTxt+")";
					viewItem["answer"]= ansStr;
					viewItem["text"]=it.getText();
					viewItem["expresult"]=answerTCDL.getExpResult();
				}
				//
				//
				//
				// disability
				var disObj = hs.find("from org.bentoweb.amfortas.hibernate.om.UserProfileHasDisability as uphd where uphd.userProfile.userProfileId='"+userprofile.getUserProfileId()+"'"); 
				var disability="";
				for (var j=0; j<disObj.size();j++) {
					if (j>0) {
						disability = disability+", ";
					}
					disability=disability+disObj.get(j).getDisability().getNameDefault();
				}
				viewItem["disability"] = disability;
				//
				// extract UA
				//
				var useragent="";
				var useragent_types="";
				var atexists=false;
				var uaObj = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as ua where ua.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and (ua.userAgent.nameKey!='amformas.recruit.ans.user_agent.null')");
				for (var m=0; m<uaObj.size();m++) {
					if (m>0) {
						atexists=true;
						useragent = useragent+", ";
						useragent_types = useragent_types+", ";
					}
					var version="";
					if (uaObj.get(m).getVersion()!="")
						version=" ("+uaObj.get(m).getVersion()+")";
					useragent = useragent + uaObj.get(m).getUserAgent().getNameDefault()+version;
					useragent_types = useragent_types + uaObj.get(m).getUserAgent().getUserAgentTypes().getNameDefault();
				}
				var uaObjOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as ua where ua.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and (ua.userAgent.nameKey='amformas.recruit.ans.user_agent.null')");
				for (var n=0; n<uaObjOther.size();n++) {
					if (j>0 || atexists) {
						useragent = useragent+", ";
						useragent_types = useragent_types+", ";
					}
					useragent = useragent + uaObjOther.get(n).getBrowserOther();
					useragent_types = useragent_types + "other";
				}
				viewItem["ua_types"]= useragent_types;
				viewItem["ua"]= useragent;	
				//	
				// AT
				//
				var assistive_technology="";
				var assistive_technology_types="";
				var atObj = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as at where at.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and at.assistiveTechnology.nameKey!='amformas.recruit.ans.assistive_technology.null' and (at.assistiveTechnology.assistiveTechnologyId < '52'))");
				atexists=false;
				for (var j=0; j<atObj.size();j++) {
					if (j>0) {
						atexists=true;
						assistive_technology = assistive_technology+", ";
						assistive_technology_types = assistive_technology_types+", ";
					}
					var version="";
					if (atObj.get(j).getVersion()!="")
						version=atObj.get(j).getVersion()+", ";
					assistive_technology = assistive_technology + atObj.get(j).getAssistiveTechnology().getNameDefault()+" ("+version+atObj.get(j).getAtExperience().getNameDefault()+")";
					assistive_technology_types = assistive_technology_types + atObj.get(j).getAssistiveTechnology().getAssistiveTechnologyTypes().getNameDefault();
				}
				var atObjOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as at where at.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and ((at.assistiveTechnology.nameKey='amformas.recruit.ans.assistive_technology.null') or (at.assistiveTechnology.assistiveTechnologyId > '51')))");
				for (var k=0; k<atObjOther.size();k++) {
					if (j>0 || atexists) {
						assistive_technology = assistive_technology+", ";
						assistive_technology_types = assistive_technology_types+", ";
					}
					assistive_technology = assistive_technology + atObjOther.get(k).getAssistiveTechnologyOther();
					assistive_technology_types = assistive_technology_types + "other";
				}
				viewItem["at_types"]= assistive_technology_types;
				viewItem["at"]= assistive_technology;
				// device
				viewItem["device"] = testprofile.getUserUsesDevice().getDevice().getNameDefault()+" ("+testprofile.getUserUsesDevice().getAtExperience().getNameDefault()+")";
				// os
				viewItem["os"] = testprofile.getUserUsesOs().getOperatingSystem().getNameDefault();
				//
				extractedResult.push(viewItem);
			}
			catch(e1)
			{
					//java.lang.System.err.println("error get answer object for "+testcaseId+"\t\t"+scenarioId+ ". " + e1);
					cocoon.log.error("error get answer object for "+testcaseId+"\t\t"+scenarioId+ ". " + e1);			
			}
		}
		
		viewData.result = extractedResult;
		//
		/*
		*/
	}

	cocoon.sendPage("testresults-auth.html", viewData);
}