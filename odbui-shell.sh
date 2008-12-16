#!/bin/sh
APPDIR=`dirname $0`;
APPDIR='/home/uran/devspace/odbui'
#env LD_LIBRARY_PATH=/home/uran/devspace/_gwt-linux-1.4.60/mozilla-1.7.12:$LD_LIBRARY_PATH 
#java  -cp "$APPDIR/src:$APPDIR/bin:$APPDIR/lib:/home/uran/devspace/odbui/lib/jena.jar:/home/uran/devspace/_gwt-linux-1.4.60/gwt-user.jar:/home/uran/devspace/_gwt-linux-1.4.60/gwt-dev-linux.jar" com.google.gwt.dev.GWTShell -out "$APPDIR/www" "$@" net.pleso.odbui.odbui/odbui.html;
java  -cp "$APPDIR/src:$APPDIR/bin:/home/uran/devspace/_gwt-linux-1.4.60/gwt-user.jar:/home/uran/devspace/_gwt-linux-1.4.60/gwt-dev-linux.jar" com.google.gwt.dev.GWTShell -out "$APPDIR/www" "$@" net.pleso.odbui.odbui/odbui.html;

#@java -cp "%~dp0\src;%~dp0\bin;C:/Program Files/eclipse/gwt/gwt-user.jar;C:/Program Files/eclipse/gwt/gwt-dev-windows.jar" com.google.gwt.dev.GWTShell -out "%~dp0\www" %* net.pleso.odbui.odbui/odbui.html

