package org.bentoweb.amfortas.components.om;

import java.util.Date;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.avalon.framework.component.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public interface TestCase extends Component
{
    String ROLE = TestCase.class.getName(); 

    public void setDom(Document doc);
    public boolean isCandidate(String statusReady);
    public String getStatus();
    public String getExpectedResult();
    public NodeList getScenarioNodeList();
    public Node getScenarioNode(String scenarioId);
    /**
     * @return Returns the date.
     */
    public Date getDate();

    /**
     * @param date The date to set.
     */
    public void setDate(Date date);


    /**
     * @return Returns the id.
     */
    public String getId();

    /**
     * @param id The id to set.
     */
    public void setId(String id);

    /**
     * @return Returns the lastModified.
     */
    public long getLastModified();

    /**
     * @param lastModified The lastModified to set.
     */
    public void setLastModified(long lastModified);
    public Node getTestCaseNode() ;
}