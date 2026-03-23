#!/bin/bash

#Напишете командна процедура tolower.sh којашто ги конвертира имињата на аргументите во
#мали букви (користејќи функција) и ги прикажува на екран.

toLower() {
	echo "$1" | tr '[A-Z' '[a-z]';
}

for argument in "$@"
do
	toLower "$argument"
done

