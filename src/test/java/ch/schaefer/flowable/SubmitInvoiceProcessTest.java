package ch.schaefer.flowable;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test cases for the SubmitInvoiceProcess.
 * 
 * @author Martin Schäfer
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SubmitInvoiceProcessTest {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void executeSubmitInvoiceProcess_withTwoInitialFails_shouldReceiveInvoiceResponse() {

		// --- given
		Invoice invoice = new Invoice();
		invoice.setInvoiceNumber("R1020304050");
		invoice.setAmount(new BigDecimal("245.00"));
		invoice.setInvoiceDate(LocalDate.of(2019, 7, 31));
		invoice.setRemarks("remarks");
		invoiceService.clear();
		invoiceService.setFails(2);

		// --- when
		ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder().processDefinitionKey("SubmitInvoiceProcess")
				.businessKey(invoice.getInvoiceNumber()).variable("invoice", objectMapper.convertValue(invoice, JsonNode.class))
				.name("Submit Invoice " + invoice.getInvoiceNumber()).start();

		// wait until invoice was submitted
		do {
			sleep(100);
		} while (!invoiceService.isInvoiceSubmitted());

		// Let some time pass before the invoiceResponseMessage arrives
		sleep(500);

		// Find the execution waiting for the invoiceResponseMessage
		Execution execution = runtimeService.createExecutionQuery().processInstanceBusinessKey(invoice.getInvoiceNumber(), true)
				.messageEventSubscriptionName("invoiceResponseMessage").singleResult();

		// fire the invoiceResponseMessage event to the execution
		runtimeService.messageEventReceived("invoiceResponseMessage", execution.getId(),
				singletonMap("invoiceResponse", objectMapper.convertValue(new InvoiceResponse(true, "message"), JsonNode.class)));

		// wait until the process terminates
		do {
			sleep(100);
		} while (runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count() != 0);

		// --- then

		// assert that the InvoiceResponseMessage was saved
		assertThat(invoiceService.isInvoiceSubmitted()).isTrue();
		assertThat(invoiceService.getInvoiceResponse().isOk()).isTrue();
		assertThat(invoiceService.getInvoiceResponse().getMessage()).isEqualTo("message");
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
