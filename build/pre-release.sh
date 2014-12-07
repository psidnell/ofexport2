#!/bin/bash

mvn clean package
rm -rf bin
rm -rf repo
cp -a target/ofexport/bin .
cp -a target/ofexport/repo .

DATE=`date "+%Y-%m-%d"`

# The version from the pom file without the SNAPSHOT suffix
NEXT_VERSION=`grep '<version>' pom.xml | head -1 | sed -e 's/.*<version>//' -e 's/<\/version>.*//' -e 's/-SNAPSHOT//'`

echo \$VERSION=$VERSION

echo Generating README.md
cat doc/templates/README.md | sed -e "s/\$VERSION/$NEXT_VERSION/g" -e "s/\$DATE/$DATE/g" > README.md

echo Generating Options.md
echo '# Options' > doc/Options.md
ofexport2 -h | sed -e 's/^/    /' >> doc/Options.md

echo Generating Attributes.md
echo '# Attributes.md' > doc/Attributes.md
echo >> doc/Attributes.md
ofexport2 -i | sed -e 's/^/    /' >> doc/Attributes.md

echo Generating version.properties
echo "version:$NEXT_VERSION" > config/version.properties
echo "date:$DATE" >> config/version.properties

echo Generating TODO.md
ofexport2 -pn ofexport2 -ti '!completed' -o doc/TODO.md

echo Generating DONE-$DATE.md
DATE=`date "+%Y-%m"`
ofexport2 -pn ofexport2 -ti 'completed && completionDate >= date("1st")' -o doc/DONE-$DATE.md

