cocoon.load("resources/flow/general_helperfunctions.js");

function admin_all_profiles() {
	//
	openHibernateSession();
	//
	var viewData = new Object();
	
	// get all active test profiles
	var testprofiles = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where (tp.isActive='1')");
	// for each test profile
	//java.lang.System.err.println("testprofiles.size() " + testprofiles.size());
	var profilesData = new Array(testprofiles.size());
	for (var i=0; i<testprofiles.size();i++) 
	{
			//getprofiledata
			var profileData = getTPprofiledata(testprofiles.get(i));
			profilesData[i] = profileData;
	}
	viewData.profilesData = profilesData;
	cocoon.sendPage("admin_users_detail_all-auth.html", viewData);
}



function getTPprofiledata(testprofile) {
	//
	openHibernateSession();
	var tp = new Object();
	tp.testprofile = testprofile;
	//+++++++ active testing profile +++++++++++
	//user agents
	tp.uaList = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as ua where ua.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and (ua.userAgent.nameKey!='amformas.recruit.ans.user_agent.null')  and (ua.userAgent.nameKey!='talking_browser_other')");
	tp.uaListOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as ua where ua.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and ( (ua.userAgent.nameKey='amformas.recruit.ans.user_agent.null') or (ua.userAgent.nameKey='talking_browser_other'))");
	//assistive technology + talking browser
	tp.atList = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as at where at.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and at.assistiveTechnology.nameKey!='amformas.recruit.ans.assistive_technology.null' and (at.assistiveTechnology.assistiveTechnologyId < '52'))");
	tp.atListOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as at where at.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and ((at.assistiveTechnology.nameKey='amformas.recruit.ans.assistive_technology.null') or (at.assistiveTechnology.assistiveTechnologyId > '51')))");

	//os
	tp.os = testprofile.getUserUsesOs();
	//
	tp.device = testprofile.getUserUsesDevice();
	//
	//ua settings
	tp.uaListSetting = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUaSetting as uas where (uas.userUsesUserAgent.testProfile.testProfileId='"+testprofile.getTestProfileId()+"') and (uas.userUsesUserAgent.testProfile.isActive='1') and (uas.userAgentSetting.nameKey!='amformas.recruit.ans.user_agent_setting_id.0')");
	tp.uaListSettingOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUaSetting as uas where (uas.userUsesUserAgent.testProfile.testProfileId='"+testprofile.getTestProfileId()+"') and (uas.userUsesUserAgent.testProfile.isActive='1') and (uas.userAgentSetting.nameKey='amformas.recruit.ans.user_agent_setting_id.0')");
	//os settings
	tp.osListSetting = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesOsSetting as oss where (oss.userUsesOs.userUsesOsId='"+testprofile.getUserUsesOs().getUserUsesOsId()+"') and (oss.operatingSystemSetting.nameKey!='amformas.recruit.ans.os_settings_id.0')");
	tp.osListSettingOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesOsSetting as oss where (oss.userUsesOs.userUsesOsId='"+testprofile.getUserUsesOs().getUserUsesOsId()+"') and (oss.operatingSystemSetting.nameKey='amformas.recruit.ans.os_settings_id.0')");
	//+++++++++ user profile ++++++++++++++
	tp.user = testprofile.getUser();
	//userprofile
	var tphup = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfileHasUserProfile as tphup where (tphup.testProfile.testProfileId='"+testprofile.getTestProfileId()+"') and (tphup.userProfile.isActive='1')").get(0);
	//FIXME: now:get(0) ... for more testprofile for every userprofile need loop
	tp.userprofile = tphup.getUserProfile();
	tp.disability = hs.find("from org.bentoweb.amfortas.hibernate.om.UserProfileHasDisability as uphd where uphd.userProfile.userProfileId='"+tphup.getUserProfile().getUserProfileId()+"'"); 
	
	tp.creditQ = hs.find("select count(*) from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and tp.finished='1' and tp.testcaseSet.testSuite.testSuiteId='"+"3"+"'").get(0);
	tp.todoQ = hs.find("select count(*) from org.bentoweb.amfortas.hibernate.om.TestcasePool as tp where tp.testcaseSet.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and tp.finished='0' and tp.testcaseSet.testSuite.testSuiteId='"+"3"+"'").get(0);
	//java.lang.System.err.println("ccc " + testprofile.getUser().getUserId());
	return tp;
}