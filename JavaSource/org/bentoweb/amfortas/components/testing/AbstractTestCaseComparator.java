package org.bentoweb.amfortas.components.testing;

import org.apache.avalon.framework.component.Component;

public interface AbstractTestCaseComparator extends Component 
{
    String ROLE = AbstractTestCaseComparator.class.getName();
    public int compare(Object o1, Object o2);

}