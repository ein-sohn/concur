@echo off
set CLASSPATH=./lib

java -Dfile.encoding-UTF-8 -Dprofiles.active=prd -jar ../PGPFileClean.jar 30