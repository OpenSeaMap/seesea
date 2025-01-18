#!/bin/bash

# mvn -f net.sf.seesea.aggregator/pom.xml -Dmaven.repo.local=/.repository -DskipClean install
mvn -f net.sf.seesea.aggregator/pom.xml -Dmaven.repo.local=/.repository -X clean install

# mvn -f net.sf.seesea.aggregator/pom.xml -Dmaven.repo.local=/.repository -X compile
# mvn -f net.sf.seesea.aggregator/pom.xml -Dmaven.repo.local=/.repository -X package
