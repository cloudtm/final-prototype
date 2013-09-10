#!/bin/bash

mvn clean compile assembly:single
cp target/autonomicManager-1.0-SNAPSHOT-jar-with-dependencies.jar .
#java -Djava.library.path="lib/cubist/jni" -cp .:lib/*:conf/* -jar autonomicManager-1.0-SNAPSHOT-jar-with-dependencies.jar
java -cp .:lib/*:conf/* -jar autonomicManager-1.0-SNAPSHOT-jar-with-dependencies.jar

