#!/bin/bash

cp _README.md README.md
python build/toc.py README.md RELEASE-NOTES.md DOCUMENTATION.md > TOC.md
cat TOC.md >> README.md
