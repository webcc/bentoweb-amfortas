package org.bentoweb.amfortas.components.testing;

import org.apache.avalon.framework.component.Component;

public interface AbstractTestCaseScenarioComparator extends Component 
{
    String ROLE = AbstractTestCaseScenarioComparator.class.getName();
    public int compare(Object o1, Object o2);

}