#!/bin/bash
sbt universal:packageZipTarball
cp record-sorter-cmd/target/universal/*.tgz $1
cp customer-rest-management/target/universal/*.tgz $1
