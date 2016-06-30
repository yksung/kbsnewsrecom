@echo off
set CLASSPATH=.\classes;
set JAVA_HOME="C:\Program Files\Java\jdk1.6.0_45"
cd classes
%JAVA_HOME%\bin\jar cvf ../lib/wisetea-v2.0.0.jar *
pause