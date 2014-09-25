#!/bin/sh

if [ "$#" -eq 0 ]
then
  echo "Please, specify MULE_HOME: "
  echo "-p <path/to/standalone/server> (required)"
  echo "-s <muleStartCommand> (required)"
  echo "Example: sh stopMule.sh -p /Users/myUser/Standalone/ -s gateway"
  exit 1
fi


while getopts “p:s:” OPTION
do
     case $OPTION in
         p)
             standaloneFolder=$OPTARG
             ;;
         s)
             muleStartupCommand=$OPTARG
             ;;
     esac
done

if [[ -z $standaloneFolder ]]
then
     echo "Path to standalone server is missing. Use -p option to specify the path. "
     exit 1
fi

if [[ -z $muleStartupCommand ]]
then
     echo "Mule startup command is missing. Use -s option to specify the command. "
     exit 1
fi

echo "*********************************************"
echo "*  Set MULE_HOME"
echo "*********************************************"
echo export MULE_HOME=$standaloneFolder
export MULE_HOME=$standaloneFolder

cd $MULE_HOME
rm conf/muleLicenseKey.lic; touch conf/.lic-mule

echo "*********************************************"
echo "*  Stop Mule Server"
echo "*********************************************"
# chmod 777 $MULE_HOME/bin/mule
echo bin/$muleStartupCommand stop
$MULE_HOME/bin/$muleStartupCommand stop