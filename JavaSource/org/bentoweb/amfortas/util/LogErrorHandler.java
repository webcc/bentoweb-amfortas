package org.bentoweb.amfortas.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Jochen Welle
 * @since 16.12.2005
 *
 */
public class LogErrorHandler implements ErrorHandler
{

    private static Log log = LogFactory.getLog(LogErrorHandler.class);

    // TODO set class for log to class using this error handler?
    // public LogErrorHandler(Class arg0)

    public LogErrorHandler()
    {
    }

    public void warning(SAXParseException arg0) throws SAXException
    {
        log.warn(arg0.getLocalizedMessage());
    }

    public void error(SAXParseException arg0) throws SAXException
    {
        log.error(arg0.getLocalizedMessage());
    }

    public void fatalError(SAXParseException arg0) throws SAXException
    {
        log.fatal(arg0.getLocalizedMessage());
    }

}
