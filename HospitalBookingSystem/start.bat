@echo off
chcp 65001 >nul
echo ========================================
echo Starting Hospital Booking System...
echo ========================================
echo.

echo Step 1: Cleaning previous build...
call mvn clean
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Clean failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Compiling project...
call mvn compile
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Step 3: Checking if port 8080 is available...
setlocal enabledelayedexpansion
set PORT_FREE=1
for /f "tokens=5" %%a in ('netstat -ano 2^>nul ^| findstr :8080 ^| findstr LISTENING') do (
    echo Port 8080 is occupied by process %%a, stopping...
    taskkill /F /PID %%a >nul 2>&1
    if !ERRORLEVEL! EQU 0 (
        echo Process %%a has been stopped.
        timeout /t 2 /nobreak >nul
    ) else (
        echo Warning: Could not stop process %%a. You may need to stop it manually.
    )
    set PORT_FREE=0
)
endlocal

echo.
echo Step 4: Starting server...
echo Server will start at http://localhost:8080
echo Press Ctrl+C to stop the server
echo.
call mvn exec:java

pause

