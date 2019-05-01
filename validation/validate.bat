@echo off
setlocal enableextensions

:: Script to validate assignment submission for Project 1 for SWEN30006 SEM 1 2019

:: Unzip submission
set zipfile=Automail.zip
echo|set /p=Looking for %zipfile%...
if not exist %zipfile% (
    echo ERROR: Specified zip file can not be found.
	pause
	exit /b
)
echo found!

set output=output.txt
set results=test_results.txt
set props=test.properties

if exist "%output%" del /q "%output%"

:: Delete twice below because rmdir is flakey if dir has files in it
if exist tmp_unzipped rmdir /s /q tmp_unzipped
if exist tmp_unzipped rmdir /s /q tmp_unzipped
if exist build rmdir /s /q build
if exist build rmdir /s /q build
if exist dist rmdir /s /q dist
if exist dist rmdir /s /q dist
if exist %output% del %output%
if exist %results% del %results%

:: Unzip submission
set zip_path="C:\Program Files\7-Zip\7z"

:: set ant=apache-ant-1.10.3\bin\ant
set ant=apache-ant-1.10.5\bin\ant

echo|set /p=Unzipping archive...
%zip_path% x "%zipfile%" -otmp_unzipped -y > nul
if not exist tmp_unzipped (
	echo ERROR: Failed to unzip %zipfile%
	pause
	exit /b
)
echo done!

:: Validate supporting files
echo|set /p=Looking for DesignAnalysis.pdf...
if not exist tmp_unzipped\Automail\DesignAnalysis.pdf (
	echo.
	echo ERROR: DesignAnalysis.pdf not found. Please check the submission guide and make sure you are following the requirements.
	pause
	exit /b
)
echo found!
echo|set /p=Looking for StaticDomainModel.pdf/png...
if not exist tmp_unzipped\Automail\StaticDomainModel.pdf if not exist tmp_unzipped\Automail\StaticDomainModel.png (
	echo.
	echo ERROR: StaticDomainModel.pdf/png not found. Please check the submission guide and make sure you are following the requirements.
	pause
	exit /b
)
echo found!
echo|set /p=Looking for StaticDesignModel.pdf/png...
if not exist tmp_unzipped\Automail\StaticDesignModel.pdf if not exist tmp_unzipped\Automail\StaticDesignModel.png (
	echo.
	echo ERROR: StaticDesignModel.pdf/png not found. Please check the submission guide and make sure you are following the requirements.
	pause
	exit /b
)
echo found!
echo|set /p=Looking for DynamicDesignModel.pdf/png...
if not exist tmp_unzipped\Automail\DynamicDesignModel.pdf if not exist tmp_unzipped\Automail\DynamicDesignModel.png (
	echo.
	echo ERROR: DynamicDesignModel.pdf/png not found. Please check the submission guide and make sure you are following the requirements.
	pause
	exit /b
)
echo found!

echo|set /p=Building project...

set "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_91"
:: set "JAVA_HOME=C:\Program Files\Java\jdk-12"
call %ant% >> "%output%" && (echo success!) || (echo Build failed. Checkout %output% for the compiler log. & pause & exit /b)

echo|set /p=Running project...
call java -jar tmp_unzipped\dist\lib\Project1.jar >> %results% && (echo success!) || (echo failed...check %output% and %results% for details.)

echo|set /p=Checking default behaviour...
fc /b expected.txt %results% > nul
if errorlevel 1 (
    echo FAILED
	echo Default behaviour failed >> %output%
) else (
    echo success!
	echo Default behaviour successful! >> %output%
)

echo|set /p=Cleaning up...
if exist tmp_unzipped rmdir /s /q tmp_unzipped
if exist tmp_unzipped rmdir /s /q tmp_unzipped
echo done.

echo.
echo !!IMPORTANT!!
echo Make sure you include %output% in your final submission zip.
echo This shows us that you have validated your work.

echo.
pause

exit /b
