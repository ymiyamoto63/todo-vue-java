@ECHO OFF
SET MAVEN_VERSION=3.9.6
SET MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MAVEN_VERSION%-bin
SET MAVEN_BIN=%MAVEN_HOME%\apache-maven-%MAVEN_VERSION%\bin\mvn.cmd
SET DOWNLOAD_URL=https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip

IF NOT EXIST "%MAVEN_BIN%" (
    ECHO Downloading Maven %MAVEN_VERSION%...
    IF NOT EXIST "%MAVEN_HOME%" MKDIR "%MAVEN_HOME%"
    POWERSHELL -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest '%DOWNLOAD_URL%' -OutFile '%MAVEN_HOME%\maven.zip'"
    POWERSHELL -Command "Expand-Archive -Path '%MAVEN_HOME%\maven.zip' -DestinationPath '%MAVEN_HOME%' -Force"
    DEL "%MAVEN_HOME%\maven.zip"
    ECHO Maven %MAVEN_VERSION% installed.
)

CALL "%MAVEN_BIN%" %*
