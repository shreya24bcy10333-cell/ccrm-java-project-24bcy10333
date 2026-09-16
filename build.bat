@echo off
REM Delete previous build
if exist out rmdir /s /q out
mkdir out

REM Delete old sources.txt if exists
if exist sources.txt del sources.txt

REM Generate sources.txt with full paths to all .java files
for /r src %%f in (*.java) do (
    echo %%f >> sources.txt
)

REM Check if sources.txt has content
if not exist sources.txt (
    echo sources.txt was not created!
    pause
    exit /b
)

REM Compile Java files
javac -d out @sources.txt

echo.
echo Build finished successfully.
echo Run using: run.bat
pause
