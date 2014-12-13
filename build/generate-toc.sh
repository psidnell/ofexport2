#!/bin/bash

set -e

cp _README.md README.md
python build/toc.py README.md RELEASE-NOTES.md SUPPORT.md DOCUMENTATION.md > TOC.md
cat TOC.md >> README.md
