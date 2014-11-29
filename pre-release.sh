#!/bin/bash

# The version from the pom file without the SNAPSHOT suffix
NEXT_VERSION=`grep version pom.xml | head -1 | sed -e 's/.*<version>//' -e 's/<\/version>.*//' -e 's/-SNAPSHOT//'`

# Fix version in README
cat doc/templates/README.md | sed -e "s/\$VERSION/$NEXT_VERSION/g" > README.md

# Create a properties file with the version
echo "version:$NEXT_VERSION" > src/main/resources/version.properties