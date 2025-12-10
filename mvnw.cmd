@REM Maven Wrapper startup script
@echo off
set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
if not "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir
set MAVEN_PROJECTBASEDIR=%~dp0
:endDetectBaseDir
set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
java -jar %WRAPPER_JAR% %*