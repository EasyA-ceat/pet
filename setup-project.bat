@echo off
chcp 65001 > nul
echo ========================================
echo     宠物管理系统 - 自动配置工具
echo ========================================
echo.

:: 设置环境变量（临时，当前会话有效）
setlocal enabledelayedexpansion
set JAVA_VERSION_REQUIRED=17
set MAVEN_VERSION_REQUIRED=3.9.6
:: 使用国内镜像源
set JAVA_DOWNLOAD_URL=https://mirrors.tuna.tsinghua.edu.cn/Adoptium/17/jdk/x64/windows/OpenJDK17U-jdk_x64_windows_hotspot_17.0.11_9.zip
set MAVEN_DOWNLOAD_URL=https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip
set TOOLS_DIR=%~dp0tools
set JAVA_DIR=%TOOLS_DIR%\jdk-17.0.11+9
set MAVEN_DIR=%TOOLS_DIR%\apache-maven-3.9.6
set MAVEN_SETTINGS=%TOOLS_DIR%\settings.xml

:: 创建工具目录
if not exist "%TOOLS_DIR%" mkdir "%TOOLS_DIR%"

:: 检查步骤1: Java 环境
echo [1/8] 检查 Java 环境...
where java >nul 2>&1
if %errorlevel% neq 0 (
    echo [警告] 未检测到 Java，尝试自动下载和安装...
    call :download_and_install_java
    if !errorlevel! neq 0 (
        echo [错误] Java 自动安装失败！
        echo 请手动从 https://mirrors.tuna.tsinghua.edu.cn/Adoptium/ 下载并安装 Java %JAVA_VERSION_REQUIRED%
        echo.
        pause
        exit /b 1
    )
)
echo [成功] Java 已安装
java -version 2>&1
echo.

:: 检查步骤2: Maven 环境
echo [2/8] 检查 Maven 环境...
set MVN_CMD=
if exist "mvnw.cmd" (
    echo [信息] 找到 Maven Wrapper
    set MVN_CMD=mvnw.cmd
) else (
    where mvn >nul 2>&1
    if %errorlevel% equ 0 (
        echo [成功] 检测到系统安装的 Maven
        set MVN_CMD=mvn
    ) else (
        echo [信息] 未检测到 Maven，尝试自动下载和安装...
        call :download_and_install_maven
        if !errorlevel! neq 0 (
            echo [错误] Maven 自动安装失败！
            echo 请手动从 https://maven.apache.org/download.cgi 下载并安装 Maven %MAVEN_VERSION_REQUIRED%
            echo.
            pause
            exit /b 1
        )
    )
)
if "%MVN_CMD%"=="" (
    echo [错误] 无法确定 Maven 命令！
    echo.
    pause
    exit /b 1
)
echo.

:: 检查步骤3: 项目文件
echo [3/8] 检查项目文件...
if not exist "pom.xml" (
    echo [错误] 未找到 pom.xml 文件！请确保在项目根目录下运行此脚本
    echo.
    pause
    exit /b 1
)
echo [成功] 找到 pom.xml
echo.

:: 步骤4: 清理旧的构建文件（可选）
echo [4/8] 清理旧的构建文件...
%MVN_CMD% clean >nul 2>&1
if %errorlevel% neq 0 (
    echo [警告] 清理过程出现问题，但继续执行
)
echo [成功] 清理完成
echo.

:: 步骤5: 配置 Maven 使用国内镜像（加速依赖下载）
echo [5/8] 配置 Maven 国内镜像...
call :configure_maven_mirror
echo [成功] Maven 镜像配置完成
echo.

:: 步骤6: 安装依赖并编译项目
echo [6/8] 安装依赖并编译项目...
echo [信息] 这可能需要几分钟，取决于网络速度...
echo.
%MVN_CMD% -s "%MAVEN_SETTINGS%" dependency:resolve compile
if %errorlevel% neq 0 (
    echo.
    echo [错误] 依赖安装或编译失败！
    echo 请检查网络连接或 Maven 配置
    echo.
    pause
    exit /b 1
)
echo.
echo [成功] 所有依赖已安装，项目编译成功！
echo.

:: 步骤7: 验证项目是否可以正常打包
echo [7/8] 验证项目打包...
echo [信息] 执行快速打包测试（跳过测试以节省时间）...
%MVN_CMD% -s "%MAVEN_SETTINGS%" package -DskipTests >nul 2>&1
if %errorlevel% neq 0 (
    echo [警告] 打包测试出现问题，但依赖安装已完成
) else (
    echo [成功] 打包验证成功！
)
echo.

