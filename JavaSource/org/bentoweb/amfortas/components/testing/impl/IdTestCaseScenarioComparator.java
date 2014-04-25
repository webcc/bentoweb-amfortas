package org.bentoweb.amfortas.components.testing.impl;

import java.util.Comparator;

import org.bentoweb.amfortas.components.om.TestCaseScenario;
import org.bentoweb.amfortas.components.testing.AbstractTestCaseComparator;


public class IdTestCaseScenarioComparator implements Comparator , AbstractTestCaseComparator
{

    public IdTestCaseScenarioComparator() {
        super();
    }

    /* (non-Javadoc)
     * @see org.bentoweb.amfortas.components.testing.impl.AbstractTestCaseComparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) 
    {
        TestCaseScenario tcs1 = (TestCaseScenario)o1;
        TestCaseScenario tcs2 = (TestCaseScenario)o2;
        //sc1.2.1_l1_005.xml
        String idS1 =  tcs1.getTestCaseId().substring(2, tcs1.getTestCaseId().length()-11);
        idS1 = idS1.replace('.', '0');        
        double idD1 = Double.parseDouble(idS1);
        String idS2 =  tcs2.getTestCaseId().substring(2, tcs2.getTestCaseId().length()-11);
        idS2 = idS2.replace('.', '0');        
        double idD2 = Double.parseDouble(idS2);
        
        if(tcs1.getTestCaseId().equalsIgnoreCase(tcs2.getTestCaseId()))
        	return 0;
        else
        	if(idD1>idD2)
        		return (1);
        	else
        		return (-1);
    }

    public void dispose() {
        // TODO Auto-generated method stub
        
    }

}
