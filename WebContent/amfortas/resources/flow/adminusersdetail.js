cocoon.load("resources/flow/general_helperfunctions.js");
cocoon.load("resources/flow/testingprofiles.js");
/*
	admin users detail
*/

//request parameter
var USERID = "userid";
var PROFILEID = "tpid";
var PAGE = "admin_users_detail-auth.html";
var ACTION = "action";
var ACTIONADDTS = "subaddts";
var TSID = "tsid";
var back=null;


function adminusersdetail() {
	//
	openHibernateSession();
	//
	var userId = cocoon.request.getParameter(USERID);
	var profileId = cocoon.request.getParameter(PROFILEID);
	var action = cocoon.request.getParameter(ACTION);
	var actionaddts = cocoon.request.getParameter(ACTIONADDTS);
	var tsId = cocoon.request.getParameter(TSID);
	//
	var viewData = new Object();
	var datasourceAdapter = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.persistence.DatasourceAdapter.ROLE);
	
	
	var user = 	hs.find("from org.bentoweb.amfortas.hibernate.om.User as us where us.userId='"+userId+"'");
	if (user.size()>0) {
	
		user = user.get(0);

		//check GET action
		if (action!=null) {
			if (action=="verify")
				setStatus(user, "1");
			if (action=="activate")
				setStatus(user, "2");
			if (action=="deactivate")
				setStatus(user, "1");
		}
	
		var testprofile = null;
		if (profileId==null) {
			testprofile = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where (tp.user.userId='"+userId+"') and (tp.isActive='1')").get(0);
		} else {
			testprofile = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where tp.testProfileId='"+profileId+"'").get(0);
		}
			//check POST action
			if (actionaddts) {
				addtestsuite(testprofile, tsId);
			}
			if (action=="delts")
				deletetestsuite(testprofile, tsId);	
				
		viewData = getprofiledata(testprofile);
		viewData.tsuser = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfileHasTestSuite as tphts where tphts.testProfile.testProfileId='"+testprofile.getTestProfileId()+"'");
		viewData.user = user;
		//all testprofiles
		viewData.testprofileall = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where tp.user.userId='"+userId+"'");
		viewData.testprofile = testprofile;
		viewData.testsuites = hs.find("from org.bentoweb.amfortas.hibernate.om.TestSuite as ts where ts.isActive='1'");
		
		//links
		back = userId;
		viewData.previous = String(parseInt(userId,0) - 1);
		viewData.next = String(parseInt(userId,0) + 1);	
		viewData.userexists = "true";
		
		//show credit
		//var testSuiteList = get_active_testsuites();		
		//viewData.testSuiteList = testSuiteList;
		//FIXME: for all active testsuite/testprofile --- hard coded now to 2
		
		//var creditList = hs.find("from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testProfile.user.userId='"+userId+"' and tp.finished='1' and tp.testcaseSet.testSuite.testSuiteId='"+"3"+"'");
		//viewData.creditList = creditList;
		//java.lang.System.err.println("creditList"+creditList.size() );
		//var todoList = hs.find("from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testProfile.user.userId='"+userId+"' and tp.finished='0' and tp.testcaseSet.testSuite.testSuiteId='"+"3"+"'");
		//viewData.todoList = todoList;
		
	} else {
		viewData.back = back; 
		viewData.userexists = "false";
	}
	
	viewData.datasourceAdapter = datasourceAdapter;
	cocoon.sendPage(PAGE,viewData);
}

function setStatus(user, status) {
	user.setStatusUser(status);
	hs.update(user);
}

function addtestsuite(testprofile, tsId) {
	//
	var tphts = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfileHasTestSuite as tphts where (tphts.testProfile.testProfileId='"+testprofile.getTestProfileId()+"') and (tphts.testSuite.testSuiteId='"+tsId+"')");
	if (tphts.size()>0)
		return
	
	var testsuite = hs.find("from org.bentoweb.amfortas.hibernate.om.TestSuite as ts where ts.testSuiteId='"+tsId+"'");
	var tphtsId = new Packages.org.bentoweb.amfortas.hibernate.om.TestProfileHasTestSuiteId();
	var tphts = new Packages.org.bentoweb.amfortas.hibernate.om.TestProfileHasTestSuite();
	
	tphtsId.setTestProfileId(testprofile.getTestProfileId());
	tphtsId.setTestSuiteId(testsuite.get(0).getTestSuiteId());
	
	tphts.setId(tphtsId);
	tphts.setTestProfile(testprofile);
	tphts.setTestSuite(testsuite.get(0));
	hs.save(tphts);
	hs.flush();
}

function deletetestsuite(testprofile, tsId) {
	var tphts = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfileHasTestSuite as tphts where (tphts.testProfile.testProfileId='"+testprofile.getTestProfileId()+"') and (tphts.testSuite.testSuiteId='"+tsId+"')");
	if (tphts.size()>0)
		hs.createQuery("delete from org.bentoweb.amfortas.hibernate.om.TestProfileHasTestSuite as tphts where (tphts.testProfile.testProfileId='"+testprofile.getTestProfileId()+"') and (tphts.testSuite.testSuiteId='"+tsId+"')").executeUpdate();
}