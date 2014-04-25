cocoon.load("resources/flow/general_helperfunctions.js");
//cocoon.load("resources/flow/definitions.js");

//classes
importClass (Packages.org.apache.cocoon.forms.util.I18nMessage);
importClass (Packages.java.util.ArrayList);
importClass (org.hibernate.Transaction);

var USERS_ADMIN_PAGE = "admin_users-auth.html";
var USERS_ADMIN_RESPONSE_PAGE = "admin_users_response-auth.html";
var VERIFIED = "1";
var ACTIVATED = "2";
var ACTION_NAME = "user_admin_action";

var msgKeys = new Array(4);
msgKeys.verified="amfortas.admin.response.verify";
msgKeys.activated="amfortas.admin.response.activate";
msgKeys.deactivated="amfortas.admin.response.deactivate";
msgKeys.fail_nouser="amfortas.admin.response.fail_nouser";
msgKeys.fail_nokey="amfortas.admin.response.fail_nokey";

var action = "";
var transaction="";
var adminrole_key = "amfortas.user.role.admin";

function admin_users() 
{
	openHibernateSession();
	//open transaction
	//transaction = hs.beginTransaction();	
	action = cocoon.request.getParameter(ACTION_NAME);
	if(action == "verify")
	{
		setStatusUser(VERIFIED,action);
	}
	else if(action == "activate")
	{
		setStatusUser(ACTIVATED,action);
	}
	else if(action == "deactivate")
	{
		setStatusUser(VERIFIED,action);;
	}
	else if(action == "statusmail")
	{
		var viewData = new Object();
		var usermail = cocoon.request.getParameter("usermail");
		var obj_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.User as user where user.email='"+ usermail +"'");
		if (obj_arr.size() > 0) 
		{
			var user = obj_arr.get(0);			
			var userstatus = user.getStatusUser();
			
			if(userstatus=="1")
				viewData.msgkey = "amfortas.email.accountstatus.verified";
			else if(userstatus=="2")
				viewData.msgkey = "amfortas.email.accountstatus.activated";
			else // 0
				viewData.msgkey = "amfortas.email.accountstatus.notverified";
			viewData.user_email = usermail;
			viewData.messagekey = "amfortas.admin.response.status"; 
			viewData.user_lastname = user.getNameLast();
			viewData.user_firstname = user.getNameFirst();
			
			cocoon.sendPage("mail-account-status",viewData);
		}
		
	}	
	else
	{

		var filter = cocoon.request.getParameter("filter");
		var usersList = new ArrayList();
		if(filter=="noverified")
			usersList = get_notverified_users();
		else if(filter=="noactivated")
			usersList = get_noactivated_users();
		else if(filter=="activated")
			usersList = get_activated_users();
		else
			usersList = get_all_users();
		
		var viewData = new Object();
		
		var l = usersList.size();		
		viewData.usersList = usersList;
		viewData.l = l;
		//close transaction
		//transaction.commit();	
	    cocoon.sendPage(USERS_ADMIN_PAGE,viewData);
	}
    
}

function setStatusUser(flag, action)
{
		var viewData = new Object();
		var userkey = cocoon.request.getParameter("userkey");
		if (userkey!="") 
		{
			var obj_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.User as user where user.mailConfHash='"+ userkey +"'");
			if (obj_arr.size() > 0) 
			{
				//set status flag
				obj_arr.get(0).setStatusUser(flag);
				hs.update(obj_arr.get(0));
				viewData.messagekey = "amfortas.admin.response." + action;
			} 
			else 
				viewData.messagekey = "amfortas.admin.response.fail_nouser";
		}
		else 
			viewData.messagekey = "amfortas.admin.response.fail_nokey";
		//close transaction
		//transaction.commit();
		cocoon.sendPage(USERS_ADMIN_RESPONSE_PAGE, viewData);	
}

function get_all_users() 
{
	return hs.find("from org.bentoweb.amfortas.hibernate.om.UserHasRole as us where us.role.nameKey='amfortas.user.role.user'"); 
	
}
function get_notverified_users() 
{
	return hs.find("from org.bentoweb.amfortas.hibernate.om.UserHasRole as us where (us.role.nameKey='amfortas.user.role.user') and (us.user.statusUser=0)");
}
function get_noactivated_users() 
{
	return hs.find("from org.bentoweb.amfortas.hibernate.om.UserHasRole as us where (us.role.nameKey='amfortas.user.role.user') and (us.user.statusUser=1)");
}
function get_activated_users() 
{
	return hs.find("from org.bentoweb.amfortas.hibernate.om.UserHasRole as us where (us.role.nameKey='amfortas.user.role.user') and (us.user.statusUser=2)");
}
function get_rejected_users() 
{
	return hs.find("from org.bentoweb.amfortas.hibernate.om.UserHasRole as us where (us.role.nameKey='amfortas.user.role.user') and (us.user.statusUser=3)");
}
