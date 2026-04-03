#!/bin/bash

#Write a shell script that copies all files that satisfy the following conditions:

#- They have a .txt extension
#- Their filename contains at least one letter
#- Others has read permission for the file

#Requirements:
#
#- The source directory is provided as a command-line argument.
#- The script should create a directory named copiedFiles in the current working directory.
#- All files that satisfy the conditions should be copied into this directory.

#Additionally, a file named statistics.txt should be created, containing:
#
#- Total number of copied files
#- Total size (in bytes)
#- Total number of lines
#- Size of the largest file
#- Name of the largest file

#The format of statistics.txt should be exactly:

#copiedFiles: X
#totalSize: Y
#totalNumberOfLines: Z
#largestFile: A
#longestName: B

#TEST:
#mkdir source
#cd source
#echo "a" > file1.txt
#echo "b" > file2.txt
#echo "c" > test.txt
#chmod o+r file1.txt
#chmod o+r file2.txt
#chmod o-r test.txt
#cd ..

#RESULT:
#copiedFiles: 2
#totalSize: 4
#totalNumberOfLines: 2
#largestFile: 2
#longestName: file1.txt

if [ $# -ne 1 ]
then
        echo "USED: $(basename $0) username"
        exit 1
fi

sourceDir="$1"

if [ ! -d "$sourceDir" ]
then
        echo "Eror: Directory '$sourceDir' does not exist."
        exit 1
fi

mkdir -p "./copiedFiles"

copiedCounter=0
totalSize=0
totalLines=0
largestFileSize=0
longestName=""

for i in "$sourceDir"/*;
do
        [ -f "$i" ] || continue
        filename=$(basename "$i")

        [[ "$filename" == *.txt ]] || continue

        basename_no_ext="${filename%.txt}"
        [[ "$basename_no_ext" =~ [a-zA-Z] ]] || continue

        if [ ! -r "$i" ]
        then
                continue
        fi

        perm_octal=$(stat -c "%a" "$i")
        others_digit=$(( perm_octal % 10 ))

        if (( (others_digit & 4) == 0 ))
        then
                continue
        fi

        cp "$i" "./copiedFiles/$filename"

        copiedCounter=$((copiedCounter + 1))

        fsize=$(stat -c "%s" "$i")
        totalSize=$((totalSize + fsize))

        flines=$(wc -l <"$i")
        totalLines=$((totalLines + flines))

        if (( fsize > largestFileSize ))
        then
                largestFileSize=$fsize
        fi

        if [ -z "$longestName" ] || [ ${#filename} -gt ${#longestName} ]
        then
                longestName="$filename"
        fi

done

cat > "statistiics.txt" << EOF
copiedFiles: $copiedCounter
totalSize: $totalSize
totalNumberOfLines: $totalLines
largestFile: $largestFileSize
longestName: $longestName
EOF
