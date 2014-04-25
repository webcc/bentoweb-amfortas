cocoon.load("resources/flow/general_helperfunctions.js");
//cocoon.load("resources/flow/definitions.js");

//classes
importClass (Packages.org.apache.cocoon.forms.util.I18nMessage);
importClass (Packages.java.util.ArrayList);
importClass (org.hibernate.Transaction);
importClass (java.lang.Integer);

var TESTSUITES_ADMIN_PAGE = "admin_testsuites-auth.html";

function admin_testsuites() 
{
	openHibernateSession();
	//open transaction
	transaction = hs.beginTransaction();	
	var testSuiteList = new ArrayList();
	var viewData = new Object();
	var action = cocoon.request.getParameter("action");
	if(action!=null)
	{
		var testsuiteid = new Integer(parseInt(cocoon.request.getParameter("testsuiteid")));
		if(action == "reload")
		{
			try
			{
				reloadTestSuite(testsuiteid);
				viewData.done = "done";
			}
			catch(e)
			{
				java.lang.System.err.println("reloadTestSuite failed " + e.toString());
				cocoon.log.warn("reloadTestSuite failed");
			}
		}
	
	}
	else
	{
		testSuiteList = get_all_testsuites();
		var l = testSuiteList.size();		
		viewData.testSuiteList = testSuiteList;
		viewData.l = l;
	}
	
	
	
	//close transaction
	transaction.commit();	
	cocoon.sendPage(TESTSUITES_ADMIN_PAGE,viewData);
}


function reloadTestSuite(testsuiteid)
{
	var tsFactory = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.testing.AbstractTestSuiteFactory.ROLE);
	var testSuite = tsFactory.generate(testsuiteid);
	cocoon.releaseComponent(tsFactory);
	cocoon.log.debug("tsFactory released");
	var tsPool = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.testing.TestSuitesPoolable.ROLE);
	tsPool.putTestSuite(testSuite);
	cocoon.releaseComponent(tsPool);
	cocoon.log.debug("tsPool released");
}


function get_all_testsuites() 
{
	return hs.find("from org.bentoweb.amfortas.hibernate.om.TestSuite"); 	
}
function get_testsuite(testsuiteid) 
{
	return hs.find("from org.bentoweb.amfortas.hibernate.om.TestSuite where testSuiteId=" + testsuiteid).get(0);
}