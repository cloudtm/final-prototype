mvn install:install-file -Dfile=flanagan-1.0.0.jar -DgroupId=flanagan \
    -DartifactId=flanagan -Dversion=1.0.0 -Dpackaging=jar

mvn install:install-file -Dfile=jama-1.0.2.jar -DgroupId=jama \
    -DartifactId=jama -Dversion=1.0.2 -Dpackaging=jar

mvn install:install-file -Dfile=tas-2.0.0.jar -DgroupId=eu.cloudtm \
    -DartifactId=tas -Dversion=2.0.0 -Dpackaging=jar
    
mvn install:install-file -Dfile=WPMConnector.jar -DgroupId=eu.cloudtm \
    -DartifactId=WPMConnector -Dversion=1.0.0 -Dpackaging=jar
    
    
        
