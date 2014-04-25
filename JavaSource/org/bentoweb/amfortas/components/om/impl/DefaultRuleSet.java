package org.bentoweb.amfortas.components.om.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.avalon.excalibur.pool.Poolable;
import org.apache.avalon.excalibur.pool.Recyclable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.components.ContextHelper;
import org.apache.cocoon.environment.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.bentoweb.amfortas.components.om.RuleSet;
import org.bentoweb.amfortas.components.testing.impl.Constants4Mapping;
import org.bentoweb.amfortas.util.LogErrorHandler;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DefaultRuleSet implements RuleSet, Contextualizable, Initializable,Serviceable, Poolable, Recyclable  
{
	private static Log log = LogFactory.getLog(DefaultRuleSet.class);

	private Element rulesSetElement = null;
	private String mappingFileUri = null;
	private boolean loaded = false; 	
    private ServiceManager manager = null;
    
    public void contextualize(Context context) throws ContextException 
    {
    	Request r = null;
    	try
    	{
    		r = ContextHelper.getRequest(context);
            this.mappingFileUri = (String) r.getAttribute(Constants4Mapping.CONTEXT_PARAM_RULESFILE);     
            if(log.isDebugEnabled())
                log.debug("Amml rules got param from request context" + this.mappingFileUri);
    	}
        catch(Exception e)
        {
        	
        }
    }

    public void initialize() throws Exception 
    {
    	if(this.mappingFileUri==null)
    		return;
        Source source = null;
        SourceResolver sourceResolver = null;
        try 
        {
            sourceResolver = (SourceResolver)this.manager.lookup(SourceResolver.ROLE);            
            source = sourceResolver.resolveURI(this.mappingFileUri);
            this.mappingFileUri = source.getURI().substring(source.getScheme().length()+2);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        finally 
        {
            if (source != null)
                sourceResolver.release(source);
            if (sourceResolver != null)
                this.manager.release(sourceResolver);
        }          
        if(this.mappingFileUri==null)
        {
            log.fatal("Rules path has not been initialized");
            throw new Exception("Rules path has not been initialized");
        }
        this.rulesSetElement = this.initRules(this.mappingFileUri);   
        if(log.isDebugEnabled())
            log.debug("Amml rules loaded from file:" + mappingFileUri + " :" + this.rulesSetElement.toString());
    }
    
	public Element initRules(File rulesFile) throws ParserConfigurationException, SAXException, IOException, TransformerException 
	{
		//init
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        docBuilder.setErrorHandler(new LogErrorHandler());
        //System.out.println("the file: " +  rulesFile);
        Element rulesSetElement = docBuilder.parse(rulesFile).getDocumentElement();            
        if(log.isDebugEnabled())
            log.debug("Amml rules loaded from file:" + rulesFile + " :" + this.rulesSetElement.toString());
		return rulesSetElement;
	}
  
	public Element getRulesSetElement()
	{        
		return this.rulesSetElement;
	}
	public boolean isLoaded() 
	{
		return this.loaded;
	}

    public void recycle() 
    {
        this.rulesSetElement = null;
        this.mappingFileUri=null;
    }

    public Element initRules(String rulesFilePath) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        return this.initRules(new File(rulesFilePath));
    }

    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
        
    }

}
