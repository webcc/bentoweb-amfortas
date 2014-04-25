cocoon.load("resources/flow/general_helperfunctions.js");
cocoon.load("resources/flow/definitions.js");

importClass (java.util.Date);
importClass (java.lang.System);
importClass (Packages.org.bentoweb.amfortas.util.EncryptionService);
importClass (org.hibernate.Transaction);
importClass (Packages.org.bentoweb.amfortas.hibernate.om.Role);
importClass (Packages.org.bentoweb.amfortas.hibernate.om.UserHasRoleId);



//var username = null;
var password = null;
var mail_confirmation_hash = null;

/**
*
*/
function store_recruit(forms, forms_other, userId) {
	//
	openHibernateSession();
	
	//open transaction	
	//+++store test_profile
	//if (!mailExists(forms)) {
	
	var transaction = hs.beginTransaction();
	
	var test_profile = new Packages.org.bentoweb.amfortas.hibernate.om.TestProfile();
	test_profile.setIsActive(true);
	test_profile.setDateCreation(new Date());
	var user = null;
	if (userId==null) {
		user = store_user(forms);
	} else {
		var userArr = hs.find("from org.bentoweb.amfortas.hibernate.om.User where userId='"+userId+"'");
		user = userArr.get(0);
	}
	test_profile.setUser(user);
	test_profile.setUserUsesOs(store_user_uses_os(forms));
	test_profile.setUserUsesDevice(store_user_uses_device(forms));
	if (userId!=null) {
		var tpArr = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where tp.user.userId='"+userId+"'");
		for (var i=0; i<tpArr.size(); i++) {
			var tp = tpArr.get(i);
			tp.setIsActive(false);
			hs.update(tp);
		}
	}
	hs.save(test_profile);
	
	//+++store test_profile_has_user profile
	var user_profile = null;
	if (userId==null) {
		user_profile = store_user_profile(forms);
	} else {
		var upArr = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfileHasUserProfile as tphup where tphup.userProfile.isActive='1' and tphup.testProfile.user.userId='"+userId+"'");
		user_profile = upArr.get(0).getUserProfile();
	}
	var test_profile_has_user_profile = new Packages.org.bentoweb.amfortas.hibernate.om.TestProfileHasUserProfile();
	//
	var test_profile_has_user_profile_id = new Packages.org.bentoweb.amfortas.hibernate.om.TestProfileHasUserProfileId();
	test_profile_has_user_profile_id.setUserProfileId(user_profile.getUserProfileId());
	test_profile_has_user_profile_id.setTestProfileId(test_profile.getTestProfileId());
	//
	test_profile_has_user_profile.setUserProfile(user_profile);
	test_profile_has_user_profile.setTestProfile(test_profile);
	test_profile_has_user_profile.setId(test_profile_has_user_profile_id);
	hs.save(test_profile_has_user_profile);
	
	//+++store at and talking browser
	for (var form in forms) {
		if ((form.toString() == AT_SCREEN_READER) ||	
			(form.toString() == AT_TALKING_BROWSER) ||
			(form.toString() == AT_SCREEN_READER_AND_MAGNIFICATION) ||
			(form.toString() == AT_MAGNIFICATION_ONLY) ||
			(form.toString() == AT_SPEECH_AND_READING) ||
			(form.toString() == AT_SPEECH_RECOGNITION) ||
			(form.toString() == AT_BRAILLE) ||
			(form.toString() == AT_ALTERNATIVE_INPUT)) {
			
			//ass_technology - checkbox store every at
			var assisitve_technology_keys = forms[form].getChild("user_uses_assistive_technology-assistive_technology_id").getValue();
			if ((assisitve_technology_keys != null) && (assisitve_technology_keys!='amformas.recruit.atgeneral.other')) {
				//handle single value fields
				if (form.toString() == AT_TALKING_BROWSER) {
					hs.save(prepareUA(test_profile, forms[form], assisitve_technology_keys, false));
				} else {
					hs.save(prepareAT(test_profile, forms[form], assisitve_technology_keys, false));
				}
				/*if (form.toString() == AT_SCREEN_READER) {
					hs.save(prepareAT(test_profile, forms[form], assisitve_technology_keys, false));
				} else {
				//handle multivalue fields
					for (var j=0; j<assisitve_technology_keys.length; j++) {
						if (form.toString() == AT_TALKING_BROWSER) {
							hs.save(prepareUA(test_profile, forms[form], assisitve_technology_keys[j], false));
						} else {
							hs.save(prepareAT(test_profile, forms[form], assisitve_technology_keys[j], false));
						}
					}//end for
				}*/
			}
			
			//store ass_technology other 
			var assisitve_technology_keys_other = null;
			if (forms_other[form] != null) {
				assisitve_technology_keys_other = forms_other[form].getChild("user_uses_assistive_technology-assistive_technology_id").getValue();
				//assistive technology has been selected
				if ((assisitve_technology_keys_other != null) && (assisitve_technology_keys_other!='amformas.recruit.ans.common.none')) {
					if (form.toString() == AT_TALKING_BROWSER) {
						hs.save(prepareUA(test_profile, forms[form], assisitve_technology_keys_other , false));
					} else {
						hs.save(prepareAT(test_profile, forms[form], assisitve_technology_keys_other , false));
					}
					/*for (var j=0; j<assisitve_technology_keys_other.length; j++) {
						if (form.toString() == AT_TALKING_BROWSER) {
							hs.save(prepareUA(test_profile, forms[form], assisitve_technology_keys_other[j] , false));
						} else {
							hs.save(prepareAT(test_profile, forms[form], assisitve_technology_keys_other[j] , false));
						}
					}//end for*/
				} 
			}
			//store technology other, if nothing found in the database or nothing selected 
			var at_other = forms[form].getChild("user_uses_assistive_technology-assistive_technology_other").getValue();
			if ((at_other != null) && ((assisitve_technology_keys_other == null) || (assisitve_technology_keys_other=='amformas.recruit.ans.common.none'))) {
				if (form.toString() == AT_TALKING_BROWSER) {
					hs.save(prepareUA(test_profile, forms[form], "talking_browser_other", true));
				} else {
					if (form.toString() == AT_SCREEN_READER) {
						hs.save(prepareAT(test_profile, forms[form], "screenreader_other", true));
					}
					if (form.toString() == AT_SCREEN_READER_AND_MAGNIFICATION) {
						hs.save(prepareAT(test_profile, forms[form], "screenreader_with_magnification_other", true));
					}
					if (form.toString() == AT_MAGNIFICATION_ONLY) {
						hs.save(prepareAT(test_profile, forms[form], "magnification_software_other", true));
					}
					if (form.toString() == AT_SPEECH_AND_READING) {
						hs.save(prepareAT(test_profile, forms[form], "speech_and_hearing_support_other", true));
					}
					if (form.toString() == AT_SPEECH_RECOGNITION) {
						hs.save(prepareAT(test_profile, forms[form], "speech_recognition_other", true));
					}
					if (form.toString() == AT_BRAILLE) {
						hs.save(prepareAT(test_profile, forms[form], "braille_display_other", true));
					}
					if (form.toString() == AT_ALTERNATIVE_INPUT) {
						hs.save(prepareAT(test_profile, forms[form], "alternative_input_device_other", true));
					}
				}
			}
		}//end if
	}//end for
	
	//store AT-select - if other // bug: for other assistive technology store reference to the type!!!
	if (forms[AT_SELECT]!=null) {
		var at_tech_other = forms[AT_SELECT].getChild("assistive_technology_type_other").getValue();
		if (at_tech_other != null) {
			var user_uses_at = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology();
			var assisitve_technology_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.AssistiveTechnology where nameKey='amformas.recruit.ans.assistive_technology.null'");
			//experience
			var at_experience_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.AtExperience where nameKey='amformas.recruit.ans.common.experience_id.0'");
			//
			user_uses_at.setTestProfile(test_profile);
			user_uses_at.setAssistiveTechnology(assisitve_technology_arr.get(0));
			user_uses_at.setAtExperience(at_experience_arr.get(0));
			user_uses_at.setAssistiveTechnologyOther(at_tech_other);
			hs.save(user_uses_at);	
		}
	}
	
	//+++store user agents from settings page
	var user_uses_ua = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent();
	//
	var browser_key = forms[SETTINGS].getChild("user_uses_user_agent-user_agent_id").getValue();
	//java.lang.System.err.println(browser_key);
	// if browser (talking browser) 
	if(browser_key!=null && browser_key.length()>0)
	{
		//java.lang.System.err.println(browser_key+"sdsd");
		if (browser_key=="amformas.recruit.ans.common.other") {
			if (forms_other[SETTINGS] != null) {
				browser_key = forms_other[SETTINGS].getChild("user_uses_user_agent-user_agent_id").getValue();
				if ((browser_key == null) || (browser_key=="amformas.recruit.ans.common.none")) {
					user_uses_ua.setBrowserOther(forms[SETTINGS].getChild("user_uses_user_agent-browser_other").getValue());	
					browser_key="amformas.recruit.ans.common.other";	
				}
			} 
		} 
		user_uses_ua.setVersion(forms[SETTINGS].getChild("user_uses_user_agent-vesion").getValue());
		//search user agent
		var ua_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.UserAgent where nameKey='"+browser_key+"'");
	
		user_uses_ua.setUserAgent(ua_arr.get(0));
		if (browser_key=="amformas.recruit.ans.common.other") {
			var browser_other = forms[SETTINGS].getChild("user_uses_user_agent-browser_other").getValue();
			if (browser_other!=null)
				user_uses_ua.setBrowserOther(browser_other);
		}
	
		var at_experience_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.AtExperience where nameKey='amformas.recruit.ans.common.experience_id.0'");
		user_uses_ua.setAtExperience(at_experience_arr.get(0));
		user_uses_ua.setTestProfile(test_profile);
		var user_uses_ua_id = hs.save(user_uses_ua);
		
		//store user agent settings
		var ua_settings_keys = forms[SETTINGS].getChild("user_uses_ua_setting-user_agent_setting_id").getValue();
		for (var i=0; i<ua_settings_keys.length; i++) {
			if (ua_settings_keys[i]!='amformas.recruit.ans.common.other') {
				var user_uses_ua_setting = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesUaSetting();
				var ua_settings_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.UserAgentSetting where nameKey='"+ua_settings_keys[i]+"'");
				//id
				var user_uses_ua_setting_id  = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesUaSettingId();
				user_uses_ua_setting_id.setUserAgentSettingId(ua_settings_arr.get(0).getUserAgentSettingId());
				user_uses_ua_setting_id.setUserUsesUaId(user_uses_ua_id);
				//
				user_uses_ua_setting.setUserUsesUserAgent(user_uses_ua);
				user_uses_ua_setting.setUserAgentSetting(ua_settings_arr.get(0));
				user_uses_ua_setting.setId(user_uses_ua_setting_id);
				hs.save(user_uses_ua_setting);
			}
		}
		var us_setting_other = forms[SETTINGS].getChild("user_uses_ua_setting-ua_setting_other").getValue();
		if (us_setting_other!=null) { // setting_other
			var user_uses_ua_setting = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesUaSetting();
			var ua_settings_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.UserAgentSetting where userAgentSettingId='1'");
			//id
			var user_uses_ua_setting_id  = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesUaSettingId();
			user_uses_ua_setting_id.setUserAgentSettingId(ua_settings_arr.get(0).getUserAgentSettingId());
			user_uses_ua_setting_id.setUserUsesUaId(user_uses_ua_id);
			//
			user_uses_ua_setting.setUserUsesUserAgent(user_uses_ua);
			user_uses_ua_setting.setUserAgentSetting(ua_settings_arr.get(0));
			user_uses_ua_setting.setId(user_uses_ua_setting_id);
			user_uses_ua_setting.setSettingOther(us_setting_other);
			hs.save(user_uses_ua_setting);
		}
	}
	//close transaction
	transaction.commit();
	
	//}	
	
	return user;
}

