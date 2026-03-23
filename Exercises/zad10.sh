#!/bin/bash

#Напишете командна процедура reverse.sh којашто за дадена датотека ги печати полињата од
#секоја линија во обратен редослед со помош на AWK.


if [ $# -lt 1 ]
then
	echo "USAGE: $0 files..."
	exit 1
fi

awk '{
	for (i = NF; i>=1; i--){
		printf("%s%s", $i, (i>1 ? OFS : ORS))
	}

}' "$@"
