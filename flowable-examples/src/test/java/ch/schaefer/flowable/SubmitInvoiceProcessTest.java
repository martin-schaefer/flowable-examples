package ch.schaefer.flowable;

import static org.assertj.core.api.Assertions.assertThat;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SubmitInvoiceProcessTest {

	@Autowired
	private RuntimeService runtimeService;

	@Test
	public void startSubmitInvoiceProcess() throws InterruptedException {

		// --- given
		String invoiceNumber = "R1020304050";

		// --- when
		ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
				.processDefinitionKey("SubmitInvoiceProcess").businessKey(invoiceNumber)
				.variable("invoiceNumber", invoiceNumber).name("Submit Invoce " + invoiceNumber).start();

		// --- then

		// wait for the async submitInvoiceTask to end
		do {
			sleep(100);
		} while (runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
				.activityId("submitInvoiceTask").singleResult() != null);

		sleep(1000);
		assertThat(runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(invoiceNumber).singleResult())
				.isNotNull();

		// Find the execution waiting for the invoiceResponseMessage
		Execution execution = runtimeService.createExecutionQuery().processInstanceBusinessKey(invoiceNumber, true)
				.messageEventSubscriptionName("invoiceResponseMessage").singleResult();

		sleep(1000);

		// fire the invoiceResponseMessage event
		runtimeService.messageEventReceived("invoiceResponseMessage", execution.getId());

		// wait for the process to end
		do {
			sleep(100);
		} while (runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId())
				.singleResult() != null);

	}

	private void sleep(long millis) throws InterruptedException {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
