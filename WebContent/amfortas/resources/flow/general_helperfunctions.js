importClass (Packages.org.bentoweb.amfortas.hibernate.om.User);
var hs;

/**
 * Resolves an URL to a more absolute form.
 */
function resolve(uri) {
    var sourceResolver = null;
    var source = null;
    try {
        sourceResolver = cocoon.getComponent(Packages.org.apache.excalibur.source.SourceResolver.ROLE);
        source = sourceResolver.resolveURI(uri);
        return source.getURI();
    } finally {
        if (source != null)
            sourceResolver.release(source);
        if (sourceResolver != null)
            cocoon.releaseComponent(sourceResolver);
    }
}
function openHibernateSession()
{
        // Make sure Hibernate Sessions are not opened twice
        if(hs && hs.isOpen())
            return;

        // Get new Session from PersistenceFactory
        var factory = cocoon.getComponent(Packages.org.bentoweb.amfortas.components.persistence.PersistenceFactory.ROLE);
        hs = factory.createSession();
        if (hs == null) {
                throw new Packages.org.apache.cocoon.ProcessingException("Hibernate session is null ");
        }

        // Release PersistenceFactory
        cocoon.releaseComponent(factory);

        // Send "Message" to HibernateFilter to dispose the session after the view was rendered
        cocoon.request.setAttribute("DisposeHibernateSession",hs);
}
function ifother(widget,widget_q_name,widget_other_name,msg_key_pref,checkvalue)
{
	var success = true;
	var widget_q = widget.lookupWidget(widget_q_name);
	var widget_other = widget.lookupWidget(widget_other_name);
	if(widget_q.value==checkvalue)
	{
		if(widget_other.value==null) 
		{
			widget_other.setValidationError(new Packages.org.apache.cocoon.forms.validation.ValidationError(msg_key_pref+"_otherempty", true));
			success = false;;
		}
	}
	else
	{
		if(widget_other.value!=null)
		{
			
			widget_other.setValidationError(new Packages.org.apache.cocoon.forms.validation.ValidationError(msg_key_pref+"_noother", true));
			success = false;
		}
	}
	return success;
}
function ifyes(widget,widget_q_name,widget_other_name,msg_key_pref,checkvalue)
{
	var success = true;
	var widget_q = widget.lookupWidget(widget_q_name);
	var widget_other = widget.lookupWidget(widget_other_name);
	if(widget_q.value==checkvalue)
	{
		if(widget_other.value!=null && widget_other.value.length==0) 
		{
			widget_other.setValidationError(new Packages.org.apache.cocoon.forms.validation.ValidationError(msg_key_pref+"_otherempty", true));
			success = false;;
		}
	}
	else
	{
		if(widget_other.value!=null && widget_other.value.length>0)
		{
			widget_other.setValidationError(new Packages.org.apache.cocoon.forms.validation.ValidationError(msg_key_pref+"_noother", true));
			success = false;
		}
	}
	return success;
}

function getUserId() {
	//get user_id from co-warp
	var appMan = cocoon.getComponent("org.osoco.cowarp.ApplicationManager");
	var userApp = appMan.login("AmfortasApp", null); //if already logged-in, User object is returned
	cocoon.releaseComponent(appMan);
	return userApp.getId();
}
function getUser(theuserId) 
{
	var users = hs.find("from org.bentoweb.amfortas.hibernate.om.User as u where u.userId='"+theuserId+"'");
	return users.get(0);  
}
function getAge(theuserId) 
{
	return hs.find("select userProfile.ageRange.nameDefault from org.bentoweb.amfortas.hibernate.om.TestProfileHasUserProfile as tpp where tpp.testProfile.user.userId='"+theuserId+"'").get(0);	
}

function isUser(userId) {
	openHibernateSession();
	//
	var isUser = false;
	var roles = new Array();
	roles = hs.find("from org.bentoweb.amfortas.hibernate.om.UserHasRole as ur where ur.user.userId='"+userId+"'");
	for (var i=0; i<roles.size(); i++) {
		var role = roles.get(i).getRole();
		if (role.getNameKey()=='amfortas.user.role.user')
			isUser = true;
	}
	return isUser
}

function getActiveTestProfile() {
	openHibernateSession();
	var tp = null;
	var tpArr = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfile as tp where (tp.user.userId='"+getUserId()+"') and (tp.isActive='1')");
	if (tpArr.size()>0) {
		tp = tpArr.get(0);
	}
	return tp;
}

function getTestProfile(tpId) {
	openHibernateSession();
	var tp = null;
	var tpArr = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfile where testProfileId='"+tpId+"'");
	if (tpArr.size()>0) {
		tp = tpArr.get(0);
	}
	return tp;
}

function getActiveUserProfile() {
	openHibernateSession();
	var up = null;
	var upArr = hs.find("from org.bentoweb.amfortas.hibernate.om.TestProfileHasUserProfile as tphup where tphup.testProfile.testProfileId='"+getActiveTestProfile().getTestProfileId()+"'");
	for (var i=0; i<upArr.size();i++) {
		if (upArr.get(i).userProfile.isIsActive()) {
			up = upArr.get(i).getUserProfile();
			break;
		}
	}
	return up;
}

function getUserProfile(upId) {
	openHibernateSession();
	var up = null;
	var upArr = hs.find("from org.bentoweb.amfortas.hibernate.om.UserProfile where userProfileId='"+upId+"'");
	if (upArr.size()>0) {
		up = upArr.get(0);
	}
	return up;	
}

function getTestSuite(tsId) {
	openHibernateSession();
	var tsArr = hs.find("from org.bentoweb.amfortas.hibernate.om.TestSuite where testSuiteId='"+tsId+"'");
	if (tsArr.size()>0)
		return tsArr.get(0);
	else
		return null; 
}
function get_all_testsuites() 
{
	return hs.find("from org.bentoweb.amfortas.hibernate.om.TestSuite"); 	
}
function get_active_testsuites() 
{
	return hs.find("from org.bentoweb.amfortas.hibernate.om.TestSuite as ts where ts.isActive='1'"); 	
}
function getHTTPHeader() {
	var req = null;
	var header = new Array();
	req = cocoon.request.getHeader("host");
	if (req!=null)
		header["host"] = req;
	req = cocoon.request.getHeader("user-agent");
	if (req!=null)
		header["user-agent"] = req;
	req = cocoon.request.getHeader("accept-language");
	if (req!=null)
		header["accept-language"] = req;
	return header;
}