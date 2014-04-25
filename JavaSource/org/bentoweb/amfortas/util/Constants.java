package org.bentoweb.amfortas.util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * Constants and default values for Hibernate-Cocoon.
 * 
 * @author carlos & sandor
 * @since  2005-12-23
 *
 */
public class Constants 
{

    public static final String TESTCASE_STATUS_READY = "testcase_status";
    public static final String TESTCASE_STATUS_READY_DEFAULT = "accepted for end user evaluation";
    public static final String TESTCASE_SCHEMA_VALIDATE = "tcdl_validate";
    
    // TODO: Add here relevant constants
	public static final int TESTCASE_SET_SIZE = 20;

	// testcases should be selected that are not 
	// tested by more than 5 users (different ones)
	public static final int TESTCASE_FINISHED_SUM = 5;
    
    public static final String USER_ACCOUNT_STATUS_NOTVERIFIED = "0";
    public static final String USER_ACCOUNT_STATUS_VERIFIED = "1";
    public static final String USER_ACCOUNT_STATUS_ACCEPTED = "2";
    public static final String USER_ROLE_NAME = "user_role_name";
    
	public static final String SITEMAPPARAMETER_PARAMETERS = "parameters";
	public static final String SITEMAPPARAMETER_EMAIL = "email";
	public static final String SITEMAPPARAMETER_PASSWORD = "password";
	//Testcase experience identifier
	public static final String HASHKEY_IDENTIFIER_TYPE = "type";
	public static final String HASHKEY_IDENTIFIER_PRODUCT = "product";
	public static final String HASHKEY_IDENTIFIER_VERSION = "version";
	public static final String HASHKEY_IDENTIFIER_EXPERIENCE = "experience";
	public static final String HASHKEY_IDENTIFIER_MINIMUMLEVEL = "minimumLevel";
	//Mapping hash identifiers - for db and tcdl
	public static final String HASHKEY_ASSISTIVE_TECHNOLOGY = "AssistiveTechnology";
	public static final String HASHKEY_USER_AGENT = "UserAgent";
	public static final String HASHKEY_DEVICE = "Device";
	public static final String HASHKEY_DISABILITY = "disability";
	//
	public static final String HASHKEY_POOL_IDENTIFIER_TESTCASE_ID = "testcase_id";
	public static final String HASHKEY_POOL_IDENTIFIER_SCENARIO_ID = "scenario_id";
    
