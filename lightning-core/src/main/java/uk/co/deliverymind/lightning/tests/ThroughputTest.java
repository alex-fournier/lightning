package uk.co.deliverymind.lightning.tests;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.co.deliverymind.lightning.data.JMeterTransactions;
import uk.co.deliverymind.lightning.enums.TestResult;

import java.util.ArrayList;

public class ThroughputTest extends ClientSideTest {

    private static final String EXPECTED_RESULT_MESSAGE = "Throughput >= %s / second";
    private static final String ACTUAL_RESULT_MESSAGE = "Throughput = %s / second";

    private final double minThroughput;

    public ThroughputTest(String name, String type, String description, String transactionName, double minThroughput) {
        super(name, type, description, transactionName);
        this.minThroughput = minThroughput;
        expectedResultDescription = String.format(EXPECTED_RESULT_MESSAGE, minThroughput);
    }

    @Override
    public void execute(ArrayList<String[]> originalJMeterTransactions) {
        try {
            JMeterTransactions transactions = filterTransactions((JMeterTransactions) originalJMeterTransactions);
            transactionCount = transactions.getTransactionCount();

            actualResult = (int) transactions.getThroughput();
            actualResultDescription = String.format(ACTUAL_RESULT_MESSAGE, actualResult);

            if (actualResult < minThroughput) {
                result = TestResult.FAIL;
            } else {
                result = TestResult.PASS;
            }
        } catch (Exception e) {
            result = TestResult.ERROR;
            actualResultDescription = e.getMessage();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