function prepareAT(test_profile, form, key, store_other) {
	var user_uses_at = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesAssistiveTechnology();
	var assisitve_technology_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.AssistiveTechnology where nameKey='"+key+"'");
	//experience
	var at_experience_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.AtExperience where nameKey='"+form.getChild("user_uses_assistive_technology-at_experience_id").getValue()+"'");
	//
	user_uses_at.setAssistiveTechnology(assisitve_technology_arr.get(0));
	user_uses_at.setTestProfile(test_profile);
	user_uses_at.setAtExperience(at_experience_arr.get(0));
	user_uses_at.setVersion(form.getChild("user_uses_assistive_technology-version").getValue());
	if (store_other) 
		user_uses_at.setAssistiveTechnologyOther(form.getChild("user_uses_assistive_technology-assistive_technology_other").getValue());
	return user_uses_at;
}

function prepareUA(test_profile, form, key, store_other) {
	var user_uses_ua = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesUserAgent();
	var user_agent_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.UserAgent where nameKey='"+key+"'");
	//experience
	var at_experience_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.AtExperience where nameKey='"+form.getChild("user_uses_assistive_technology-at_experience_id").getValue()+"'");
	//
	user_uses_ua.setUserAgent(user_agent_arr.get(0));
	user_uses_ua.setTestProfile(test_profile);
	user_uses_ua.setAtExperience(at_experience_arr.get(0));
	user_uses_ua.setVersion(form.getChild("user_uses_assistive_technology-version").getValue());
	if (store_other) 
		user_uses_ua.setBrowserOther(form.getChild("user_uses_assistive_technology-assistive_technology_other").getValue());
	return user_uses_ua;
}
/**
*
*/
function store_user(forms) {
	var user = new Packages.org.bentoweb.amfortas.hibernate.om.User();
	user.setNameFirst(forms[ABOUT].getChild("name_first").getValue());
	user.setNameLast(forms[ABOUT].getChild("name_last").getValue());
	user.setEmail(forms[ABOUT].getChild("email").getValue());
	//if (forms["ABOUT"].getChild("language_id").getValue() == "") //be strict? 
		user.setLanguageNativeOther(forms[ABOUT].getChild("language_other").getValue()); 
	user.setStatusUser("0"); //user registered 
	user.setDateRegister(new Date());
	
	var sex_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.Sex where nameKey='"+forms[ABOUT].getChild("sex").getValue()+"'");
	user.setSex(sex_arr.get(0));
	
	var language_arr = null;
	if (forms[ABOUT].getChild("language_id").getValue() != null) {
		language_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.Language where nameKey='"+forms[ABOUT].getChild("language_id").getValue()+"'");
	} else {
		language_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.Language where nameKey='amformas.recruit.ans.language_id.2'");
	}
	user.setLanguage(language_arr.get(0));
	
	//generate username / encrypted pw / mail_confirm_hash
	//username = forms[ABOUT].getChild("name_first").getValue()+"."+forms[ABOUT].getChild("name_last").getValue();
//	username = forms[ABOUT].getChild("email").getValue();
	var pass = forms[ABOUT].getChild("password").getValue();
	password = EncryptionService.getInstance().encrypt(pass); 
	mail_confirmation_hash = forms[ABOUT].getChild("email").getValue() + System.currentTimeMillis().toString();
//	user.setUsername(username); 
	user.setPassword(password);
	user.setMailConfHash(mail_confirmation_hash); 
	//store user
	hs.save(user);
	
	// store default role for user
	var savedUser = hs.find("from org.bentoweb.amfortas.hibernate.om.User where email='"+ user.getEmail() + "'").get(0); //need the id from db
	var userHasRoleId = new Packages.org.bentoweb.amfortas.hibernate.om.UserHasRoleId();
	
	var userRole = hs.find("from org.bentoweb.amfortas.hibernate.om.Role where nameKey='amfortas.user.role.user'").get(0); //def user
	userHasRoleId.setRoleId(userRole.getRoleId());
	userHasRoleId.setUserId(savedUser.getUserId());
	var userHasRole = new Packages.org.bentoweb.amfortas.hibernate.om.UserHasRole();
	userHasRole.setId(userHasRoleId);
//	java.lang.System.err.println("savedUser___: "+savedUser.getUserId() + ".. " + userRole.getRoleId());
	hs.save(userHasRole);
	return savedUser;
}

