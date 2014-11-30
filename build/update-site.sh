#!/bin/bash
set -e
mvn clean package site
cp -a target/site doc