:: 步骤8: 创建快速启动脚本（使用本地工具）
echo [8/8] 创建本地工具启动脚本...
(
    echo @echo off
    echo chcp 65001 ^> nul
    echo echo 使用本地工具启动宠物管理系统...
    echo echo.
    echo if exist "%JAVA_DIR%\bin\java.exe" set "PATH=%JAVA_DIR%\bin;%MAVEN_DIR%\bin;%%PATH%%"
    echo if exist "target\pet-management-system-1.0.2.jar" ^(
    echo     set JAR_FILE=target\pet-management-system-1.0.2.jar
    echo ^) else if exist "pet-management-system-1.0.2.jar" ^(
    echo     set JAR_FILE=pet-management-system-1.0.2.jar
    echo ^) else ^(
    echo     echo [错误] 未找到 jar 文件，请先运行 setup-project.bat 进行构建
    echo     echo.
    echo     pause
    echo     exit /b 1
    echo ^)
    echo java -jar "%%JAR_FILE%%"
    echo if %%errorlevel%% neq 0 ^(
    echo     echo.
    echo     echo [错误] 程序异常退出
    echo     echo.
    echo     pause
    echo ^)
    echo pause
) > "%~dp0启动宠物管理系统-本地工具.bat"
echo [成功] 已创建 "启动宠物管理系统-本地工具.bat"
echo.

:: 创建配置说明文件
echo 创建配置说明文件...
(
    echo ========================================
    echo     宠物管理系统 - 本地工具配置
    echo ========================================
    echo.
    echo Java 安装位置: %JAVA_DIR%
    echo Maven 安装位置: %MAVEN_DIR%
    echo Maven 配置文件: %MAVEN_SETTINGS%
    echo.
    echo 使用说明:
    echo 1. 运行 "setup-project.bat" 来配置环境和安装依赖
    echo 2. 运行 "启动宠物管理系统-本地工具.bat" 来启动应用
    echo 3. 或者使用 "启动宠物管理系统.bat" 启动应用（需要系统已安装Java）
    echo.
    echo 注意: 本目录下的 tools 文件夹包含了本地的 Java 和 Maven，
    echo       仅供本项目使用，不会影响系统环境。
    echo.
    echo Maven 已配置为使用阿里云和清华大学镜像源加速依赖下载。
    echo.
) > "%~dp0本地工具配置说明.txt"
echo [成功] 已创建 "本地工具配置说明.txt"
echo.

:: 完成
echo ========================================
echo     项目配置完成！
echo ========================================
echo.
echo 现在你可以使用以下方式启动应用：
echo   1. 双击 "启动宠物管理系统-本地工具.bat" （推荐，使用自动下载的工具）
echo   2. 双击 "启动宠物管理系统.bat" （使用系统安装的Java）
echo.
echo 开发命令:
echo   - 编译项目: %MVN_CMD% -s "%MAVEN_SETTINGS%" compile
echo   - 运行项目: %MVN_CMD% -s "%MAVEN_SETTINGS%" spring-boot:run
echo   - 打包项目: %MVN_CMD% -s "%MAVEN_SETTINGS%" clean package
echo.
pause
exit /b 0

:: ========================================
:: 函数：下载并安装 Java
:: ========================================
:download_and_install_java
echo.
echo [信息] 正在下载 Java ...
echo        下载地址: %JAVA_DOWNLOAD_URL%
echo        保存位置: %TOOLS_DIR%\java.zip
echo.

:: 检查是否已经下载过
if exist "%TOOLS_DIR%\java.zip" (
    echo [信息] 检测到已下载的 Java 安装包，跳过下载
) else (
    :: 使用 PowerShell 下载
    powershell -Command "Invoke-WebRequest -Uri '%JAVA_DOWNLOAD_URL%' -OutFile '%TOOLS_DIR%\java.zip' -UseBasicParsing"
    if %errorlevel% neq 0 (
        echo [错误] Java 下载失败！
        echo.
        pause
        exit /b 1
    )
)

:: 解压 Java
echo [信息] 正在解压 Java ...
if exist "%JAVA_DIR%" rmdir /s /q "%JAVA_DIR%"
powershell -Command "Expand-Archive -Path '%TOOLS_DIR%\java.zip' -DestinationPath '%TOOLS_DIR%' -Force"
if %errorlevel% neq 0 (
    echo [错误] Java 解压失败！
    echo.
    pause
    exit /b 1
)

