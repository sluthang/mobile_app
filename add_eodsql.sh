#!/bin/bash

#This adds the eodsql dependency in the /.m2/repository
mvn install:install-file -Dfile=lib/eodsql.jar  \
   -DgroupId=net.lemnik \
   -DartifactId=eodsql \
   -Dversion=2.2 \
   -Dpackaging=jar \
   -DgeneratePom=true