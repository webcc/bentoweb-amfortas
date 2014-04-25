cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");
cocoon.load("resources/flow/load_recruit.js");
cocoon.load("resources/flow/store_recruit.js");
cocoon.load("resources/flow/definitions.js");
cocoon.load("resources/flow/general_helperfunctions.js");

var AT_TYPES = new Array();
AT_TYPES[AT_SCREEN_READER]='amformas.recruit.ans.assistive_technology_types_id.1';//screen reader
AT_TYPES[AT_TALKING_BROWSER]='amformas.recruit.ans.user_agent_types.talking_browser';
AT_TYPES[AT_SCREEN_READER_AND_MAGNIFICATION]='amformas.recruit.ans.assistive_technology_types_id.3';
AT_TYPES[AT_MAGNIFICATION_ONLY]='amformas.recruit.ans.assistive_technology_types_id.4';
AT_TYPES[AT_SPEECH_AND_READING]='amformas.recruit.ans.assistive_technology_types_id.5';
AT_TYPES[AT_SPEECH_RECOGNITION]='amformas.recruit.ans.assistive_technology_types_id.6';
AT_TYPES[AT_BRAILLE]='amformas.recruit.ans.assistive_technology_types_id.7';
AT_TYPES[AT_ALTERNATIVE_INPUT]='amformas.recruit.ans.assistive_technology_types_id.8';
var YES = "amformas.recruit.ans.common.yes";
var NO = "amformas.recruit.ans.common.no";

var errormsg=""; //to remove
var viewData="";
var modelpath = "forms/recruit/model/";
var button_label = 'amformas.recruit.button.next';

var forms = null;
var forms_other = null;
var current_form="";
var forms_history = null;
var form_index=0;
var useragent_header="";

importClass (Packages.org.apache.cocoon.forms.util.I18nMessage);

var runData = null;
var profileAdd = false;

function recruit() {
	var userId = null;
	storeprofile(userId);
}

function addprofile() {
	var userId = getUserId();
	storeprofile(userId);
}

function storeprofile(userId) 
{	

	forms = new Array();
	forms_other = new Array();
	forms_history = new Array();
	runData = new Array();

	useragent_header = cocoon.request.getHeader("user-agent");
	runData = loadRunData();
	
	//
	var assistive_technology;
	if (userId==null) {
		about();
		disability();
		assistive_technology = forms[DISABILITY].getChild("assistive_technology").getValue();
		profileAdd = false;
	} else {
		assistive_technology = YES;
		profileAdd = true;
	}
	
	while(assistive_technology==YES)
	{	
		at_select();
		var assistive_technology_types_id = forms[AT_SELECT].getChild("assistive_technology-assistive_technology_types_id").getValue();
		if(assistive_technology_types_id==AT_TYPES[AT_SCREEN_READER])
			at_screen_reader();		
		else if(assistive_technology_types_id==AT_TYPES[AT_TALKING_BROWSER])
			at_talking_browser();
		else if(assistive_technology_types_id==AT_TYPES[AT_SCREEN_READER_AND_MAGNIFICATION])
			at_screen_reader_and_magnification();
		else if(assistive_technology_types_id==AT_TYPES[AT_MAGNIFICATION_ONLY])
			at_magnification_only();
		else if(assistive_technology_types_id==AT_TYPES[AT_SPEECH_AND_READING])
			at_speech_and_reading();
		else if(assistive_technology_types_id==AT_TYPES[AT_SPEECH_RECOGNITION])
			at_speech_recognition();
		else if(assistive_technology_types_id==AT_TYPES[AT_BRAILLE])
			at_braille();
		else if(assistive_technology_types_id==AT_TYPES[AT_ALTERNATIVE_INPUT])
			at_alternative_input();
		
		var strTechOther;
		var atTechId;
		try
		{
			assistive_technology = forms[current_form].getChild("another-assistive_technology").getValue();	
			atTechId = forms[current_form].getChild("user_uses_assistive_technology-assistive_technology_id").getValue();
			strTechOther = forms[current_form].getChild("user_uses_assistive_technology-assistive_technology_other").getValue();
		}
		catch(err)
		{
			assistive_technology = NO;
			errormsg=err;
		}
		
		if (atTechId == "amformas.recruit.atgeneral.other") {
			at_other(assistive_technology_types_id, strTechOther);
		}
		
	}
	settings(userId);
	var browser_selection = forms[SETTINGS].getChild("user_uses_user_agent-user_agent_id").getValue();
	if (browser_selection == "amformas.recruit.ans.common.other") {
		var br_other = forms[SETTINGS].getChild("user_uses_user_agent-browser_other").getValue();
		browser_other(br_other);
	}
	submitIt(userId);
} 

