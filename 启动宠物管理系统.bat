@echo off
chcp 65001 > nul
echo ========================================
echo     宠物管理系统启动器
echo ========================================
echo.

REM 检查Java是否安装
where java >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到Java环境！
    echo 请安装Java 17或更高版本
    echo 下载地址: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

echo [信息] 检测到Java环境
java -version 2>&1
echo.

REM 查找jar文件
if exist "target\pet-management-system-1.0.2.jar" (
    set JAR_FILE=target\pet-management-system-1.0.2.jar
) else if exist "pet-management-system-1.0.2.jar" (
    set JAR_FILE=pet-management-system-1.0.2.jar
) else (
    echo [错误] 未找到jar文件！
    echo 请确保已在正确的目录中运行此脚本
    echo 或先运行 setup-project.bat 构建项目
    echo.
    pause
    exit /b 1
)

echo [信息] 正在启动宠物管理系统...
echo.
java -jar "%JAR_FILE%"

if %errorlevel% neq 0 (
    echo.
    echo [错误] 程序异常退出
    echo.
    pause
)
pause
