package ch.schaefer.flowable.process.caseexchange;

public interface CaseExchangeProcessStarter {

	boolean isRunning(String caseId);

	void start(String caseId);

	boolean fireCaseClosedEvent(String caseId);

}
