@echo off
setlocal enabledelayedexpansion
echo ========================================
echo 医院预约和挂号系统 - 启动脚本
echo ========================================
echo.

echo 正在检查端口8080是否被占用...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
    echo 发现端口8080被进程 %%a 占用，正在停止...
    taskkill /F /PID %%a >nul 2>&1
    timeout /t 1 /nobreak >nul
)

echo.
echo 正在编译项目...
call mvn clean compile

if %ERRORLEVEL% NEQ 0 (
    echo 编译失败！
    pause
    exit /b 1
)

echo.
echo 正在生成依赖classpath...
call mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt

if %ERRORLEVEL% NEQ 0 (
    echo 生成classpath失败！
    pause
    exit /b 1
)

echo.
echo 正在启动服务器...
set CLASSPATH=target\classes
for /f "delims=" %%i in (classpath.txt) do set CLASSPATH=!CLASSPATH!;%%i
java -cp "!CLASSPATH!" com.hospital.server.HospitalServer

pause
