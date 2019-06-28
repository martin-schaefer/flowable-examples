package ch.schaefer.flowable.process.caseexchange;

public interface CaseExchangeService {

	CaseData getCaseData(String caseId);

	void noCaseData(String caseId);

	void saveCaseData(CaseData caseData);

	void submitCaseData(CaseData caseData);

}
