package ch.schaefer.flowable;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The Invoice is used as JSON process variable for the SubmitInvoiceProcess.
 * 
 * @author Martin Schäfer
 */
@JsonVariable
public class Invoice {

	private String invoiceNumber;
	private BigDecimal amount;
	private LocalDate invoiceDate;
	private String remarks;

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
