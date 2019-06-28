package ch.schaefer.flowable.process.caseexchange;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.springframework.stereotype.Service;

@Service
public class CaseExchangeProcessStarterImpl implements CaseExchangeProcessStarter {

	private final RuntimeService runtimeService;

	private static final String CASE_EXCHANGE_PROCESS = "CaseExchangeProcess";
	private static final String CASE_ID = "caseId";
	private static final String CASE_CLOSED_MESSAGE = "caseClosedMessage";

	public CaseExchangeProcessStarterImpl(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	@Override
	public boolean isRunning(String caseId) {
		return runtimeService.createProcessInstanceQuery().processDefinitionKey(CASE_EXCHANGE_PROCESS)
				.processInstanceBusinessKey(caseId).count() > 0;
	}

	@Override
	public void start(String caseId) {
		runtimeService.createProcessInstanceBuilder().processDefinitionKey(CASE_EXCHANGE_PROCESS).businessKey(caseId)
				.variable(CASE_ID, caseId).start();
	}

	@Override
	public boolean fireCaseClosedEvent(String caseId) {
		Execution execution = runtimeService.createExecutionQuery().processInstanceBusinessKey(caseId, true)
				.messageEventSubscriptionName(CASE_CLOSED_MESSAGE).singleResult();

		// fire the caseClosedMessage event to the execution
		if (execution != null) {
			runtimeService.messageEventReceived(CASE_CLOSED_MESSAGE, execution.getId());
			return true;
		}
		return false;
	}

}
