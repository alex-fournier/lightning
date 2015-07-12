#!/bin/bash

mkdir -p src/test/resources/results/actual/

EXPECTED_RESULT="src/test/resources/results/expected/respTimeStdDevTest.txt"
ACTUAL_RESULT="src/test/resources/results/actual/respTimeStdDevTest.txt"

java \
    -jar target/lightning*.jar \
    -xml src/test/resources/xml/respTimeStdDevTest.xml \
    -csv src/test/resources/csv/10_transactions.csv \
    | grep -v "Execution time:" > $ACTUAL_RESULT

DIFF_OUTPUT=`diff $EXPECTED_RESULT $ACTUAL_RESULT`
OUT=$?

echo "CONSOLE OUTPUT TEST"
if [ $OUT -eq 0 ];then
    echo "OUTPUT AS EXPECTED"
    echo "TEST PASSED"
    exit 0
else
    echo "INCORRECT CONSOLE OUTPUT - DIFF:"
    echo $DIFF_OUTPUT
    echo "TEST FAILED"
    exit 1
fi