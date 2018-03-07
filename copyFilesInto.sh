#!/bin/bash

#shell script to copy files from git repo into the project directory
#USAGE:
#	./copyFiles.sh --proj  (copy files from repo into project directory)
#	./copyFiles.sh --repo  (copy files from project directory into repo)

ERROR="ERROR: must specify where to copy files.
	--proj: copy files from repo into project directory
	--repo: copy files from project directory into repo"

if [ $# -lt 1 ]
then
	echo "$ERROR"
	exit 1
fi

case $1 in
	--proj) 
		proj="true"
		;;
	--repo)
		proj="false"
		;;
	*)	
		echo "$USAGE"
		exit 1
		;;
esac


#set directory of the project
PROJECT_DIR=~/AndroidStudioProjects/ttr

if [ $proj == "true" ]
then
	#update the app modules main folder
	rm -rf $PROJECT_DIR/app/src/main/*
	cp -r mainApp/* $PROJECT_DIR/app/src/main/

	#update server module main folder
	rm -rf $PROJECT_DIR/server/src/main/*
	cp -r mainServer/* $PROJECT_DIR/server/src/main/

	#update shared code module
	rm -rf $PROJECT_DIR/shared/src/main/*
	cp -r mainShared/* $PROJECT_DIR/shared/src/main/
else
	#update mainApp functional files
	rm -rf mainApp/*
	cp -r $PROJECT_DIR/app/src/main/* mainApp/

	#update mainServer functional files
	rm -rf mainServer/*
	cp -r $PROJECT_DIR/server/src/main/* mainServer/

	#update mainShared functional files
	rm -rf mainShared/*
	cp -r $PROJECT_DIR/shared/src/main/* mainShared/
fi
