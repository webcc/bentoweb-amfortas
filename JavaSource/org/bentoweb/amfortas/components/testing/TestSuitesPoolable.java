package org.bentoweb.amfortas.components.testing;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.avalon.framework.component.Component;
import org.bentoweb.amfortas.components.om.AbstractTestSuite;

public interface TestSuitesPoolable extends Component 
{
    String ROLE = TestSuitesPoolable.class.getName();  
    public ConcurrentHashMap getActiveTestSuites();
    public AbstractTestSuite getTestSuite(String tsId)
            throws Exception;
    public boolean isInitialized();
    public void putTestSuite(AbstractTestSuite ts);
    public void initialize() throws Exception;
    public void recycle();
}