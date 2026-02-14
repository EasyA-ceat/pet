package com.pet.management;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class MainApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        this.context = new SpringApplicationBuilder(PetManagementSystem.class)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // 设置应用程序图标（改进的容错处理）
        try {
            // 尝试加载自定义图标
            Image icon = new Image(getClass().getResourceAsStream("/images/icon.png"));
            if (icon != null && !icon.isError()) {
                primaryStage.getIcons().add(icon);
            } else {
                // 如果自定义图标不存在，使用系统默认图标
                System.out.println("自定义图标未找到，使用系统默认图标");
            }
        } catch (Exception e) {
            System.out.println("图标加载失败: " + e.getMessage() + "，使用系统默认图标");
        }
        
        // 设置应用程序标题
        primaryStage.setTitle("宠物管理系统");
        
        // 加载主界面
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        
        Pane root = fxmlLoader.load();
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // 设置关闭操作
        primaryStage.setOnCloseRequest(event -> {
            context.close();
            Platform.exit();
        });
    }

    @Override
    public void stop() {
        context.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
