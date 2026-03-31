#!/bin/bash

#Write a shell script that, for data entered through standard input, in the format of the output of the command netstat -an, prints to standard output the number of TCP connections for each foreign address (IP without port).
#
#Each line of the output should be in the format:
#"%-20s %s\n"
#where the first argument is the foreign IP address (without port), and the second is the number of TCP connections for that address.
#
#The output should be sorted:
#- in descending order by the number of connections
#- for equal values, in ascending order by the IP address
#
#Additional requirements:
#- Only TCP connections should be considered (tcp and tcp6)
#- Header lines should be ignored
#- The port should be removed (e.g., 192.168.1.10:443 → 192.168.1.10)
#- Use awk, sort and uniq (or an equivalent solution using awk)
#- Do not use temporary files
#- The script must read exclusively from standard input (stdin)


awk '
NR>2 && ($1=="tcp" || $1=="tcp6"){

        address = $5
        sub(/:[^:]*$/, "", address)
        if(address == "::") address = ":::"
        print address
}' | sort | uniq -c | sort -k1,1nr -k2,2 | awk '{printf "%-20s %s\n", $2, $1}'
