#!/bin/sh
APPDIR=`dirname $0`;
java  -Xmn100M -Xms700M -Xmx700M -cp "$APPDIR/src:$APPDIR/bin:$APPDIR/lib:/home/uran/devspace/_gwt-linux-1.4.60/gwt-user.jar:/home/uran/devspace/_gwt-linux-1.4.60/gwt-dev-linux.jar" com.google.gwt.dev.GWTCompiler -out "$APPDIR/www" "$@" net.pleso.odbui.odbui/odbui.html;


#@java -cp "%~dp0\src;%~dp0\bin;C:/Program Files/eclipse/gwt/gwt-user.jar;C:/Program Files/eclipse/gwt/gwt-dev-windows.jar" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %* net.pleso.odbui.odbui