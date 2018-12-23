package ch.schaefer.flowable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

	private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

	private int failIndex;

	public void submit(String invoiceNumber) throws InterruptedException {
		Thread.sleep(10000);
		failIndex++;
		if (failIndex % 2 == 0) {
			throw new RuntimeException("Could not submit invoice " + invoiceNumber);
		}
		log.info("Submitted invoice {}", invoiceNumber);
	}
}
