#!/bin/bash

#Да се напише командна процедура smartzip што може да врши автоматско декомпресирање
#bzip2, gzip и zip компресирани датотеки.

filetype=$(file "$1")

case "$filetype" in
	"$1: Zip archive"*) unzip "$1";;
	"$1: gzip compressed"*) gunzip "$1";;
	"$1: bzip2 compressed"*) bunzip2 "$1";;
	*) echo "Datotekata $1 ne e vo potrebniot format"
exit 1;;
esac