function about()
{
	addForm(ABOUT);
	viewData.age_ranges = runData.age_ranges;
	viewData.sex = runData.sex;
	viewData.languages = runData.languages;
    showCurrentForm();
}
function disability()
{
	addForm(DISABILITY);
	viewData.language_experiences = runData.language_experiences;
	viewData.disabilities = runData.disabilities;
	viewData.at_usage = runData.at_usage;
	var language_id = forms[ABOUT].getChild("language_id").getValue();
	viewData.language_id = language_id;
	showCurrentForm();
}

function at_select()
{
	addForm(AT_SELECT);
	viewData.at_types = runData.at_types;
	showCurrentForm();  
} 
function at_screen_reader()
{
	addForm(AT_SCREEN_READER);
	viewData.assistive_technologies = runData.at_screen_reader;
	viewData.at_experiences = runData.at_experiences;
	viewData.at_usage = runData.at_usage;
	showCurrentForm(); 
}
function at_talking_browser()
{
	addForm(AT_TALKING_BROWSER);
	viewData.assistive_technologies = runData.at_talking_browser;
	viewData.at_experiences = runData.at_experiences;
	viewData.at_usage = runData.at_usage;
	showCurrentForm();
}
function at_alternative_input()
{
	addForm(AT_ALTERNATIVE_INPUT);
	viewData.assistive_technologies = runData.at_alternative_input;
	viewData.at_experiences = runData.at_experiences;
	viewData.at_usage = runData.at_usage;
	showCurrentForm();
}
function at_braille()
{
	addForm(AT_BRAILLE);
	viewData.assistive_technologies = runData.at_braille;
	viewData.at_experiences = runData.at_experiences;
	viewData.at_usage = runData.at_usage;	
	showCurrentForm();
}
function at_magnification_only()
{
	addForm(AT_MAGNIFICATION_ONLY);
	viewData.assistive_technologies = runData.at_magnification_only;
	viewData.at_experiences = runData.at_experiences;
	viewData.at_usage = runData.at_usage;	
	showCurrentForm();
}
function at_speech_and_reading()
{
	addForm(AT_SPEECH_AND_READING);
	viewData.assistive_technologies = runData.at_speech_and_reading;
	viewData.at_experiences = runData.at_experiences;
	viewData.at_usage = runData.at_usage;	
	showCurrentForm();
}
function at_speech_recognition()
{
	addForm(AT_SPEECH_RECOGNITION);
	viewData.assistive_technologies = runData.at_speech_recognition;
	viewData.at_experiences = runData.at_experiences;
	viewData.at_usage = runData.at_usage;	
	showCurrentForm();
}
function at_screen_reader_and_magnification()
{
	addForm(AT_SCREEN_READER_AND_MAGNIFICATION);
	viewData.assistive_technologies = runData.at_screen_reader_and_magnification;
	viewData.at_experiences = runData.at_experiences;
	viewData.at_usage = runData.at_usage;	
	showCurrentForm();
}

function at_other(at_types_id, str) {
	//search for mapping
	var other_tech = get_assistive_technologies_other(at_types_id, str);
	if (other_tech.length > 0) {
		addOtherForm("at_other");
		viewData.assistive_technologies = other_tech;
		showCurrentFormOther("at_other");
	}
}

