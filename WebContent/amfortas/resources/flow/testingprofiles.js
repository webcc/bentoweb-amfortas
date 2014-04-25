cocoon.load("resources/flow/general_helperfunctions.js");



function selecttestprofile() {
	var viewData = new Object();
	var datasourceAdapter = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.persistence.DatasourceAdapter.ROLE);	
	var userId = getUserId();
	var action = cocoon.request.getParameter("action");
	var tpId = cocoon.request.getParameter("tpid");	
	// actions: delete, clone, activate, show, edit
	//if(action=="clone")
		//datasourceAdapter.cloneTestProfile(tpId);
	if(action=="delete")
		datasourceAdapter.deleteTestProfile(datasourceAdapter.getTestProfile(tpId));
	if (action=="activate") 
		datasourceAdapter.activateTestProfile(datasourceAdapter.getTestProfile(tpId));
	var testprofile = datasourceAdapter.getActiveTestProfile(userId);
	
	viewData = getprofiledata(testprofile);	
	viewData.profilesList = datasourceAdapter.getTestProfiles(userId);
	viewData.datasourceAdapter = datasourceAdapter;
	//cocoon.releaseComponent(datasourceAdapter); // this is used in selectprofile.xml page 
	cocoon.sendPage("selectprofile-auth.html", viewData);
}

function showactiveprofile() {
	//
	openHibernateSession();
	var userId = getUserId();
	var viewData = new Object();
	var user = hs.find("from org.bentoweb.amfortas.hibernate.om.User where userId='"+userId+"'");
	viewData.username = user.get(0).getNameFirst()+" "+user.get(0).getNameLast();
	viewData.hastp = false;
	
	if (isUser(userId)) {
		//
		var testprofile = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where (tp.user.userId='"+userId+"') and (tp.isActive='1')");
		//user agents
		if (testprofile.size()>0) {
			viewData.hastp = true;
			viewData.uaList = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as ua where ua.testProfile.testProfileId='"+testprofile.get(0).getTestProfileId()+"' and (ua.userAgent.nameKey!='amformas.recruit.ans.user_agent.null')  and (ua.userAgent.nameKey!='talking_browser_other')");
			viewData.uaListOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as ua where ua.testProfile.testProfileId='"+testprofile.get(0).getTestProfileId()+"' and ( (ua.userAgent.nameKey='amformas.recruit.ans.user_agent.null') or (ua.userAgent.nameKey='talking_browser_other'))");
			//assistive technology + talking browser
			//viewData.atList = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as at where at.testProfile.testProfileId='"+testprofile.get(0).getTestProfileId()+"' and (at.assistiveTechnology.nameKey!='amformas.recruit.ans.assistive_technology.null')");
			viewData.atList = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as at where at.testProfile.testProfileId='"+testprofile.get(0).getTestProfileId()+"' and at.assistiveTechnology.nameKey!='amformas.recruit.ans.assistive_technology.null' and (at.assistiveTechnology.assistiveTechnologyId < '52'))");
			//viewData.atListOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as at where at.testProfile.testProfileId='"+testprofile.get(0).getTestProfileId()+"' and (at.assistiveTechnology.nameKey='amformas.recruit.ans.assistive_technology.null')");
			viewData.atListOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as at where at.testProfile.testProfileId='"+testprofile.get(0).getTestProfileId()+"' and ((at.assistiveTechnology.nameKey='amformas.recruit.ans.assistive_technology.null') or (at.assistiveTechnology.assistiveTechnologyId > '51')))");
			//
			var uudevice = testprofile.get(0).getUserUsesDevice();
			if (uudevice.getDevice().getNameKey()!='amformas.recruit.ans.common.other') {
				viewData.device = uudevice.getDevice().getNameKey();
			} else {
				viewData.deviceOther = uudevice.getDeviceOther();
			}
		}
	}
	cocoon.sendPage("welcome-auth.html", viewData);
}


function getprofiledata(testprofile) {
	//
	openHibernateSession();
	var viewData = new Object();
	//+++++++ active testing profile +++++++++++
	//user agents
	viewData.uaList = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as ua where ua.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and (ua.userAgent.nameKey!='amformas.recruit.ans.user_agent.null')  and (ua.userAgent.nameKey!='talking_browser_other')");
	viewData.uaListOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent as ua where ua.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and ( (ua.userAgent.nameKey='amformas.recruit.ans.user_agent.null') or (ua.userAgent.nameKey='talking_browser_other'))");

	//java.lang.System.err.println("uaListOther"+viewData.uaListOther.size() );
	
	//assistive technology
	viewData.atList = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as at where at.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and at.assistiveTechnology.nameKey!='amformas.recruit.ans.assistive_technology.null' and (at.assistiveTechnology.assistiveTechnologyId < '52'))");
	viewData.atListOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology as at where at.testProfile.testProfileId='"+testprofile.getTestProfileId()+"' and ((at.assistiveTechnology.nameKey='amformas.recruit.ans.assistive_technology.null') or (at.assistiveTechnology.assistiveTechnologyId > '51')))");
	//os
	viewData.os = testprofile.getUserUsesOs();
	//
	viewData.device = testprofile.getUserUsesDevice();
	//
	//ua settings
	viewData.uaListSetting = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUaSetting as uas where (uas.userUsesUserAgent.testProfile.testProfileId='"+testprofile.getTestProfileId()+"') and (uas.userUsesUserAgent.testProfile.isActive='1') and (uas.userAgentSetting.nameKey!='amformas.recruit.ans.user_agent_setting_id.0')");
	viewData.uaListSettingOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesUaSetting as uas where (uas.userUsesUserAgent.testProfile.testProfileId='"+testprofile.getTestProfileId()+"') and (uas.userUsesUserAgent.testProfile.isActive='1') and (uas.userAgentSetting.nameKey='amformas.recruit.ans.user_agent_setting_id.0')");
	//os settings
	viewData.osListSetting = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesOsSetting as oss where (oss.userUsesOs.userUsesOsId='"+testprofile.getUserUsesOs().getUserUsesOsId()+"') and (oss.operatingSystemSetting.nameKey!='amformas.recruit.ans.os_settings_id.0')");
	viewData.osListSettingOther = hs.find("from org.bentoweb.amfortas.hibernate.om.UserUsesOsSetting as oss where (oss.userUsesOs.userUsesOsId='"+testprofile.getUserUsesOs().getUserUsesOsId()+"') and (oss.operatingSystemSetting.nameKey='amformas.recruit.ans.os_settings_id.0')");
	//+++++++++ user profile ++++++++++++++
	viewData.user = testprofile.getUser();
	//userprofile
	var tphup = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfileHasUserProfile as tphup where (tphup.testProfile.testProfileId='"+testprofile.getTestProfileId()+"') and (tphup.userProfile.isActive='1')");
	viewData.userprofile = tphup.get(0).getUserProfile();
	viewData.disability = hs.find("from org.bentoweb.amfortas.hibernate.om.UserProfileHasDisability as uphd where uphd.userProfile.userProfileId='"+tphup.get(0).getUserProfile().getUserProfileId()+"'"); 
	return viewData;
}