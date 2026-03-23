#!/bin/bash

#Напишете командна процедура show.sh којашто користи select јамка за да прикаже интерактивно
#мени за сите датотеки во тековниот именик.
#Процедурата треба:
#- да му овозможи на корисникот да избере датотека преку нејзиниот број за да ја погледне
#содржината,
#- да овозможи опција „Exit Program“ којашто ќе му дозволи на корисникот на излезе од јамката,
#- да прикаже соодветна порака, ако корисникот избере нешто што не е регуларна датотека (на
#пример, именик).

PS3="Select a file to view: "

select file in * "Exit Program"

do
	if [ "$file" == "Exit Program" ]
	then
		echo "You have exited the program."
		break
	fi

	if [ -z "$file" ]
	then
		echo "Invalid selection. Try again!"
		continue
	fi
	
	if [ ! -f "$file" ]
	then
		echo "WARNING: '$file' is NOT a regular file."
		continue
	fi

	echo "$file"
	cat "$file"
done

