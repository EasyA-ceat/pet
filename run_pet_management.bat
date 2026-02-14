@echo off
echo.
echo ==============================================
echo    宠物管理系统启动程序
echo ==============================================
echo.

REM 检查Java是否已安装
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java运行环境!
    echo 请先安装Java 17或更高版本。
    echo.
    pause
    exit /b 1
)

echo Java版本检查通过...
echo.

REM 设置应用程序参数
set APP_NAME=pet-management-system
set JAR_FILE=target\%APP_NAME%-1.0.0.jar
set MIN_HEAP=512m
set MAX_HEAP=1024m

REM 检查JAR文件是否存在
if not exist "%JAR_FILE%" (
    echo 错误: 未找到应用程序JAR文件!
    echo 请先执行 mvn clean package 命令打包项目。
    echo.
    pause
    exit /b 1
)

echo 正在启动宠物管理系统...
echo.

REM 启动应用程序
java -Xms%MIN_HEAP% -Xmx%MAX_HEAP% -jar "%JAR_FILE%"

REM 检查应用程序是否正常退出
if %errorlevel% neq 0 (
    echo.
    echo 错误: 应用程序异常退出!
    echo 错误码: %errorlevel%
    echo.
    pause
    exit /b %errorlevel%
)

echo.
echo 应用程序已正常退出。
pause
