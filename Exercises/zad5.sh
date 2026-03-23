#!/bin/bash

#Напишете командна процедура rename.sh што овозможува групно преименување на повеќе
#датотеки користејќи ја sed командата за пребарување и замена на текст во имињата на
#датотеките.
#Процедурата треба да прима најмалку 3 аргументи:
#- стариот текст што треба да биде заменет,
#- новиот текст за замена, и
#- листа од датотеки за преименување.
#За секоја датотека од листата, процедурата треба:
#- да провери дали датотеката постои,
#- да го генерира новото име на датотеката користејќи sed

if [ $# -lt 3 ]
then 
	echo "USAGE: $0 old new files..."
	exit 1
fi

old="$1"
new="$2"

shift 2
files="$@"

for file in $files
do
	if [ ! -f "$file ]
	then
		echo "WARNING: The file '$file' does not exist"
		continue	
	fi

	newfile=$(echo "$file" | sed "s/$old/$new/g")

	if [ -f "$newfile" ]
	then
		echo "WARNING: The file '$file' cannot be renamed to '$newfile', because '$newfile' already exist."
		continue
	fi
	
	mv "$file" "$newfile"
	echo "The file '$file' is renamed to '$newfile'."
done

