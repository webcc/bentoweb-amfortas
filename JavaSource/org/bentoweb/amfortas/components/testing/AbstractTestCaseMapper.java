/**
 * 
 */
package org.bentoweb.amfortas.components.testing;

import java.util.List;

import org.apache.avalon.framework.component.Component;

/**
 * @author evlach
 *
 */
public interface AbstractTestCaseMapper  extends Component 
{
	String ROLE = AbstractTestCaseMapper.class.getName();
    /*
     * AbstractTestSuite  testSuite, AbstractTestProfile testProfile 
     * need to be passed in request context 
     */      
	public List doMap();
}
