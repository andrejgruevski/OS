Lab 1 - Group B

# 1.
mkdir directory1 directory2 && touch directory1/file123

# 2.
#chmod ug=rw,o= hello_world.py
#chmod ug+rw,0-rwx hello_world.py
chmod 660 hello_world.py

# 3.
mv directory1/file123 directory2/file456

# 4.
#cat OS1.txt OS2.txt
awk '{print}' OS1.txt OS2.txt

# 5.
awk '{print $1, $3}' OS1.txt OS2.txt

# 6.
wc OS2.txt

# 7.
#grep '^21' OS2.txt
awk '$1 ~ /^21/ OS2.txt

# 8.
#awk '$1 ~ /^22/ && $4 <=50 {print $1,$2,$4}' OS2.txt
grep '^22' OS2.txt | awk '$4 <=50 {print $1,$2,$4}'

# 9.
#awk '$2=="14.03.2024" && $3=="in_progress" {counter++} END {print counter}' 

# 10.
awk 'NR>1{counter[$1]++} END{for(i in counter) printf "%-20s %s\n", i, counter[i]}' | sort -k2,2nr


## BONUS
# 11.
awk 'NR>1{counter[$1]++; total++}
END{
	for(i in counter){
	percent = (counter[i]/total)*100;
	printf "%-20s %5d %6.2f%%\n", i, counter[i],percent
}
}' file.txt | sort -k2,2nr

# 12.
# Даден е CSV фајл кој содржи 8 колони IME, KATEGORIJA, BREND, CENA, VALUTA, BOJA, GOLEMINA и DOSTAPNOST. Да се напише команда која ќе ги конвертира сите цени во USD така што ако валутата е DEN цената да се подели со 56, а потоа за сите производи со цена поголема или еднаква на 500 USD да се примени попуст од 10% и сите вредности за валута да се постават на USD, при што резултатот треба да се испечати во ист CSV формат без промена на редоследот на колоните.
awk -f',' 'BEGIN{OFS=","}
NR==1{print; next}
{
if($5=="DEN"){
	$4 = $4 / 56
}
if($4 >= 500){
	$4 = $4 * 0.9
}

$5="USD"

print
}' file.csv