/**
*
*/
function store_user_uses_os(forms) {
	//
	var user_uses_os = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesOs();
	//
	user_uses_os.setOsOther(forms[SETTINGS].getChild("user_uses_os-os_other").getValue());
	user_uses_os.setVersion(forms[SETTINGS].getChild("user_uses_os-version").getValue());
	//search os
	var os_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.OperatingSystem where nameKey='"+forms[SETTINGS].getChild("user_uses_os-user_uses_os_id").getValue()+"'");
	user_uses_os.setOperatingSystem(os_arr.get(0));
	var user_uses_os_id = hs.save(user_uses_os);
	
	//os setting
	var os_settings_keys = forms[SETTINGS].getChild("user_uses_os_setting-operating_system_setting_id").getValue();
	for (var i=0; i<os_settings_keys.length; i++) {
		if (os_settings_keys[i]!='amformas.recruit.ans.common.other') {
			var user_uses_os_setting = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesOsSetting();
			var os_settings_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.OperatingSystemSetting where nameKey='"+os_settings_keys[i]+"'");
			//id
			var user_uses_os_setting_id  = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesOsSettingId();
			user_uses_os_setting_id.setOperatingSystemSettingId(os_settings_arr.get(0).getOperatingSystemSettingId());
			user_uses_os_setting_id.setUserUsesOsId(user_uses_os_id);
			//
			user_uses_os_setting.setUserUsesOs(user_uses_os);
			user_uses_os_setting.setOperatingSystemSetting(os_settings_arr.get(0));
			user_uses_os_setting.setId(user_uses_os_setting_id);
			hs.save(user_uses_os_setting);
		}
	}
	var os_settings_other = forms[SETTINGS].getChild("user_uses_ua_setting-os_setting_other").getValue();
	if (os_settings_other != null) {
		var user_uses_os_setting = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesOsSetting();
		var os_settings_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.OperatingSystemSetting where operatingSystemSettingId='1'");
		//id
		var user_uses_os_setting_id  = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesOsSettingId();
		user_uses_os_setting_id.setOperatingSystemSettingId(os_settings_arr.get(0).getOperatingSystemSettingId());
		user_uses_os_setting_id.setUserUsesOsId(user_uses_os_id);
		//
		user_uses_os_setting.setUserUsesOs(user_uses_os);
		user_uses_os_setting.setOperatingSystemSetting(os_settings_arr.get(0));
		user_uses_os_setting.setId(user_uses_os_setting_id);
		user_uses_os_setting.setSettingOther(os_settings_other);
		hs.save(user_uses_os_setting);
	}
	//
	return user_uses_os;
}

