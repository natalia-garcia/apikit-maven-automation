#!/bin/sh

if [ "$#" -eq 0 ]
then
  echo "Please, specify the required parameters: "
  echo "-p <path/to/standalone/server> (required)"
  echo "-v <apikitVersion> (required)"
  echo "-a <path/to/application/folder> (required)"
  echo "-r <path/to/RAML/file> (required)"
  echo "-s <muleStartCommand> (required)"
  echo "Example: sh mavenAutomation.sh -p /Users/myUser/Standalone/ -v 1.3 -a /Users/myUser/dev/APIkit/myTestApp -r /Users/myUser/testFiles/RAMLfile.raml -s gateway"
  exit 1
fi


while getopts “p:v:a:r:s:” OPTION
do
     case $OPTION in
         p)
             standaloneFolder=$OPTARG
             ;;
         v)
             version=$OPTARG
             ;;
         a)
             appFolder=$OPTARG
             ;;
         r)
             raml=$OPTARG
             ;;
         s)
             muleStartCommand=$OPTARG
             ;;
     esac
done

if [[ -z $standaloneFolder ]]
then
     echo "Path to standalone server is missing. Use -p option to specify the path. "
     exit 1
fi

if [[ -z $version ]]
then
     echo "Version is missing. Use -v option to specify the version. "
     exit 1
fi

if [[ -z $appFolder ]]
then
     echo "Path to application folder is missing. Use -a option to specify the path. "
     exit 1
fi

if [[ -z $raml ]]
then
     echo "Path to RAML file is missing. Use -r option to specify the path. "
     exit 1
fi

if [[ -z $muleStartCommand ]]
then
     echo "Command to start mule is missing. Use -s option to specify the command. "
     exit 1
fi

echo "*********************************************"
echo "*  Set MULE_HOME"
echo "*********************************************"
echo export MULE_HOME=$standaloneFolder
export MULE_HOME=$standaloneFolder

echo "*********************************************"
echo "*  Empty apps folder in server"
echo "*********************************************"
echo rm -rf $MULE_HOME/apps/*
rm -rf $MULE_HOME/apps/*

echo "*********************************************"
echo "*  Generate APIkit project"
echo "*********************************************"

cd $appFolder

app=interopTest

rm -rf $app
echo mvn archetype:generate -DarchetypeGroupId=org.mule.tools -DarchetypeArtifactId=apikit-archetype -DarchetypeVersion=$version -DgroupId=org.mule -DartifactId=$app -Dversion=1.0 -B
mvn archetype:generate -DarchetypeGroupId=org.mule.tools -DarchetypeArtifactId=apikit-archetype -DarchetypeVersion=$version -DgroupId=org.mule -DartifactId=$app -Dversion=1.0 -B

cd $app
pwd

echo "*********************************************"
echo "*  Original RAML"
echo "*********************************************"

echo "*********************************************"
echo "*  Copy RAML file to api folder"
echo "*********************************************"

echo cp $raml src/main/api
cp $raml src/main/api
#raml2=/Users/natalia.garcia/Drive/TestCases/RAMLs/withSecuritySchemes.raml
#cp $raml2 src/main/api
echo "*********************************************"
echo "*  Remove api.raml"
echo "*********************************************"

echo rm src/main/api/api.raml
rm src/main/api/api.raml

echo "*********************************************"
echo "*  Rename RAML file"
echo "*********************************************"

echo mv src/main/api/$(basename $raml) src/main/api/api.raml
mv src/main/api/$(basename $raml) src/main/api/api.raml

ls -la src/main/api

echo "*********************************************"
echo "*  Scaffold project"
echo "*********************************************"

echo mvn org.mule.tools:apikit-maven-plugin:$version:create 
mvn org.mule.tools:apikit-maven-plugin:$version:create 

echo "*********************************************"
echo "*  Install app"
echo "*********************************************"
echo mvn clean install
mvn clean install

echo cd $MULE_HOME
cd $MULE_HOME
echo bin/mule start
bin/mule start

