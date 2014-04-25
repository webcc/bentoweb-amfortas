package org.bentoweb.amfortas.components.hibernate;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.logger.CommonsLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.commons.logging.LogFactory;
import org.bentoweb.amfortas.components.persistence.PersistenceFactory;
import org.bentoweb.amfortas.util.Strings;

public class HibernateFactory extends AbstractLogEnabled implements
        PersistenceFactory, Configurable, Serviceable, Initializable,
        Disposable, ThreadSafe
{

    private boolean initialized = false;
    private boolean disposed = false;
    private ServiceManager manager = null;
    
    // Creates the logger
    private static Logger log = new CommonsLogger(
            LogFactory.getLog(HibernateFactory.class), "");

    // do not confuse with Avalon Configuration, Cocoon Session, etc.
    org.hibernate.cfg.Configuration cfg;

    org.hibernate.SessionFactory sf;

    // for debugging: which instance am I using?
    long time_start;

    public HibernateFactory()
    {
        super();
        enableLogging(log);
        if (log.isInfoEnabled())
        {
            log.info(Strings.INFO_FACTORY_CREATED);
        }
        System.out.println(Strings.INFO_FACTORY_CREATED);
    }

    public final void configure(Configuration conf)
            throws ConfigurationException
    {
        if (initialized || disposed)
        {
            throw new IllegalStateException("Illegal call");
        }
        if (log.isInfoEnabled())
        {
            log.info(Strings.INFO_CONFIG_CALLED);
        }
        System.out.println(Strings.INFO_CONFIG_CALLED);
    }

    public final void service(ServiceManager smanager) throws ServiceException
    {
        if (initialized || disposed)
        {
            throw new IllegalStateException("Illegal call");
        }

        if (null == this.manager)
        {
            this.manager = smanager;
        }
        if (log.isInfoEnabled())
        {
            log.info(Strings.INFO_SERVICE_CALLED);
        }
        System.out.println(Strings.INFO_SERVICE_CALLED);
    }

    public final void initialize() throws Exception
    {
        if (null == this.manager)
        {
            throw new IllegalStateException("Not Composed");
        }

        if (disposed)
        {
            throw new IllegalStateException("Already disposed");
        }

        // adapt:
        // build sessionfactory
        // map all classes we need
        // keep in sync with configuration file
        //
        try
        {
            cfg = new org.hibernate.cfg.Configuration();

            // Make sure the corresponding .class and .hbm.xml files are located
            // in
            // (the same directory of) your classpath (e.g. WEB-INF/classes)
            // sf = cfg.buildSessionFactory();
            sf = cfg.configure().buildSessionFactory();
        }
        catch (Exception e)
        {
            log.error("Hibernate:" + e.getMessage());
            return;
        }
        this.initialized = true;
        if (log.isInfoEnabled())
        {
            log.info(Strings.INFO_INIT_CALLED);
        }
        System.out.println(Strings.INFO_INIT_CALLED);
    }

    public final void dispose()
    {
        //
        try
        {
            sf.close();
        }
        catch (Exception e)
        {
            log.error("Hibernate:" + e.getMessage());
        }
        finally
        {
            sf = null;
            cfg = null;
        }
        this.disposed = true;
        this.manager = null;
        if (log.isInfoEnabled())
        {
            log.info(Strings.INFO_DISPOSED);
        }
        System.out.println(Strings.INFO_DISPOSED);
    }

    public org.hibernate.Session createSession()
    {
        org.hibernate.Session hs;
        DataSourceComponent datasource = null;

        // When creating a session, use a connection from
        // cocoon's connection pool
        try
        {
            // Select the DataSourceComponent named "test"
            // This is a normal SQL connection configured in cocoon.xconf
            ServiceSelector dbselector = (ServiceSelector) manager
                    .lookup(DataSourceComponent.ROLE + "Selector");
            datasource = (DataSourceComponent) dbselector.select("amfortas");
            // name as defined in cocoon.xconf
            hs = sf.openSession(datasource.getConnection());
        }
        catch (Exception e)
        {
            log.error("Hibernate: " + e.getMessage());
            hs = null;
        }
        return hs;
    }
}