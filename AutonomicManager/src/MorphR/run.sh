#/bin/bash

cp="morphr-am.jar:.:lib/*"
javaOpts=""

export LD_LIBRARY_PATH=`pwd`

java ${javaOpts} -classpath ${cp} morphr.MorphR  


