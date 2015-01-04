#!/bin/bash

set -e

python build/mdtoc.py README.md RELEASE-NOTES.md SUPPORT.md DOCUMENTATION.md FAQ.md | egrep '\[Table Of Contents' -v > TOC.md

for FILE in README.md DOCUMENTATION.md FAQ.md;do
	python build/mdinsert.py TOC.md $FILE '^## Table Of Contents' '^#' > .tmp
	cp .tmp $FILE
	rm .tmp
done