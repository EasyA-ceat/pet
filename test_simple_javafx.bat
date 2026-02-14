@echo off
chcp 65001 >nul
echo ==============================================
echo 宠物管理系统 - JavaFX界面测试
echo ==============================================
echo.

:: 设置编码为UTF-8
chcp 65001

:: 检查Java安装
if not defined JAVA_HOME (
    for /f "tokens=2 delims==" %%i in ('wmic os get localdatetime /value') do if "%%i" neq "" set t=%%i
    set "CURRENT_DIR=%CD%"
    
    echo 正在查找Java安装目录...
    for /f "tokens=1,2*" %%i in ('reg query "HKLM\Software\JavaSoft\Java Runtime Environment" /v CurrentVersion 2^>nul') do (
        if "%%i"=="CurrentVersion" (
            for /f "tokens=1,2*" %%a in ('reg query "HKLM\Software\JavaSoft\Java Runtime Environment\%%j" /v JavaHome 2^>nul') do (
                if "%%a"=="JavaHome" (
                    set "JAVA_HOME=%%c"
                )
            )
        )
    )
    
    if not defined JAVA_HOME (
        echo.
        echo 错误：未找到Java安装目录！
        echo 请检查Java是否正确安装。
        pause
        exit /b 1
    )
)

echo Java安装目录：%JAVA_HOME%
echo.

:: 编译简单测试程序
echo 正在编译JavaFX测试程序...
javac -encoding UTF-8 -d . TestJavaFX.java

if %errorlevel% neq 0 (
    echo.
    echo 编译错误！
    pause
    exit /b 1
)

echo 编译成功！
echo.

:: 运行JavaFX应用程序
echo 正在运行JavaFX应用程序...
java --module-path "%JAVA_HOME%\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base TestJavaFX

if %errorlevel% equ 0 (
    echo.
    echo ==============================================
    echo 成功！JavaFX界面已正常显示。
    echo ==============================================
    echo.
    echo 测试程序功能：
    echo 1. 显示简单的JavaFX窗口
    echo 2. 包含标题、标签和按钮
    echo 3. 能响应按钮点击事件
    echo.
    echo 这验证了：
    echo - JavaFX模块正确安装
    echo - 模块路径配置正确
    echo - 窗口能正常打开和关闭
    pause
) else (
    echo.
    echo ==============================================
    echo 运行失败！
    echo ==============================================
    echo.
    echo 可能的原因：
    echo 1. JavaFX模块未正确配置
    echo 2. 缺少必要的JavaFX模块
    echo 3. 模块路径不正确
    echo.
    echo 请检查是否安装了完整的JDK，或使用以下命令检查JavaFX状态：
    echo   java --list-modules
    echo.
    pause
)

:: 清理编译文件
del /q TestJavaFX.class
exit /b
