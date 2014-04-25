cocoon.load("resources/flow/general_helperfunctions.js");

//classes
importClass (Packages.org.apache.cocoon.forms.util.I18nMessage);
//importPackage(Packages.org.apache.cocoon.components.modules.input);

function loadRunData()
{
	//openHibernateSession();
	//var global = cocoon.getComponent(InputModule.ROLE + "Selector").select("global");
	//var webappurl = global.getAttribute("webapp_url",null,null); //get from globals
	//java.lang.System.err.println("webappurl: "+webappurl);
	
	var runData = new Array(25);
	//runData.webappurl = webappurl;
	
	runData.age_ranges = get_age_ranges();
	runData.sex = get_sex();
	runData.languages = get_languages();
	runData.disabilities = get_disabilities();
	runData.at_usage = at_usage();
	runData.at_experiences = get_at_experiences();
	runData.language_experiences = get_language_experiences();
	runData.devices = get_devices();
	runData.hours_per_weeks = get_hours_per_weeks();
	runData.operating_systems = get_operating_systems();
	runData.operating_system_settings = get_operating_system_settings();
	runData.user_agents = get_user_agents();
	runData.user_agent_settings = get_user_agent_settings();
	runData.at_types = get_assistive_technology_types();	
	
	runData.at_screen_reader = get_assistive_technologies(AT_TYPES[AT_SCREEN_READER]);
	runData.at_talking_browser = get_assistive_technologies(AT_TYPES[AT_TALKING_BROWSER]);
	runData.at_screen_reader_and_magnification = get_assistive_technologies(AT_TYPES[AT_SCREEN_READER_AND_MAGNIFICATION]);
	runData.at_magnification_only = get_assistive_technologies(AT_TYPES[AT_MAGNIFICATION_ONLY]);
	runData.at_speech_and_reading = get_assistive_technologies(AT_TYPES[AT_SPEECH_AND_READING]);			
	runData.at_speech_recognition = get_assistive_technologies(AT_TYPES[AT_SPEECH_RECOGNITION]);			
	runData.at_braille = get_assistive_technologies(AT_TYPES[AT_BRAILLE]);			
	runData.at_alternative_input = get_assistive_technologies(AT_TYPES[AT_ALTERNATIVE_INPUT]);	

	return runData;						
}

function lookup_db(str_hql) {
	openHibernateSession();
	
	var obj_List = hs.find(str_hql);
	var obj_array = new Array();
	
	for (var i=0; i<obj_List.size(); i++ ) {
		var thekey = obj_List.get(i).getNameKey().toString();
		obj_array.push({value: thekey, label: new I18nMessage(thekey)});
	}
	return obj_array;
}

function at_usage() {
	var obj_array = new Array();
	obj_array.push({value: "amformas.recruit.ans.common.yes", label: new I18nMessage("amformas.recruit.ans.common.yes")});
	obj_array.push({value: "amformas.recruit.ans.common.no", label: new I18nMessage("amformas.recruit.ans.common.no")});
	return obj_array;
}

