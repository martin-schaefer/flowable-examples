package ch.schaefer.flowable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The InvoiceService provides the methods invoked by the SubmitInvoiceProcess. It is not a real-word service but rather a mock to test the process flow.
 * 
 * @author Martin Schäfer
 */
@Service
public class InvoiceService {

	private InvoiceResponse invoiceResponse;
	private boolean invoiceSubmitted;
	private int fails;
	private int attempt;

	@Autowired
	private ObjectMapper objectMapper;

	private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

	public void submit(JsonNode invoiceNode) {
		Invoice invoice = objectMapper.convertValue(invoiceNode, Invoice.class);
		if (attempt < fails) {
			attempt++;
			throw new RuntimeException("Could not submit invoice " + invoice.getInvoiceNumber() + " on attempt " + attempt);
		}
		this.invoiceSubmitted = true;
		log.info("Submitted invoice {}", invoice.getInvoiceNumber());
	}

	public void noInvoiceResponse(String invoiceNumber) {
		invoiceResponse = null;
		log.info("No response received for invoice {}", invoiceNumber);
	}

	public void receive(JsonNode invoiceResponseNode) {
		this.invoiceResponse = objectMapper.convertValue(invoiceResponseNode, InvoiceResponse.class);
	}

	public InvoiceResponse getInvoiceResponse() {
		return invoiceResponse;
	}

	public void clear() {
		this.attempt = 0;
		this.fails = 0;
		this.invoiceSubmitted = false;
		this.invoiceResponse = null;
	}

	public void setFails(int fails) {
		this.fails = fails;
	}

	public boolean isInvoiceSubmitted() {
		return invoiceSubmitted;
	}

}
