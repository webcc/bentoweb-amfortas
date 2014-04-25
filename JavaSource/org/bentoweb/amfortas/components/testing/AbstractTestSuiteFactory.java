package org.bentoweb.amfortas.components.testing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.avalon.framework.component.Component;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.bentoweb.amfortas.components.om.AbstractTestSuite;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public interface AbstractTestSuiteFactory extends Component
{
    String ROLE = AbstractTestSuiteFactory.class.getName();
    
	public abstract AbstractTestSuite generate(String testSuiteId)
			throws IOException, ParserConfigurationException, SAXException;

	public abstract Properties getTestSuiteProperties(String testSuiteId);

	public abstract InputSource resolveInputSource(String uri)
			throws ServiceException, MalformedURLException, IOException;

	public abstract String resolveUri(String uri) throws ServiceException,
			MalformedURLException, IOException;

	public abstract void parameterize(Parameters params)
			throws ParameterException;
    
}
