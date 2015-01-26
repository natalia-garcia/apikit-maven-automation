echo Ahora se viene le cat
cat ./mule-config.xml | ./script.pl > temp.xml
echo Ahora se viene el mv
mv temp.xml mule-config.xml