:: 重命名解压后的文件夹（处理可能的不同目录名）
for /d %%d in ("%TOOLS_DIR%\jdk*") do (
    if not "%%d"=="%JAVA_DIR%" (
        ren "%%d" "jdk-17.0.11+9"
    )
)

:: 设置 JAVA_HOME 和 PATH
set "JAVA_HOME=%JAVA_DIR%"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo [成功] Java 安装完成！
echo        安装位置: %JAVA_DIR%
echo.
exit /b 0

:: ========================================
:: 函数：下载并安装 Maven
:: ========================================
:download_and_install_maven
echo.
echo [信息] 正在下载 Maven ...
echo        下载地址: %MAVEN_DOWNLOAD_URL%
echo        保存位置: %TOOLS_DIR%\maven.zip
echo.

:: 检查是否已经下载过
if exist "%TOOLS_DIR%\maven.zip" (
    echo [信息] 检测到已下载的 Maven 安装包，跳过下载
) else (
    :: 使用 PowerShell 下载
    powershell -Command "Invoke-WebRequest -Uri '%MAVEN_DOWNLOAD_URL%' -OutFile '%TOOLS_DIR%\maven.zip' -UseBasicParsing"
    if %errorlevel% neq 0 (
        echo [错误] Maven 下载失败！
        echo.
        pause
        exit /b 1
    )
)

:: 解压 Maven
echo [信息] 正在解压 Maven ...
if exist "%MAVEN_DIR%" rmdir /s /q "%MAVEN_DIR%"
powershell -Command "Expand-Archive -Path '%TOOLS_DIR%\maven.zip' -DestinationPath '%TOOLS_DIR%' -Force"
if %errorlevel% neq 0 (
    echo [错误] Maven 解压失败！
    echo.
    pause
    exit /b 1
)

:: 设置 PATH 和 MVN_CMD
set "PATH=%MAVEN_DIR%\bin;%PATH%"
set "MVN_CMD=%MAVEN_DIR%\bin\mvn.cmd"

echo [成功] Maven 安装完成！
echo        安装位置: %MAVEN_DIR%
echo.
exit /b 0

:: ========================================
:: 函数：配置 Maven 国内镜像
:: ========================================
:configure_maven_mirror
echo [信息] 创建 Maven 配置文件...
(
    echo ^<?xml version="1.0" encoding="UTF-8"?^>
    echo ^<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    echo           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    echo           xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd"^>
    echo   ^<mirrors^>
    echo     ^<mirror^>
    echo       ^<id^>aliyunmaven^</id^>
    echo       ^<mirrorOf^>*^</mirrorOf^>
    echo       ^<name^>阿里云公共仓库^</name^>
    echo       ^<url^>https://maven.aliyun.com/repository/public^</url^>
    echo     ^</mirror^>
    echo   ^</mirrors^>
    echo   ^<profiles^>
    echo     ^<profile^>
    echo       ^<id^>aliyun^</id^>
    echo       ^<repositories^>
    echo         ^<repository^>
    echo           ^<id^>aliyun^</id^>
    echo           ^<url^>https://maven.aliyun.com/repository/public^</url^>
    echo           ^<releases^>
    echo             ^<enabled^>true^</enabled^>
    echo           ^</releases^>
    echo           ^<snapshots^>
    echo             ^<enabled^>false^</enabled^>
    echo           ^</snapshots^>
    echo         ^</repository^>
    echo       ^</repositories^>
    echo       ^<pluginRepositories^>
    echo         ^<pluginRepository^>
    echo           ^<id^>aliyun-plugin^</id^>
    echo           ^<url^>https://maven.aliyun.com/repository/public^</url^>
    echo           ^<releases^>
    echo             ^<enabled^>true^</enabled^>
    echo           ^</releases^>
    echo           ^<snapshots^>
    echo             ^<enabled^>false^</enabled^>
    echo           ^</snapshots^>
    echo         ^</pluginRepository^>
    echo       ^</pluginRepositories^>
    echo     ^</profile^>
    echo   ^</profiles^>
    echo   ^<activeProfiles^>
    echo     ^<activeProfile^>aliyun^</activeProfile^>
    echo   ^</activeProfiles^>
    echo ^</settings^>
) > "%MAVEN_SETTINGS%"

echo [信息] Maven 配置文件已创建: %MAVEN_SETTINGS%
exit /b 0
