cocoon.load("resources/flow/definitions.js");
cocoon.load("resources/flow/general_helperfunctions.js");
importClass (Packages.org.bentoweb.amfortas.util.EncryptionService);
function forgotpass() 
{
	var modelpath = "";
	var viewData = new Object();
	var email = cocoon.request.getParameter("email");
	var mailconfhash = cocoon.request.getParameter("mailconfhash");
	viewData.titleKey="amformas.mail-forgotpass-reply.title";
	if (email!=null && mailconfhash==null) 
	{
		openHibernateSession();
		var obj_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.User as user where user.email='"+email+"'");
		if (obj_arr.size() > 0) 
		{
			var user = obj_arr.get(0);
			viewData.user_email = user.getEmail();
			viewData.user_lastname = user.getNameLast();	
			viewData.user_firstname = user.getNameFirst();	
			//var webappurl = "http://" + cocoon.request.getServerName() + ":" + cocoon.request.getServerPort() + "/Amfortas/amfortas/";
			viewData.resetUrl = "forgotpass.go?mailconfhash=" + user.getMailConfHash() + "&email=" + user.getEmail();
			viewData.msgKey="amformas.mail-forgotpass-reply.success";
			cocoon.sendPage("mail-forgotpass-success",viewData);
		}
		else
		{
			viewData.msgKey="amformas.mail-verify-reply.already";				
			cocoon.sendPage("page-action-reply.html",viewData);
		}
	} 
	else if(mailconfhash!=null)
	{	
		var form = new Form("forms/forgotpass/resetpass-form-model.xml"); 
		form.showForm("forgotpass/resetpass-form.form", viewData ); 	
		//show pass to reset
		var newpass = form.getChild("password").getValue();
		//get user
		openHibernateSession();
		var obj_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.User as user where user.email='"+email+"'");
		if (obj_arr.size() > 0) 
		{
			var user = obj_arr.get(0);
			newpass = EncryptionService.getInstance().encrypt(newpass); 
			user.setPassword(newpass);
			hs.save(user);
			hs.flush();
			viewData.msgKey="amformas.mail-forgotpass-reply.done";	
		}
		else
			viewData.msgKey="amformas.mail-forgotpass-reply.fail";	
		cocoon.sendPage("page-action-reply.html",viewData);
	}	
	else 
	{
		var form = new Form("forms/forgotpass/forgotpass-form-model.xml"); 
		form.showForm("forgotpass/forgotpass-form.form", viewData ); 			
	}	
}