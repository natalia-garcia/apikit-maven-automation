#!/bin/sh

if [ "$#" -eq 0 ]
then
  echo "Please, specify MULE_HOME: "
  echo "-p <path/to/standalone/server> (required)"
  echo "Example: sh stopMule.sh -p /Users/myUser/Standalone/ "
  exit 1
fi


while getopts “p:” OPTION
do
     case $OPTION in
         p)
             standaloneFolder=$OPTARG
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

cd $MULE_HOME

echo "*********************************************"
echo "*  Stop Mule Server"
echo "*********************************************"
echo bin/mule stop
bin/mule stop