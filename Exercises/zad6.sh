#!/bin/bash

#Напишете командна процедура multiply.sh којашто прима еден аргумент и го множи со 3,5.
#Процедурата треба да го прикаже резултатот со две децимални места.

if [ $# -lt 1 ] 
then
	echo "USAGE: $0 number"
	exit 1
fi

num=$1

result=$(echo "scale=2; $num * 3.5" | bc)
printf "%.2f\n" "$result"

# BC командата е Linux, така да во Git Bash нема да работи, пробај со  AWK
