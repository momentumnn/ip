#!/usr/bin/env bash

# create bin directory if it doesn't exist
if [ ! -d "../bin" ]
then
    mkdir ../bin
fi

# delete output from previous run
if [ -e "./ACTUAL.TXT" ]
then
    rm ACTUAL.TXT
fi

# compile the code into the bin folder, terminates if error occurred
if ! javac -cp ../src/main/java -Xlint:none -d ../bin ../src/main/java/*.java
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath ../bin Thonk < input.txt > ACTUAL.TXT
java -classpath ../bin Thonk < events.txt > ACTUAL_EVENTS.TXT
java -classpath ../bin Thonk < delete.txt > ACTUAL_DELETE.TXT


# convert to UNIX format
cp EXPECTED.TXT EXPECTED-UNIX.TXT
cp EXPECTED_EVENTS.TXT EXPECTED_EVENTS-UNIX.TXT
cp EXPECTED_DELETE.TXT EXPECTED_DELETE-UNIX.TXT
dos2unix -q ACTUAL.TXT EXPECTED-UNIX.TXT ACTUAL_DELETE.TXT ACTUAL_EVENTS.TXT EXPECTED_EVENTS-UNIX.TXT EXPECTED_DELETE-UNIX.TXT

# compare the output to the expected output
diff -bB ACTUAL.TXT EXPECTED-UNIX.TXT
echo "Normal"
if [ $? -eq 0 ]
then
    echo "Test result: PASSED"
else
    echo "Test result: FAILED"
fi
diff -bB ACTUAL_DELETE.TXT EXPECTED_DELETE-UNIX.TXT
echo "Delete"
if [ $? -eq 0 ]
then
    echo "Test result: PASSED"
else
    echo "Test result: FAILED"
fi
diff -bB ACTUAL_EVENTS.TXT EXPECTED_EVENTS-UNIX.TXT
echo "Events"
if [ $? -eq 0 ]
then
    echo "Test result: PASSED"
else
    echo "Test result: FAILED"
fi
