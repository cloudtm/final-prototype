#!/bin/bash

WORKING_DIR=`cd $(dirname $0); pwd`
SRC=${WORKING_DIR}/src
EXAMPLES=${WORKING_DIR}/demos
PROJECTS="JGroups Infinispan Fenix-Framework Hibernate-Search Hibernate-OGM"
EX="Demo1 Demo2 Demo3 Demo4 Demo5 Demo6 Demo7 Demo8"

for project in ${PROJECTS}; do
cd ${SRC}/${project};
mvn clean
cd -;
done;

rm -r ${WORKING_DIR}/bin/* 2>/dev/null

for project in ${EX}; do
cd ${EXAMPLES}/${project}
mvn clean
cd -
done

rm -r ${EXAMPLES}/bin/* 2>/dev/null

exit 0
