package org.bentoweb.amfortas.components.om;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.avalon.framework.component.Component;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public interface RuleSet extends Component 
{
    String ROLE = RuleSet.class.getName();   
    public Element initRules(File rulesFile) throws ParserConfigurationException, SAXException, IOException, TransformerException;
    public Element initRules(String rulesFilePath) throws ParserConfigurationException, SAXException, IOException, TransformerException;
    public boolean isLoaded();
	//public Properties getProperties();
    public Element getRulesSetElement();
}
