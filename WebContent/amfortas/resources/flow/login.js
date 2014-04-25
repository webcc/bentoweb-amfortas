cocoon.load("resource://org/apache/cocoon/forms/flow/javascript/Form.js");

function login() 
{	
	//var errormsg="amformas.login.fail";
	var errormsg="";
	var button_label = 'amformas.login.button.submit';
	var success=false;

	var loginform = new Form("forms/login/login-form-model.xml"); 
	var acceptlanguages = cocoon.request.getHeader("accept-language");
	if(acceptlanguages==null)
		acceptlanguages = "en";
	var selected_lang = acceptlanguages.substring(0,2);
	//var selected_lang = acceptlanguages.substring(0,2);
	
	var viewData = new Object();
	viewData.button_label = button_label;
	viewData.errormsg="";
	viewData.selected_lang = selected_lang;
	
	//loginform.showForm("login-form", viewData ); 
	viewData.errormsg = errormsg;
	loginform.showForm("login-form", viewData ); 
}

function checkloginfailure() {
	//
	openHibernateSession();
	//
	var email = action = cocoon.request.getParameter("email");
	
	var errorkey = null;
	var user = hs.find("from org.bentoweb.amfortas.hibernate.om.User where email='"+email+"'");
	if (user.size()>0) {
		if (user.get(0).getStatusUser()=='0') {
			errorkey = "amformas.login-failed.error.novalidation";
		} 
		if (user.get(0).getStatusUser()=='1') {
			errorkey = "amformas.login-failed.error.notactive";
		}
		if (user.get(0).getStatusUser()=='2') {
			errorkey = "amformas.login-failed.error.failure";
		}
	} else {
		errorkey = "amformas.login-failed.error.failure";
	}
	
	var viewData = new Object();
	viewData.errorkey = errorkey;
	cocoon.sendPage("login-failed.html",viewData);
}
