@echo off
echo 正在运行JavaFX测试程序...
echo.

:: 设置JavaFX模块路径
set JAVA_FX_MODULES=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web

:: 尝试直接运行测试程序
echo 方法1: 使用java命令直接运行JavaFX应用程序...
java --module-path "%JAVA_HOME%\lib" --add-modules %JAVA_FX_MODULES% -cp .;src\main\java SimpleJavaFXTest

if %errorlevel% equ 0 (
    echo.
    echo JavaFX界面运行成功！
    pause
    exit /b 0
)

echo.
echo 方法1失败，尝试其他方法...
echo.

:: 尝试使用javac编译然后运行
echo 方法2: 编译并运行JavaFX应用程序...
javac -cp src\main\java -d . src\main\java\com\pet\management\*.java src\main\java\com\pet\management\view\controller\*.java src\main\java\com\pet\management\model\*.java src\main\java\com\pet\management\repository\*.java src\main\java\com\pet\management\service\*.java

if %errorlevel% equ 0 (
    echo 编译成功，现在运行应用程序...
    java --module-path "%JAVA_HOME%\lib" --add-modules %JAVA_FX_MODULES% -cp . com.pet.management.MainApplication
    
    if %errorlevel% equ 0 (
        echo.
        echo JavaFX界面运行成功！
        pause
        exit /b 0
    )
)

echo.
echo 所有方法都失败了。请检查：
echo 1. JavaFX模块是否正确安装
echo 2. 类路径是否正确
echo 3. 文件路径是否存在

echo.
pause
