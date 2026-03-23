#!/bin/bash

#Да се напише командна процедура што ќе одбројува одреден број секунди до почетокот на
#натпреварот. 

counter=$1

while [ $counter -gt 0 ]
do
	echo $counter
	counter=$(( $counter - 1 ))
	sleep 1
done
echo "The match stareted"

