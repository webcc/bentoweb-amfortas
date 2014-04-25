package org.bentoweb.amfortas.components.om;

import org.apache.avalon.framework.component.Component;

public interface AbstractTestProfile extends Component
{
    String ROLE = AbstractTestProfile.class.getName();
    public Object getTestProfile() throws NullPointerException;
    public void setTestProfile(Object testProfile);
}
