package ch.schaefer.flowable;

/**
 * The InvoiceResponse is fired as message on the SubmitInvoiceProcess and used as JSON process variable.
 * 
 * @author Martin Schäfer
 */
@JsonVariable
public class InvoiceResponse {

	private boolean ok;
	private String message;

	public InvoiceResponse() {
	}

	public InvoiceResponse(boolean ok, String message) {
		this.ok = ok;
		this.message = message;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
