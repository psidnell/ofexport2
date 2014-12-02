#!/bin/bash
set -e
git add bin repo
git commit -m "adding artifacts"
mvn release:prepare
mvn release:perform
