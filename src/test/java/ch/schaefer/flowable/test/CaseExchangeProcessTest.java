package ch.schaefer.flowable.test;

import org.flowable.engine.ManagementService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.schaefer.flowable.process.caseexchange.CaseExchangeService;

/**
 * Test cases for the SubmitInvoiceProcess.
 * 
 * @author Martin Schäfer
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CaseExchangeProcessTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private ManagementService managementService;

	@Autowired
	private CaseExchangeService caseExchangeService;

	@Test
	public void executeCaseExchangeProcess_withValidCaseId_shouldSubmitCase() {

		executeCaseExchangeProces("001");
	}

	@Test
	public void executeCaseExchangeProcess_withInvalidCaseId_shouldNotSubmitCase() {

		executeCaseExchangeProces("invalid");
	}

	public void executeCaseExchangeProces(String caseId) {

		// --- given: caseId

		// --- when
		ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
				.processDefinitionKey("CaseExchangeProcess").businessKey(caseId).variable("caseId", caseId).start();

		waitForGetCaseDataTaskCompletion(processInstance);

		// Let some time pass before the case is closed
		sleep(500);

		// Find the execution waiting for the caseClosedMessage
		Execution execution = runtimeService.createExecutionQuery().processInstanceBusinessKey(caseId, true)
				.messageEventSubscriptionName("caseClosedMessage").singleResult();

		// fire the caseClosedMessage event to the execution
		if (execution != null) {
			runtimeService.messageEventReceived("caseClosedMessage", execution.getId());
		}
		// wait until the process terminates
		do {
			sleep(100);
		} while (runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count() != 0);

		// --- then

	}

	private void waitForGetCaseDataTaskCompletion(ProcessInstance processInstance) {
		do {
			sleep(200);
		} while (runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list().stream()
				.filter(e -> "getCaseDataTask".equals(e.getActivityId())).findFirst().isPresent());
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