function browser_other(str) {
	var other_browser = get_user_agent_other(str);
	if (other_browser.length > 0) {
		addOtherForm("browser_other");
		viewData.user_agent = other_browser;
		showCurrentFormOther("browser_other");
	}
}

function settings(userId)
{
	addForm(SETTINGS);
	viewData.devices = runData.devices;
	viewData.hours_per_weeks = runData.hours_per_weeks;
	viewData.operating_systems = runData.operating_systems;
	viewData.operating_system_settings = runData.operating_system_settings;
	viewData.user_agents = runData.user_agents;
	viewData.user_agent_settings = runData.user_agent_settings;
	viewData.at_usage = runData.at_usage;	
	viewData.at_experiences = runData.at_experiences;
	viewData.showlastquestion = true;
	viewData.talkingbrowser = uses_talking_browser();
	if (userId!=null) {
		viewData.showlastquestion = false;
	}
	try
	{
		//get browser info from header
		var ua_info = detect_ua_info();
		if (uses_talking_browser()) {
			forms[current_form].getChild("user_uses_user_agent-user_agent_id").setValue("amformas.recruit.ans.common.none");
			forms[current_form].getChild("user_uses_user_agent-vesion").setValue("");
		} else {
			forms[current_form].getChild("user_uses_user_agent-user_agent_id").setValue(ua_info.name);
			forms[current_form].getChild("user_uses_user_agent-vesion").setValue(ua_info.ver);
		}
	}
	catch(e)
	{
	}
	try
	{
		//get os info from headers
		var os_info = detect_os_info();
		forms[current_form].getChild("user_uses_os-user_uses_os_id").setValue(os_info.name);
		forms[current_form].getChild("user_uses_os-version").setValue(os_info.ver);
	}
	catch(e)
	{
	}
	showCurrentForm();   
} 

function submitIt(userId)
{
	//store data
	var user = store_recruit(forms, forms_other, userId);
	//If user has select an "other disability" inform admin by email
	try
	{	
		var disability_other_value = forms[DISABILITY].getChild("disability_other").getValue();
		if(disability_other_value!=null)
		{
			//get user email
			var usermail = forms[ABOUT].getChild("email").getValue();			
			//send mail to admin
			var mailer = cocoon.getComponent(Packages.org.apache.cocoon.mail.MailSender.ROLE);
			mailer.setFrom("amfortas@bentoweb.org");
			mailer.setTo("chairman@isdac.org");
			mailer.setBody("A user("+usermail+") has been just registered with filling other disabilitytest to: " + disability_other_value);
			mailer.setSubject("cocoon");
			mailer.send();
			cocoon.releaseComponent(mailer);
			cocoon.log.debug("Mail when a user selects other disability sent!");
			//java.lang.System.err.println("mail sent ");
		}
		if(profileAdd)
		{
			//send mail to admin
			var mailer = cocoon.getComponent(Packages.org.apache.cocoon.mail.MailSender.ROLE);
			mailer.setFrom("amfortas@bentoweb.org");
			mailer.setTo("bika-amfortas@fit.fraunhofer.de");
			mailer.setBody("A user(with user id:"+user.getUserId()+") has added a new profile. You might need to assign him/her a testsuite.");
			mailer.setSubject("New profile added");
			mailer.send();
			cocoon.releaseComponent(mailer);
			cocoon.log.debug("Mail when new profile added sent!");
			java.lang.System.err.println("mail sent ");
		}
	}
	catch(e)
	{
			cocoon.releaseComponent(mailer);
			//java.lang.System.err.println("error sending mail when a user selects other disability");
			cocoon.log.error("error sending mail when a user selects other disability");
	}

	if (userId==null) {
		viewData.activateUrl = "mail_confirm?confirm=" + user.getMailConfHash();
		//cocoon.log.info("urllll: " + viewData.activateUrl);
	    viewData.user_email = forms[ABOUT].getChild("email").getValue();
	    viewData.user_firstname = forms[ABOUT].getChild("name_first").getValue();
	    viewData.user_lastname = forms[ABOUT].getChild("name_last").getValue();
	    cocoon.sendPage("recruit-success-pipeline", viewData);
	} else {
		cocoon.sendPage("selectprofile");
	}
}

