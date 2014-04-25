cocoon.load("resources/flow/general_helperfunctions.js");
importClass (Packages.org.bentoweb.amfortas.components.om.impl.TCDLTestCase);
importClass (Packages.org.bentoweb.amfortas.hibernate.om.TestSuite);
importClass (Packages.java.lang.String);
function tcmap()
{	
	if(cocoon.request.getParameter("id")!=null)
		cocoon.sendPage("dofetchtcdl_"+cocoon.request.getParameter("id"),viewData);
	
   	var testProfileId = "10";//get the active testprofile of the user
	var testSuiteId = "2"; //get the active testsuite for the active testprofile   	
   	openHibernateSession();
   	try
   	{
		var userId = getUserId();
		var user = hs.find("from org.bentoweb.amfortas.hibernate.om.User where userId='"+userId+"'");
		if (isUser(userId)) 
		{
			var testprofile = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where (tp.user.userId='"+userId+"') and (tp.isActive='1')");
			if (testprofile.size()>0) 
			{
				var testProfileIdInt = testprofile.get(0).getTestProfileId();
				testProfileId = String.valueOf(testProfileIdInt);
			}
		}
	   	java.lang.System.err.println("testprofile: "+testProfileId);
	}
	catch (any)
	{
	}
	var viewData = new Object();
   	var tsPool = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.testing.TestSuitesPoolable.ROLE);
   	var testSuite = tsPool.getTestSuite(testSuiteId).getProps().get(Packages.org.bentoweb.amfortas.components.testing.impl.Constants4Mapping.TS_PROP_TEST_SUITE);
	cocoon.request.setAttribute("testProfileId",testProfileId);
	cocoon.request.setAttribute("testSuiteId",testSuiteId);   	
    var testCase2TestProfileMapper = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.testing.AbstractTestCaseMapper.ROLE);
    var winners = testCase2TestProfileMapper.doMap();	
	viewData.winners = winners;	
	viewData.metadataURI = testSuite.getMetadataFilesUri();	
	java.lang.System.err.println("metadataURI: "+viewData.metadataURI);
	viewData.prefURI = "dofetchtcdl_";
	viewData.testSuite = testSuite;	
	cocoon.releaseComponent(testCase2TestProfileMapper);
    cocoon.sendPage("test.html",viewData);
    
}
