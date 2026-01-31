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
    rm thonk.txt
fi

# compile the code into the bin folder, terminates if error occurred
if ! javac -cp ../src/main/java -Xlint:none -d ../bin ../src/main/java/*.java
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

touch thonk.txt
# run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath ../bin Thonk < input.txt > ACTUAL.TXT
rm thonk.txt


# convert to UNIX format
cp EXPECTED.TXT EXPECTED-UNIX.TXT
dos2unix -q ACTUAL.TXT EXPECTED-UNIX.TXT

# compare the output to the expected output
echo "Normal"
diff -bB ACTUAL.TXT EXPECTED-UNIX.TXT
if [ $? -eq 0 ]
then
    echo "Test result: PASSED"
else
    echo "Test result: FAILED"
fi
