
#!/bin/bash

if [ $# -ne 2 ]
then
    echo "USED: $(basename $0) <json_file> <output_folder>"
    exit 1
fi

if [ ! -f "$1" ]
then
    echo "Prviot argument mora da e JSON file"
    exit 1
fi

[ ! -d "$2" ] && mkdir -p "$2"

file=$(cat "$1")

total=0
counter=0
IFS=$'\n'

for i in $file
do
    duration=$(echo "$i" | grep "duration" | awk '{print $2}' | awk -F, '{print $1}')

    if [ -n "$duration" ]
    then
        total=$(echo "$total + $duration" | bc)
        counter=$((counter + 1))
    fi
done

average=$(echo "scale=2; $total / $counter" | bc)

echo "id,filepath,filesize,is_longer" > "$2/output.csv"

count=0


for i in $file
do
    filepath=$(echo "$i" | grep "filepath" | awk '{print $2}' | awk -F, '{print $1}' | awk -F\" '{print $2}')
    duration=$(echo "$i" | grep "duration" | awk '{print $2}' | awk -F, '{print $1}')
    filesize=$(echo "$i" | grep "filesize" | awk '{print $2}' | awk -F\" '{print $2}')

    if [ -n "$duration" ]
    then
        temp=`awk 'BEGIN {if ( '$duration' > '$average' ) print "1"}'`

        if [ "$temp" -eq 1 ]
        then
            echo "$count,$filepath,$filesize,1" >> "$2/output.csv"
        else
            echo "$count,$filepath,$filesize,0" >> "$2/output.csv"
        fi

        count=$(($count + 1))
    fi
done
