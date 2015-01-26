#!/bin/sh

if [ "$#" -eq 0 ]
then
  echo "Please, specify the required parameters: "
  echo "-p <path/to/standalone/server> (required)"
  echo "-f <path/to/project> (required)"
  exit 1
fi


while getopts “p:f:” OPTION
do
     case $OPTION in
         p)
             standaloneFolder=$OPTARG
             ;;
         f)
             projectFolder=$OPTARG
             ;;
     esac
done

if [[ -z $standaloneFolder ]]
then
     echo "Path to standalone server is missing. Use -p option to specify the path. "
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

cd $projectFolder
pwd

echo "*********************************************"
echo "*  Install app"
echo "*********************************************"
echo mvn -f pom.xml install
mvn -f pom.xml install

echo cd $MULE_HOME
cd $MULE_HOME
echo bin/$muleStartCommand start
bin/$muleStartCommand start