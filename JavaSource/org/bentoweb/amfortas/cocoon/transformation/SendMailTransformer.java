/**
 * bentoweb
 */
package org.bentoweb.amfortas.cocoon.transformation;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.avalon.excalibur.pool.Poolable;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author evlach@aegean.gr
 *
 */
public class SendMailTransformer extends AbstractTransformer implements
        Parameterizable, Poolable 
{
    
    public static final String NAMESPACE = "http://bentoweb.org/sendmail";
    public static final String HTML_NAMESPACE = "http://www.w3.org/1999/xhtml";
    
    public static final String SENDMAIL_ELEMENT = "sendmail";
    public static final String MAILTO_ELEMENT = "mailto";
    public static final String MAILSUBJECT_ELEMENT = "mailsubject";
    public static final String MAILBODY_ELEMENT = "mailbody";

    protected static final int MODE_NONE = 0;
    protected static final int MODE_TO = 1;
    protected static final int MODE_SUBJECT = 2;
    protected static final int MODE_BODY = 3;

    protected int mode;
    protected StringBuffer toAddress;
    protected StringBuffer subject;
    protected StringBuffer body;

    protected String mailHost;
    protected String fromAddress;
    protected String successLocalKey;
    protected String failLocalKey;
    protected String i18n_catalogue;
	private String ccAdmin = null;


    /* (non-Javadoc)
     * @see org.apache.avalon.framework.parameters.Parameterizable#parameterize(org.apache.avalon.framework.parameters.Parameters)
     */
    public void parameterize(Parameters parameters) throws ParameterException 
    {
        this.mailHost = parameters.getParameter("mailhost");
        this.fromAddress = parameters.getParameter("from");
        try
        {
        	this.ccAdmin = parameters.getParameter("ccAdmin");
        }
        catch(Exception e)
        {
        	
        }

        this.successLocalKey = parameters.getParameter("successLocalKey");
        this.failLocalKey = parameters.getParameter("failLocalKey");
        this.i18n_catalogue = parameters.getParameter("i18n_catalogue");                	

    }

    /* (non-Javadoc)
     * @see org.apache.cocoon.sitemap.SitemapModelComponent#setup(org.apache.cocoon.environment.SourceResolver, java.util.Map, java.lang.String, org.apache.avalon.framework.parameters.Parameters)
     */
    public void setup(SourceResolver arg0, Map arg1, String arg2,
            Parameters arg3) throws ProcessingException, SAXException,
            IOException 
    {
        this.mode = MODE_NONE;
        this.toAddress = new StringBuffer();
        this.subject = new StringBuffer();
        this.body = new StringBuffer(); 

    }

    public void startElement(String uri, String name, String raw,
            Attributes attr)
        throws SAXException 
    {
        if (this.getLogger().isDebugEnabled() == true) 
            this.getLogger().debug("BEGIN startElement uri=" + uri + ", name=" + name + ", raw=" + raw + ", attr=" + attr);
        if (uri != null && uri.equals(NAMESPACE) ) 
        { 
            if (name.equals(SENDMAIL_ELEMENT) == true) 
                ;// No need to do anything here
            else if (name.equals(MAILTO_ELEMENT) == true) 
                this.mode = MODE_TO;
            else if (name.equals(MAILSUBJECT_ELEMENT) == true) 
                this.mode = MODE_SUBJECT;
            else if (name.equals(MAILBODY_ELEMENT))
                this.mode = MODE_BODY;
            else 
                throw new SAXException("Unknown element " + name);
        } 
        else if(uri.equals(HTML_NAMESPACE))
        {
        	this.body.append("<"+name);
        	if(attr!=null && attr.getLength()>0)
        	{
        		for(int i=0;i<attr.getLength();i++)
        			this.body.append(" " + attr.getLocalName(i) + "=\"" + attr.getValue(i) +"\"");
        	}
        	this.body.append(">");
        	
        }
        else     // Not for us
            super.startElement(uri, name, raw, attr);
        
        if (this.getLogger().isDebugEnabled() == true) 
            this.getLogger().debug("END startElement");
    }
        
    public void endElement(String uri, String name, String raw)
        throws SAXException 
    {
    	if (this.getLogger().isDebugEnabled() == true) 
    	{
    		this.getLogger().debug("BEGIN endTransformingElement uri=" + uri +
                 ", name=" + name + ", raw=" + raw);
    	}
	    
	    if (uri != null && uri.equals(NAMESPACE) ) 
	    {
	    	if (name.equals(SENDMAIL_ELEMENT) == true) 
	    	{
	    		if (this.getLogger().isInfoEnabled() == true) 
	    		{
	    			this.getLogger().info("Mail contents: To: "+ this.toAddress +
	                        ", Subject: " + this.subject +
	                        ", Body: "+ this.body);
	    		}
			    String text;
			    AttributesImpl atr = new AttributesImpl();
			    try 
			    {
			       Properties props = new Properties();
			       props.put("mail.smtp.host", this.mailHost);
			       Session mailSession = Session.getInstance(props, null);
			       MimeMessage message = new MimeMessage(mailSession);
			       message.setFrom(new InternetAddress( this.fromAddress ));
			       message.setRecipients(Message.RecipientType.TO,
			                        InternetAddress.parse( this.toAddress.toString() ));
			       if(this.ccAdmin!=null)
			    	   message.setRecipients(Message.RecipientType.CC, InternetAddress.parse( this.ccAdmin ));
			       message.setSubject( this.subject.toString().trim() );
			       message.setSentDate(new Date());
	      //System.out.println(this.body.toString());
	      		   message.setContent(this.body.toString().trim(),"text/plain");
			       //message.set.sett.setText(this.body.toString().trim() );
			       Transport transport = mailSession.getTransport("smtp");
			       //transport.connect("smtp.aegean.gr", "user", "pass"); 
			       // send mail
			       Transport.send(message);	       
			       // success message
			       text = this.toAddress.toString();
			       //  	localize
			       atr.addAttribute("","","catalogue","CDATA",this.i18n_catalogue);
			       atr.addAttribute("","","key","CDATA",this.successLocalKey);       
			       
			    } 
			    catch (Exception any) 
			    {
			       this.getLogger().error("Exception during sending of mail", any);
			      //System.out.println("Exception during sending of mail"+ any.toString());//TODO:remove
			       // failure message
			       //atr.setAttribute(0,"","key","","",this.failLocalKey);
			       text = this.toAddress.toString();       
			    }
			    super.startElement("", "sendmail", "sendmail", new AttributesImpl());
			    super.startElement("http://apache.org/cocoon/i18n/2.1", "text", "i18n:text", atr); 
			   // super.characters(text.toCharArray(), 0, text.length());
			    super.endElement("http://apache.org/cocoon/i18n/2.1", "text", "i18n:text");
			    super.endElement("", "sendmail", "sendmail");    
	    	} 
	    	else if (name.equals(MAILTO_ELEMENT) == true) 
	    	{
	    		// mailto received
	    		this.mode = MODE_NONE;
	    	} 
	    	else if (name.equals(MAILSUBJECT_ELEMENT) == true) 
	    	{
	    		this.mode = MODE_NONE;
	    	} 
	    	else if (name.equals(MAILBODY_ELEMENT) == true) 
	    	{
	    		this.mode = MODE_NONE;
	    	}
	    	else 
	    	{
	    		throw new SAXException("Unknown element " + name);
	    	}
	    } 
        else if(uri.equals(HTML_NAMESPACE))
        	this.body.append("</"+name+">");	    
	    else 
	    {
	    	// Not for us
	    	super.endElement(uri, name, raw);
	    }
	    if (this.getLogger().isDebugEnabled() == true) 
	    {
	    	this.getLogger().debug("END endElement");
	    }
    }
        
   public void characters(char[] buffer, int start, int length) throws SAXException 
   {
	  //  buffer = buffer.toString().trim().toCharArray();
	  //  start = 0;
	  //  length = buffer.length;
        switch (this.mode) 
        {
        	case MODE_NONE : super.characters(buffer, start, length);
                    break;
	        case MODE_TO : this.toAddress.append(buffer, start, length);
	                  break;
	        case MODE_SUBJECT : this.subject.append(buffer, start, length);
	                  break;
	        case MODE_BODY : this.body.append(buffer, start, length);
	                  break;
        }
   }
}