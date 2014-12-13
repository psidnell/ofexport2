#!/bin/bash

bash build/generate-doc.sh

mvn clean package
rm -rf bin
rm -rf repo
cp -a target/ofexport/bin .
cp -a target/ofexport/repo .
