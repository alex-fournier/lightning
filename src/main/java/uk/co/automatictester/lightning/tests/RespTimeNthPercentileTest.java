package uk.co.automatictester.lightning.tests;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import uk.co.automatictester.lightning.JMeterTransactions;
import uk.co.automatictester.lightning.utils.IntToOrdConverter;

import java.text.DecimalFormat;
import java.util.List;

public class RespTimeNthPercentileTest extends Test {

    private static final String MESSAGE = "%s percentile of transactions have response time ";
    private static final String EXPECTED_RESULT_MESSAGE = MESSAGE + "<= %s";
    private static final String ACTUAL_RESULT_MESSAGE = MESSAGE + "= %s";

    private final double maxRespTime;
    private final int percentile;

    public RespTimeNthPercentileTest(String name, String description, String transactionName, int percentile, double maxRespTime) {
        super(name, description, transactionName);
        this.maxRespTime = maxRespTime;
        this.percentile = percentile;
        expectedResult = String.format(EXPECTED_RESULT_MESSAGE, IntToOrdConverter.convert(percentile), maxRespTime);
    }

    public void execute(JMeterTransactions originalJMeterTransactions) {
        try {
            JMeterTransactions transactions = originalJMeterTransactions.excludeLabelsOtherThan(transactionName);

            DescriptiveStatistics ds = new DescriptiveStatistics();
            for (List<String> transaction : transactions) {
                String elapsed = transaction.get(1);
                ds.addValue(Double.parseDouble(elapsed));
            }
            double actualRespTimePercentile = ds.getPercentile((double) percentile);
            DecimalFormat df = new DecimalFormat("#.##");
            double roundedActualRespTimePercentile = Double.valueOf(df.format(actualRespTimePercentile));

            actualResult = String.format(ACTUAL_RESULT_MESSAGE, IntToOrdConverter.convert(percentile), roundedActualRespTimePercentile);
            passed = !(roundedActualRespTimePercentile > maxRespTime);
            failed = (roundedActualRespTimePercentile > maxRespTime);
        } catch (Exception e) {
            error = true;
            actualResult = e.getMessage();
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof RespTimeNthPercentileTest) {
            RespTimeNthPercentileTest test = (RespTimeNthPercentileTest) obj;
            return name.equals(test.name) &&
                    description.equals(test.description) &&
                    transactionName.equals(test.transactionName) &&
                    expectedResult.equals(test.expectedResult) &&
                    actualResult.equals(test.actualResult) &&
                    failed == test.failed &&
                    maxRespTime == test.maxRespTime &&
                    percentile == test.percentile;
        } else {
            return false;
        }
    }
}