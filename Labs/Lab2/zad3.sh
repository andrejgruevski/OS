#!/bin/bash


#Write a shell script that takes as a command-line argument a path to a source directory.
#
#The source directory contains text files. The script should process only the files that satisfy the following conditions:
#
#- they are regular files
#- they have a .txt extension
#- their filename contains at least one digit
#- others have read permission for the file
#
#The script should create a directory named processed in the current working directory.
#
#For each file that satisfies the conditions, a new file should be created in the processed directory with the same name, but with the extension .out instead of .txt.
#
#Each .out file should contain all unique words from the corresponding input file that satisfy the following conditions:

#- the word contains only letters from the English alphabet
#- the word is treated case-insensitively
#- all words in the output should be written in lowercase
#- each word should appear only once
#- the words should be sorted in alphabetical order

#A “word” is defined as any sequence obtained by splitting the text by spaces and punctuation.

#Additionally, the script should create a file named summary.txt in the current working directory with exactly the following format:

#processedFiles: X
#totalUniqueWords: Y
#fileWithMostUniqueWords: Z
#maxUniqueWords: W

#where:
#- X is the number of processed files
#- Y is the total number of unique words across all generated .out files
#- Z is the name of the file (original .txt name) that has the most unique words
#- W is the number of unique words in that file

#Additional requirements:

#- If exactly one argument is not provided, print:
#  Usage: ./script.sh <source_directory>
#- If the directory does not exist, print:
#  Error: source directory does not exist
#- If there are no files that satisfy the conditions, the script should still create processed and summary.txt
#- In that case, summary.txt should contain:
#  processedFiles: 0
#  totalUniqueWords: 0
#  fileWithMostUniqueWords: none
#  maxUniqueWords: 0
# - Temporary files must not be used


if [ $# -ne 1 ]
 then
    echo "Usage: $0 <source_directory>"
    exit 1
fi

sourceDir="$1"

if [ ! -d "$sourceDir" ]
 then
    echo "Error: source directory does not exist"
    exit 1
fi

mkdir -p "./processed"

processed_count=0
total_unique_words=0
max_unique_words=0
file_with_most=""

for i in "$sourceDir"/*;
 do
    [ -f "$i" ] || continue

    filename=$(basename "$i")



    [[ "$filename" == *.txt ]] || continue
    [[ "$filename" =~ [0-9] ]] || continue


    perm_octal=$(stat -c "%a" "$i")
    others_digit=$(( perm_octal % 10 ))
    (( (others_digit & 4) != 0 )) || continue

    outname="${filename%.txt}.out"
    outpath="./processed/$outname"


    unique_words=$(cat "$i" | tr -s '[:space:][:punct:]' '\n' | grep -E '^[a-zA-Z]+$' | tr '[:upper:]' '[:lower:]' | sort -u)


    echo "$unique_words" > "$outpath"


    word_count=$(echo "$unique_words" | grep -c '^[a-zA-Z]')

    processed_count=$((processed_count + 1))
    total_unique_words=$((total_unique_words + word_count))

    if (( word_count > max_unique_words ))
         then
                max_unique_words=$word_count
                file_with_most="$filename"
        fi
done

if [ $processed_count -eq 0 ]; then
    cat > "./summary.txt" <<EOF
processedFiles: 0
totalUniqueWords: 0
fileWithMostUniqueWords: none
maxUniqueWords: 0
EOF
else
    cat > "./summary.txt" <<EOF
processedFiles: $processed_count
totalUniqueWords: $total_unique_words
fileWithMostUniqueWords: $file_with_most
maxUniqueWords: $max_unique_words
EOF
fi
