
function mailconfirm() {
	//
	openHibernateSession();
	var viewData = new Object();
	if (cocoon.request.getParameter("confirm")!="") {
		var obj_arr = hs.find("from org.bentoweb.amfortas.hibernate.om.User as user where user.mailConfHash='"+cocoon.request.getParameter("confirm")+"'");
		viewData.titleKey="amformas.mail-verify-reply.title";
		//
		if (obj_arr.size() > 0) 
		{
			//set status flag to 1
			var user = obj_arr.get(0);
			if(user.getStatusUser()==0)
			{
				user.setStatusUser("1");
				hs.update(obj_arr.get(0));
				hs.flush();
				viewData.email = user.getEmail();
				viewData.msgKey="amformas.mail-verify-reply.success";
				cocoon.sendPage("mail-verify-success",viewData);
			}
			else
			{
				viewData.msgKey="amformas.mail-verify-reply.already";				
				cocoon.sendPage("mail-verify-reply.html",viewData);
			}
		} 
		else 
		{
			viewData.msgKey="amformas.mail-verify-reply.fail";			
			cocoon.sendPage("mail-verify-reply.html",viewData);
		}
	}
	else 
	{
		viewData.msgKey="amformas.mail-verify-reply.fail-nokey";	
		cocoon.sendPage("mail-verify-reply.html",viewData);
	}
}