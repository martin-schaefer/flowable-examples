package ch.schaefer.flowable.process.caseexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("caseExchangeService")
public class CaseExchangeServiceMock implements CaseExchangeService {

	private static final Logger log = LoggerFactory.getLogger(CaseExchangeServiceMock.class);

	private boolean noCaseData;
	private boolean submitCaseData;
	private boolean saveCaseData;

	@Override
	public CaseData getCaseData(String caseId) {
		if (!"invalid".equals(caseId)) {
			CaseData caseData = new CaseData(caseId, "Data for caseId=" + caseId);
			log.info("Returning caseData: {}", caseData);
			return caseData;
		}
		log.info("Returning null");
		return null;
	}

	@Override
	public void noCaseData(String caseId) {
		noCaseData = true;
		log.info("No available data for case: {}", caseId);

	}

	@Override
	public void saveCaseData(CaseData caseData) {
		saveCaseData = true;
		log.info("Save case data: {}", caseData);
	}

	@Override
	public void submitCaseData(CaseData caseData) {
		submitCaseData = true;
		log.info("Submit case data: {}", caseData);
	}

	public boolean isNoCaseData() {
		return noCaseData;
	}

	public boolean isSubmitCaseData() {
		return submitCaseData;
	}

	public boolean isSaveCaseData() {
		return saveCaseData;
	}

}
