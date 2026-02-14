@echo off
chcp 65001 >nul
echo ==============================================
echo 宠物管理系统 - 图形界面测试
echo ==============================================
echo.

:: 编译简单的AWT程序
echo 正在编译测试程序...
javac -encoding UTF-8 TestJavaFXSimple.java

if %errorlevel% neq 0 (
    echo.
    echo 编译错误！
    echo 请检查Java是否正确安装。
    pause
    exit /b 1
)

echo 编译成功！
echo.

:: 运行AWT应用程序
echo 正在运行图形界面测试...
java TestJavaFXSimple

if %errorlevel% equ 0 (
    echo.
    echo ==============================================
    echo 成功！图形界面能正常显示。
    echo ==============================================
    echo.
    echo 测试程序功能：
    echo 1. 显示简单的AWT窗口
    echo 2. 包含标题、标签和按钮
    echo 3. 能响应按钮点击事件
    echo.
    echo 这验证了：
    echo - Java图形界面系统正常工作
    echo - 您的系统支持图形界面显示
    echo - 可以继续开发JavaFX应用程序
) else (
    echo.
    echo ==============================================
    echo 运行失败！
    echo ==============================================
    echo.
    echo 可能的原因：
    echo 1. Java图形界面库损坏
    echo 2. 系统配置问题
    echo 3. 安全设置阻止了图形界面
    echo.
    echo 请检查：
    echo 1. Java是否正确安装
    echo 2. 系统图形驱动是否正常
    echo 3. 是否有安全软件阻止了图形界面
)

:: 清理编译文件
del /q TestJavaFXSimple.class
pause
exit /b
