#!/bin/bash

#Напишете командна процедура pathinfo.sh што ги изминува сите именици присустни во PATH
#околинската променлива и прикажува колку извршувачки датотеки има во секој именик. Исто
#така, процедурата треба да се справи со случаите кога некој именик што е во PATH не постои.

IFS=:
for DIR in $PATH
do
 	if [ -d "$DIR" ]
 	then
		COUNT=$(find "$DIR" -maxdepth 1 -type f -executable 2>/dev/null | wc -l)
		echo "The directory '$DIR' has $COUNT executables."
	 else
 		echo "The directory '$DIR' is missing."
 	fi
done