    public static final String TC_INDEX_FILE_DATE_FORMAT = "dd/MM/yyyy - HH:mm:ss";
    public static final Locale TC_INDEX_FILE_LOCALE = Locale.ENGLISH;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TC_INDEX_FILE_DATE_FORMAT, Constants.TC_INDEX_FILE_LOCALE);    
	
	//mappings of database key to corresponding value in xsd
	public static final String db2xsd(String key) {
		HashMap map = new HashMap();
		//at types
		map.put("amformas.recruit.ans.assistive_technology_types_id.0","");
		map.put("amformas.recruit.ans.assistive_technology_types_id.1","screenreader");
		map.put("amformas.recruit.ans.assistive_technology_types_id.2","talking browser");
		map.put("amformas.recruit.ans.assistive_technology_types_id.3","screenreader with magnification");
		map.put("amformas.recruit.ans.assistive_technology_types_id.4","magnification software");
		map.put("amformas.recruit.ans.assistive_technology_types_id.5","speech and hearing support software");
		map.put("amformas.recruit.ans.assistive_technology_types_id.6","speech recognition software");
		map.put("amformas.recruit.ans.assistive_technology_types_id.7","Braille display");
		map.put("amformas.recruit.ans.assistive_technology_types_id.8","alternative input devices");
		//experience
		map.put("amformas.recruit.ans.common.experience_id.1","1");
		map.put("amformas.recruit.ans.common.experience_id.2","2");
		map.put("amformas.recruit.ans.common.experience_id.3","3");
		map.put("amformas.recruit.ans.common.experience_id.4","4");
		map.put("amformas.recruit.ans.common.experience_id.5","5");
		//assisitive technology
		//+++ screenreader
		map.put("amformas.recruit.ans.screen_reader_id.1","JAWS");
		map.put("amformas.recruit.ans.screen_reader_id.2","Window-Eyes");
		map.put("amformas.recruit.ans.screen_reader_id.3","HAL");
		//+++
		map.put("amformas.recruit.ans.talking_browser_id.1","IBM Home Page Reader");
		map.put("amformas.recruit.ans.talking_browser_id.2","FreedomBox");
		map.put("amformas.recruit.ans.talking_browser_id.3","EdWeb");
		//+++
		map.put("amformas.recruit.ans.sr_N_magn_sw_id.1","ZoomText");
		map.put("amformas.recruit.ans.sr_N_magn_sw_id.2","Supernova");
		map.put("amformas.recruit.ans.sr_N_magn_sw_id.3","VoiceOver");
		//+++
		map.put("amformas.recruit.ans.magn_sw_id.1","Lunar");
		map.put("amformas.recruit.ans.magn_sw_id.2","MAGic");
		//+++
		map.put("amformas.recruit.ans.speech_N_read_sw_id.1","BrowseAloud");
		map.put("amformas.recruit.ans.speech_N_read_sw_id.2","ReadPlease");
		map.put("amformas.recruit.ans.speech_N_read_sw_id.3","Kurzweil 3000");
		//+++
		map.put("amformas.recruit.ans.speechrec_sw_id.1","Dragon Naturally Speaking");
		map.put("amformas.recruit.ans.speechrec_sw_id.2","ViaVoice");
		map.put("amformas.recruit.ans.speechrec_sw_id.3","QPointer");
		//+++
		map.put("amformas.recruit.ans.sr_N_braille_id.1","Alva Braille display");
		map.put("amformas.recruit.ans.sr_N_braille_id.2","Baum Braille display");
		map.put("amformas.recruit.ans.sr_N_braille_id.3","Papenmeier Braille display");
		map.put("amformas.recruit.ans.sr_N_braille_id.4","Tieman Braille display");
		//+++
		map.put("amformas.recruit.ans.alterin_id.1","switch and scanning device");
		map.put("amformas.recruit.ans.alterin_id.2","alternative keyboard");
		map.put("amformas.recruit.ans.alterin_id.3","headmouse / tracking device");
		//user agents
		map.put("amformas.recruit.ans.user_agent.msie","Microsoft Internet Explorer");
		map.put("amformas.recruit.ans.user_agent.netscape","Netscape");
		map.put("amformas.recruit.ans.user_agent.firefox","Firefox");
		map.put("amformas.recruit.ans.user_agent.opera","Opera");
		map.put("amformas.recruit.ans.user_agent.safari","Safari");
		//devices
		map.put("amformas.recruit.ans.device_id.1","PC");
		map.put("amformas.recruit.ans.device_id.2","Television");
		map.put("amformas.recruit.ans.device_id.3","PDA");
		map.put("amformas.recruit.ans.device_id.4","MobilePhone");
		//disability
		map.put("amformas.recruit.ans.disability_id.1","blindness");
		map.put("amformas.recruit.ans.disability_id.2","colour vision deficiency");
		map.put("amformas.recruit.ans.disability_id.3","low vision");
		map.put("amformas.recruit.ans.disability_id.4","deafness");
		map.put("amformas.recruit.ans.disability_id.5","hard of hearing");
		map.put("amformas.recruit.ans.disability_id.6","dyslexia");
		map.put("amformas.recruit.ans.disability_id.7","cognitive disability");
		map.put("amformas.recruit.ans.disability_id.8","dexterity impairment");
		map.put("amformas.recruit.ans.disability_id.9","motor impairment");
		map.put("amformas.recruit.ans.disability_id.10","other");   // attention 
		map.put("amformas.recruit.ans.disability_id.11","other");   // check if that is wanted
		map.put("amformas.recruit.ans.disability_id.12","other");
		
		return (String)map.get(key);
	}
	public static final String xsd2db(String key) {
		HashMap map = new HashMap();
		return (String)map.get(key);
	}	
}
