package org.bentoweb.amfortas.components.testing.impl;

import java.util.Comparator;

import org.bentoweb.amfortas.components.om.TestCaseScenario;
import org.bentoweb.amfortas.components.testing.AbstractTestCaseComparator;


public class DefaultTestCaseScenarioComparator implements Comparator , AbstractTestCaseComparator
{

    public DefaultTestCaseScenarioComparator() {
        super();
    }

    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestCaseComparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) 
    {
        TestCaseScenario tcs1 = (TestCaseScenario)o1;
        TestCaseScenario tcs2 = (TestCaseScenario)o2;
        if(tcs1.getGrade()>tcs2.getGrade())
            return (-1);
        else if(tcs1.getGrade()<tcs2.getGrade())
            return 1;
        else
            return 0;
    }

    public void dispose() {
        // TODO Auto-generated method stub
        
    }

}
