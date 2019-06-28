package ch.schaefer.flowable.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.schaefer.flowable.process.caseexchange.CaseExchangeProcessStarter;
import ch.schaefer.flowable.process.caseexchange.CaseExchangeServiceMock;

/**
 * Test cases for the SubmitInvoiceProcess.
 * 
 * @author Martin Schäfer
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CaseExchangeProcessTest {

	@Autowired
	private CaseExchangeProcessStarter caseExchangeProcessStarter;

	@Autowired
	private CaseExchangeServiceMock caseExchangeServiceMock;

	@Test
	public void executeCaseExchangeProcess_withValidCaseId_shouldSubmitCase() {
		// given
		String caseId = "001";

		// when
		executeCaseExchangeProces(caseId);

		// --- then
		assertThat(caseExchangeProcessStarter.isRunning(caseId)).isFalse();
		assertThat(caseExchangeServiceMock.isNoCaseData()).isFalse();
		assertThat(caseExchangeServiceMock.isSaveCaseData()).isTrue();
		assertThat(caseExchangeServiceMock.isSubmitCaseData()).isTrue();
	}

	@Test
	public void executeCaseExchangeProcess_withInvalidCaseId_shouldNotSubmitCase() {

		// given
		String caseId = "invalid";

		// when
		executeCaseExchangeProces(caseId);

		// --- then
		assertThat(caseExchangeProcessStarter.isRunning(caseId)).isFalse();
		assertThat(caseExchangeServiceMock.isNoCaseData()).isTrue();
		assertThat(caseExchangeServiceMock.isSaveCaseData()).isFalse();
		assertThat(caseExchangeServiceMock.isSubmitCaseData()).isFalse();
	}

	public void executeCaseExchangeProces(String caseId) {
		caseExchangeServiceMock.reset();

		// --- given: caseId
		assertThat(caseExchangeProcessStarter.isRunning(caseId)).isFalse();

		// --- when
		caseExchangeProcessStarter.start(caseId);

		// Let some time pass before the case is closed
		sleep(500);

		// Find the execution waiting for the caseClosedMessage
		caseExchangeProcessStarter.fireCaseClosedEvent(caseId);

		sleep(100);

	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