/**
*
*/
function store_user_uses_device(forms) {
	//
	var user_uses_dev = new Packages.org.bentoweb.amfortas.hibernate.om.UserUsesDevice();
	//
	var device_id = forms[SETTINGS].getChild("user_uses_device-device_id").getValue();
	var device_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.Device where nameKey='"+device_id+"'");
	var experience_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.AtExperience where nameKey='"+forms[SETTINGS].getChild("user_uses_device-experience_id").getValue()+"'");
	//
	user_uses_dev.setDevice(device_arr.get(0));
	user_uses_dev.setAtExperience(experience_arr.get(0));
	if (device_id=='amformas.recruit.ans.common.other') {
		var device_other = forms[SETTINGS].getChild("user_uses_ua_setting-device_setting_other").getValue();
		if (device_other != null)
			user_uses_dev.setDeviceOther(device_other);
	}
	//
	hs.save(user_uses_dev);
	return user_uses_dev;
}

/**
*
*/
function store_user_profile(forms) {
	//user profile
	var user_profile = new Packages.org.bentoweb.amfortas.hibernate.om.UserProfile();
	var age_range_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.AgeRange where nameKey='"+forms[ABOUT].getChild("age_range_id").getValue()+"'");
	
	var language_experience_id = forms[DISABILITY].getChild("language_experience_id").getValue();
	if(language_experience_id==null)
		language_experience_id = "amformas.recruit.ans.common.good_id.0"; // for English native
		
	cocoon.log.info("language_experience_id:::: " + language_experience_id);//TODO: remov
	var language_experience_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.LanguageExperience where nameKey='"+ language_experience_id +"'");
	user_profile.setLanguageExperience(language_experience_arr.get(0));			
	var hours_per_week_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.HoursPerWeek where nameKey='"+forms[SETTINGS].getChild("user-hours_per_week").getValue()+"'");
	user_profile.setAgeRange(age_range_arr.get(0));
	user_profile.setHoursPerWeek(hours_per_week_arr.get(0));
	user_profile.setIsActive(true);
	user_profile.setDateCreation(new Date());
	//user_profile.setDisabilityOther(forms["DISABILITY"].getChild("disability_other").getValue());
	var user_profile_id = hs.save(user_profile);
	//
	
	var disability_keys = forms[DISABILITY].getChild("disability_id").getValue();
	for (var i=0; i<disability_keys.length; i++) {
		var user_has_disability = new Packages.org.bentoweb.amfortas.hibernate.om.UserProfileHasDisability();
		var disability_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.Disability where nameKey='"+disability_keys[i]+"'");
		//id
		var user_has_disability_id  = new Packages.org.bentoweb.amfortas.hibernate.om.UserProfileHasDisabilityId();
		user_has_disability_id.setDisabilityId(disability_arr.get(0).getDisabilityId());
		user_has_disability_id.setUserProfileId(user_profile_id);
		//
		user_has_disability.setUserProfile(user_profile);
		user_has_disability.setDisability(disability_arr.get(0));
		user_has_disability.setId(user_has_disability_id);
		hs.save(user_has_disability);
	}
	//
	return user_profile;
}

/*
function mailExists(forms) {
	var mail = forms[ABOUT].getChild("email").getValue();
	var user_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.User where email='"+mail+"'");
	cocoon.log.debug("xxx: "+mail);
	cocoon.log.debug("xxx: "+user_arr.length);
	if (user_arr.size()>0)
		return true;
	else
		return false; 
}
*/
