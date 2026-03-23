#!/bin/bash

#Напишете командна процедура pcnt.sh којашто за корисник предаден како прв аргумент ќе
#запише колку деца процеси има секој од процесите што корисникот ги отпочнал во излезна
#датотека out.txt.
#Процедурата треба:
#- да прикаже насоки за користење, ако не е проследен аргумент,
#- да ја пребрише излезната датотека, ако таа постои, и
#- на крај да ја прикаже содржината на излезната датотека.

if [ $# -ne 1 ]
then
	echo "USAGE: `basename $0` username"
	exit 1
fi

user="$1"
file="out.txt"
[ -f "$file" ] && rm "$file"

ps --no-headers -u "$user" -o pid | while read pid
do
	counter=$(ps --no-headers --ppid "$pid" -o pid | wc -l)
	echo "$pid $counter" >> "$file"
done

cat "$file"

