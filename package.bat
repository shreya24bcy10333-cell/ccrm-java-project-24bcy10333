@echo off
REM Package the project into a runnable JAR

set BIN_DIR=bin
set JAR_NAME=ccrm.jar
set MANIFEST=manifest.mf

echo =========================================
echo Packaging Campus Course & Records Manager
echo =========================================

if not exist "%BIN_DIR%" (
    echo [ERROR] Bin folder not found. Please run build.bat first.
    exit /b 1
)

REM Create manifest file
echo Main-Class: edu.ccrm.cli.Main > %MANIFEST%
echo Class-Path: . >> %MANIFEST%

REM Build the JAR
jar cfm %JAR_NAME% %MANIFEST% -C %BIN_DIR% .

REM Cleanup
del %MANIFEST%

echo.
echo [SUCCESS] %JAR_NAME% created.
echo To run: java -jar %JAR_NAME%
pause
