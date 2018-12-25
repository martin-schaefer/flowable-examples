package ch.schaefer.flowable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

	private Boolean invoiceResponse;
	private boolean invoiceSubmitted;
	private int fails;
	private int attempt;

	private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

	public void submit(String invoiceNumber) {
		if (attempt < fails) {
			attempt++;
			throw new RuntimeException("Could not submit invoice " + invoiceNumber + " on attempt " + attempt);
		}
		this.invoiceSubmitted = true;
		log.info("Submitted invoice {}", invoiceNumber);
	}

	public void noInvoiceResponse(String invoiceNumber) {
		invoiceResponse = null;
		log.info("No response received for invoice {}", invoiceNumber);
	}

	public void setInvoiceResponse(Boolean invoiceResponse) {
		this.invoiceResponse = invoiceResponse;
	}

	public Boolean getInvoiceResponse() {
		return invoiceResponse;
	}

	public void setFails(int fails) {
		this.fails = fails;
	}

	public boolean isInvoiceSubmitted() {
		return invoiceSubmitted;
	}

	public void setInvoiceSubmitted(boolean invoiceSubmitted) {
		this.invoiceSubmitted = invoiceSubmitted;
	}

}
