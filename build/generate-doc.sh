#!/bin/bash

DATE=`date "+%Y-%m-%d"`

# The version from the pom file without the SNAPSHOT suffix
NEXT_VERSION=`grep '<version>' pom.xml | head -1 | sed -e 's/.*<version>//' -e 's/<\/version>.*//' -e 's/-SNAPSHOT//'`

echo \$NEXT_VERSION=$NEXT_VERSION

echo Generating Doc
for TEMPLATE in `ls doc/templates/*.md`
do
        FILE=`echo $TEMPLATE | sed -e 's/\/templates//'`
        cat doc/templates/$TEMPLATE | sed -e "s/\$VERSION/$NEXT_VERSION/g" -e "s/\$DATE/$DATE/g" > $FILE
done
mv doc/README.md README.md

echo Generating Options.md
echo '# Options' > doc/Options.md
echo >> doc/Options.md
ofexport2 -h | sed -e 's/^/    /' >> doc/Options.md

echo Generating Attributes.md
echo '# Attributes.md' > doc/Attributes.md
echo >> doc/Attributes.md
ofexport2 -i | sed -E -e 's/^[PFCT]/## &/' -e 's/( +[a-zA-Z]+ )([a-zA-Z]+)/-\1\*\*\2\*\*/' -e 's/ +/ /g' >> doc/Attributes.md

echo Generating version.properties
echo "version:$NEXT_VERSION" > config/version.properties
echo "date:$DATE" >> config/version.properties
