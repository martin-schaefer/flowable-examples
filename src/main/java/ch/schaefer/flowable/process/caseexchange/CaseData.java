package ch.schaefer.flowable.process.caseexchange;

import java.io.Serializable;

public class CaseData implements Serializable {

	private String id;

	private String data;

	public CaseData() {
	}

	public CaseData(String id, String data) {
		this.id = id;
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CaseData [id=" + id + ", data=" + data + "]";
	}

}
