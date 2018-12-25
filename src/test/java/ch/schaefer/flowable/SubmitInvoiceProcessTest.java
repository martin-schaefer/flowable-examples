package ch.schaefer.flowable;

import static java.util.Collections.singletonMap;
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

	@Autowired
	private InvoiceService invoiceService;

	@Test
	public void executeSubmitInvoiceProcess_withTwoInitialFails_shouldReceiveInviceResponse() {

		// --- given
		String invoiceNumber = "R1020304050";
		invoiceService.setInvoiceSubmitted(false);
		invoiceService.setInvoiceResponse(null);
		invoiceService.setFails(2);

		// --- when
		ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder().processDefinitionKey("SubmitInvoiceProcess").businessKey(invoiceNumber)
				.variable("invoiceNumber", invoiceNumber).name("Submit Invoice " + invoiceNumber).start();

		// wait until invoice was submitted
		do {
			sleep(100);
		} while (!invoiceService.isInvoiceSubmitted());

		// Let some time pass before the invoiceResponseMessage arrives
		sleep(1000);

		// Find the execution waiting for the invoiceResponseMessage
		Execution execution = runtimeService.createExecutionQuery().processInstanceBusinessKey(invoiceNumber, true)
				.messageEventSubscriptionName("invoiceResponseMessage").singleResult();

		// fire the invoiceResponseMessage event to the execution
		runtimeService.messageEventReceived("invoiceResponseMessage", execution.getId(), singletonMap("invoiceResponse", true));

		// let the process terminate
		sleep(100);

		// --- then

		// assert that the process is terminated and the invoiceResponseMessage was
		// saved
		assertThat(runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult()).isNull();
		assertThat(invoiceService.isInvoiceSubmitted()).isTrue();
		assertThat(invoiceService.getInvoiceResponse()).isTrue();
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