function get_age_ranges() {
	return lookup_db("from org.bentoweb.amfortas.hibernate.om.AgeRange where ageRangeId>1");
}
function get_languages() {
	var obj_array = new Array();
	obj_array = lookup_db("from org.bentoweb.amfortas.hibernate.om.Language as lang where lang.languageId>1 ORDER BY lang.nameDefault ASC");//get the null for other
	obj_array.push({value: "amformas.recruit.ans.language_id.0", label: new I18nMessage("amformas.recruit.ans.language_id.0")});
	return obj_array;
}
function get_sex() {  
	return lookup_db("from org.bentoweb.amfortas.hibernate.om.Sex where sexId>1");
}
function get_language_experiences() {
	return lookup_db("from org.bentoweb.amfortas.hibernate.om.LanguageExperience where languageExperienceId>1");
}
function get_disabilities() {
	return lookup_db("from org.bentoweb.amfortas.hibernate.om.Disability where disabilityId>1");
}
function get_assistive_technologies(key) {
	if (key=="amformas.recruit.ans.user_agent_types.talking_browser") {
		var obj_array = new Array();
		obj_array = lookup_db("from org.bentoweb.amfortas.hibernate.om.UserAgent as ua where (ua.userAgentTypes.nameKey='"+key+"') and (ua.visible=true)");
		obj_array.push({value: "amformas.recruit.atgeneral.other", label: new I18nMessage("amformas.recruit.atgeneral.other")}); 
		return obj_array;
	} else {
		var obj_array = new Array();
		obj_array = lookup_db("from org.bentoweb.amfortas.hibernate.om.AssistiveTechnology as at where (at.assistiveTechnologyTypes.nameKey='"+key+"') and (at.visible=true)");
		obj_array.push({value: "amformas.recruit.atgeneral.other", label: new I18nMessage("amformas.recruit.atgeneral.other")}); 
		return obj_array;
	}
}
//extended search for at other
function get_assistive_technologies_other(at_types_id, str) { 
	openHibernateSession();
	//search in table assistive technology and user agent
	var obj_array = new Array();
	var search_arr = str.split(" ");
	var strHQLat = "from org.bentoweb.amfortas.hibernate.om.AssistiveTechnology as at where (at.assistiveTechnologyTypes.nameKey='"+at_types_id+"') AND (at.assistiveTechnologyId<52) AND (";
	var strHQLua = "from org.bentoweb.amfortas.hibernate.om.UserAgent as ua where (ua.userAgentTypes.nameKey='amformas.recruit.ans.user_agent_types.talking_browser') AND (ua.userAgentId!=48) AND (";
	strHQLat = strHQLat+ "(at.nameDefault like '%"+search_arr[0]+"%')";
	strHQLua = strHQLua+ "(ua.nameDefault like '%"+search_arr[0]+"%')";
	for (var i=1; i<search_arr.length; i++) {
		strHQLat = strHQLat+"and (at.nameDefault like '%"+search_arr[i]+"%')";
		strHQLua = strHQLua+"and (ua.nameDefault like '%"+search_arr[i]+"%')";	
	}
	strHQLat = strHQLat+") and (at.visible=false)"; 
	strHQLua = strHQLua+") and (ua.visible=false)"; 
	//
	var strHQL="";
	if (at_types_id=="amformas.recruit.ans.user_agent_types.talking_browser") {
		strHQL=strHQLua;
	} else {
		strHQL=strHQLat;
	}
	var obj_ListAt = hs.find(strHQL);
	for (var i=0; i<obj_ListAt.size(); i++ ) {
		var thekey = obj_ListAt.get(i).getNameKey().toString();
		obj_array.push({value: thekey, label: new I18nMessage(thekey)});
	}
	if (obj_array.length >0) {
		obj_array.push({value: "amformas.recruit.ans.common.none", label:  new I18nMessage("amformas.recruit.ans.common.none")});
	}
	//
	return obj_array;
}
//search for browser other
function get_user_agent_other(str) {
	openHibernateSession();
	var obj_array = new Array();
	if (str != null) {
		var search_arr = str.split(" ");
		var strHQL = "from org.bentoweb.amfortas.hibernate.om.UserAgent as ua where (ua.userAgentTypes.nameKey!='amformas.recruit.ans.user_agent_types.talking_browser') and (";
		strHQL = strHQL+ "(ua.nameDefault like '%"+search_arr[0]+"%')";
		for (var i=1; i<search_arr.length; i++) {
			strHQL = strHQL+"and (ua.nameDefault like '%"+search_arr[i]+"%')";	
		}
		strHQL = strHQL+") and (ua.visible=false)"; 
		var obj_List = hs.find(strHQL);
		for (var i=0; i<obj_List.size(); i++ ) {
			var thekey = obj_List.get(i).getNameKey().toString();
			obj_array.push({value: thekey, label: new I18nMessage(thekey)});
		}
		if (obj_array.length >0) {
			obj_array.push({value: "amformas.recruit.ans.common.none", label:  new I18nMessage("amformas.recruit.ans.common.none")});
		}
	}
	return obj_array;
}
function get_assistive_technology_types() 
{
	//cause 'talking browsers' are in table user_agent, added separately
	var obj_array = new Array();
	obj_array.push({value: "amformas.recruit.ans.assistive_technology_types_id.other", label: new I18nMessage("amformas.recruit.ans.assistive_technology_types_id.other")});
	obj_array.push({value: "amformas.recruit.ans.user_agent_types.talking_browser", label: new I18nMessage("amformas.recruit.ans.user_agent_types.talking_browser")});
	obj_array = obj_array.concat(lookup_db("from org.bentoweb.amfortas.hibernate.om.AssistiveTechnologyTypes as ast where ast.assistiveTechnologyTypesId>1 ORDER BY ast.assistiveTechnologyTypesId ASC"));
	return obj_array.reverse();
}
function get_at_experiences() {
	return lookup_db("from org.bentoweb.amfortas.hibernate.om.AtExperience where atExperienceId>1");
}
function get_devices() {
	return lookup_db("from org.bentoweb.amfortas.hibernate.om.Device as dev where dev.deviceId>0 ORDER BY dev.deviceId DESC");
}
function get_hours_per_weeks() {
	return lookup_db("from org.bentoweb.amfortas.hibernate.om.HoursPerWeek where hoursPerWeekId>1");
}
function get_language_experiences() {
	return lookup_db("from org.bentoweb.amfortas.hibernate.om.LanguageExperience where languageExperienceId>1");
}
function get_operating_systems() {
	return lookup_db("from org.bentoweb.amfortas.hibernate.om.OperatingSystem as os where os.operationgSystemId>0 ORDER BY os.operationgSystemId DESC");
}
function get_operating_system_settings() {
	var obj_array = new Array();
	obj_array = lookup_db("from org.bentoweb.amfortas.hibernate.om.OperatingSystemSetting where operatingSystemSettingId>1");
	obj_array.push({value: "amformas.recruit.ans.common.other", label: new I18nMessage("amformas.recruit.ans.common.other")});
	return obj_array;
}
function get_user_agents() {
	var obj_array = new Array();
	obj_array = lookup_db("from org.bentoweb.amfortas.hibernate.om.UserAgent as ua where (ua.userAgentTypes.nameKey='amformas.recruit.ans.user_agent_types.browser') and (ua.nameKey!='amformas.recruit.ans.common.other') and (ua.visible=true)");
	obj_array.push({value: "amformas.recruit.ans.common.none", label: new I18nMessage("amformas.recruit.ans.common.none")});
	return obj_array.concat(lookup_db("from org.bentoweb.amfortas.hibernate.om.UserAgent as ua where (ua.nameKey='amformas.recruit.ans.common.other') and (ua.visible=true)")); 
}
function get_user_agent_settings() {
 	var obj_array = new Array();
 	obj_array = lookup_db("from org.bentoweb.amfortas.hibernate.om.UserAgentSetting where userAgentSettingId>1");
 	obj_array.push({value: "amformas.recruit.ans.common.other", label: new I18nMessage("amformas.recruit.ans.common.other")});
	return obj_array;
}

function isEmailAvailable(email) 
{
	openHibernateSession();
	var obj_List = hs.find("from org.bentoweb.amfortas.hibernate.om.User as user where user.email='" + email + "'");
	if(obj_List.size()>0)
	{
	cocoon.log.info("isEmailAvailable=false;");
		return false;
	}
	else
	{
	cocoon.log.info("isEmailAvailable=true;");
		return true;
	}
}