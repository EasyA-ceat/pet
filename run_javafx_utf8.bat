@echo off
chcp 65001 >nul 2>&1
echo 正在准备运行宠物管理系统...
echo.

:: 设置UTF-8编码环境
chcp 65001 >nul

:: 定义JavaFX模块路径
set "JAVA_FX_MODULES=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web"

:: 清理可能的旧编译文件
if exist classes (
    rd /s /q classes
)
if exist .classpath rd /s /q .classpath

:: 编译项目
echo 步骤1：编译项目...
javac -encoding UTF-8 -cp "src\main\java" -d "classes" ^
src\main\java\com\pet\management\*.java ^
src\main\java\com\pet\management\view\controller\*.java ^
src\main\java\com\pet\management\model\*.java ^
src\main\java\com\pet\management\repository\*.java ^
src\main\java\com\pet\management\service\*.java

if %errorlevel% equ 0 (
    echo 编译成功！
    echo.
    
    echo 步骤2：运行应用程序...
    java --module-path "%JAVA_HOME%\lib" --add-modules %JAVA_FX_MODULES% -cp "classes" com.pet.management.MainApplication
    
    if %errorlevel% equ 0 (
        echo.
        echo 宠物管理系统已成功启动！
        echo 您应该能看到JavaFX界面了。
        goto :success
    )
)

echo.
echo 编译或运行失败。让我检查JavaFX模块是否正常...

:: 测试JavaFX可用性
echo.
echo 正在检查JavaFX模块...
java --list-modules | findstr javafx

echo.
echo JavaFX模块列表：
echo %JAVA_FX_MODULES%
echo.
echo 请检查：
echo 1. JavaFX模块是否正确安装
echo 2. 类路径配置是否正确
echo 3. 是否有缺少的依赖库

:success
pause