function addForm(formname)
{
	current_form=formname;
	form_index++;
	forms_history[form_index]=formname;
	forms[current_form] = new Form(modelpath + "recruit-form-model-" + formname + ".xml"); 
	viewData = new Object();
}

function addOtherForm(formname) {
	forms_other[current_form] = new Form(modelpath + "recruit-form-model-" + formname + ".xml"); 
	viewData = new Object();
}

function showCurrentForm()
{
	loadStdParam();
	if (profileAdd) {
    	forms[current_form].showForm("auth-recruit-page-" + current_form, viewData ); 
    } else {
    	forms[current_form].showForm("recruit-page-" + current_form, viewData ); 
    }
}

function showCurrentFormOther(form)
{
    loadStdParam(); 
    forms_other[current_form].showForm("recruit-page-"+form, viewData );
}

function loadStdParam() {
	viewData.button_label = button_label;
	viewData.errormsg = errormsg;
	var acceptlanguages = cocoon.request.getHeader("accept-language");
	var selected_lang = null;
	if (acceptlanguages!=null) {
		selected_lang = acceptlanguages.substring(0,2);//TODO take more
	} else { 
		selected_lang = "";
	}
	viewData.selected_lang = selected_lang;
}

function getPrevFormName()
{
	return forms_history[form_index-1];
}
function getPrevForm()
{
	return forms[getPrevFormName()];
}

/*
User-Agent     = "User-Agent" ":" 1*( product | comment )
Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322)
Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8) Gecko/20051111 Firefox/1.5
Lynx/2.8.4rel.1 libwww-FM/2.14 SSL-MM/1.4.1 OpenSSL/0.9.7a
*/
function detect_ua_info()
{
	var ua_info = new Array(2);
	ua_info.ver = "";
	ua_info.name = "";	
	useragent_header = useragent_header.toLowerCase();
	for(var i=0;i<runData.user_agents.length;i++)
	{
		var temp = runData.user_agents[i].value.toLowerCase(); 
		var temp_nopref = temp.substring(temp.lastIndexOf(".")+1);//remove preffix (amfortas.X.X.UA)
		var index = useragent_header.indexOf(temp_nopref);
		if(index>0)
		{
			//get version
			var name_size = temp_nopref.length();
			var start = index+name_size+1; //index found the name +1 
			ua_info.ver = useragent_header.substring(start,start+3);//and +3 (X.X) for the ver
			ua_info.name = temp;
			return ua_info;
		}
	}
}
function detect_os_info()
{
	var os_info = new Array(2);
	os_info.ver = "";
	os_info.name = "";
	useragent_header = useragent_header.toLowerCase();
	for(var i=0;i<runData.operating_systems.length;i++)
	{
		var temp = runData.operating_systems[i].value.toLowerCase(); 
		var temp_nopref = temp.substring(temp.lastIndexOf(".")+1);//remove preffix (amfortas.X.X.UA)
		temp_nopref = temp_nopref.replace("_"," ");// for windows_nt to windows nt
		var index = useragent_header.indexOf(temp_nopref);
		if(index>0)
		{
			//get version
			var name_size = temp_nopref.length();
			var start = index+name_size+1; //index found the name +1 
			os_info.ver = useragent_header.substring(start,start+3);//and +3 (X.X) for the ver
			os_info.name = temp;
			return os_info; 
		}
	}
	return os_info;
}

function uses_talking_browser() {
	var tb = false;
	for (var form in forms) {
		if (form.toString() == AT_TALKING_BROWSER) {
			tb = true;
			break;
		}
	}
	return tb;
}
