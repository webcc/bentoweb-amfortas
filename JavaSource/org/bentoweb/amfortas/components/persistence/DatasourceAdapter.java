package org.bentoweb.amfortas.components.persistence;

import java.util.List;

import org.apache.avalon.framework.component.Component;
import org.bentoweb.amfortas.components.om.AbstractTestSuite;
import org.bentoweb.amfortas.hibernate.om.TestProfile;
import org.bentoweb.amfortas.hibernate.om.TestSuite;
import org.bentoweb.amfortas.hibernate.om.TestcasePool;

public interface DatasourceAdapter  extends Component {
	String ROLE = DatasourceAdapter.class.getName();
	public int getCountCreditForProfileNTestSuite(TestProfile tp, TestSuite ts);
	public int getCountTODOForProfileNTestSuite(TestProfile tp, TestSuite ts);
	public List<TestcasePool> getCreditForProfileNTestSuite(TestProfile tp, TestSuite ts);
	public List<TestcasePool> getTODOForProfileNTestSuite(TestProfile tp, TestSuite ts);
	
	public List<TestSuite> getAssignedTestSuites(TestProfile tp);
	public TestProfile getTestProfile(String tpid);
	public List<TestProfile> getTestProfiles(String userId);
	public TestProfile getActiveTestProfile(String userId);
	public void activateTestProfile(TestProfile tp);
	public boolean canDeleteTestProfile(TestProfile tp);
	public boolean cloneTestProfile(String tpid);
	public abstract List select_TestProfile_where_TpId(int testProfileId);
	public List select_User_where_userpass(String username, String password);
	public List select_UserHasRole_where_Username(String username);
	public List select_UserUsesDevice_where_TpId(int testProfileId);
	public List select_UserUsesUserAgent_where_TpId(int testProfileId);
	public List select_ATtypes_where_TpIda(int testProfileId);
	public List select_AT_where_TpId(int testProfileId);
	public List select_UserProfileHasDisability_where_UserProfileId(int userProfileId);
	public List select_Testsuite_where_ActiveNId(int testSuiteId);
    public boolean hasTestCaseSet(int testProfileId, int testSuiteId);
    public List getActiveTestSuites();
    
    public boolean hasTestProfile(int userId);
    public void setTestCaseFinished(int pool_id);
    public void setTestCaseStarted(int pool_id);
    public List getAllocatedTestCasesScenariosList(Integer testSuiteId, Integer testProfileId);
    public List getAllocatedTestCasesScenariosList(Integer testSuiteId, Integer testProfileId, boolean finished);
    public List getAllocatedTestcasePoolsList(Integer testSuiteId, Integer testProfileId);
    public List getNotAllocatedTestCasesScenariosList(AbstractTestSuite testSuite, int testProfileId); 
    public List newTestCasePool(TestProfile tp, List testCaseScenarios, AbstractTestSuite testSuite);
}