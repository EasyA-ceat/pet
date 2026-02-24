package com.pet.management;

import java.io.IOException;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        // 启用硬件加速
        System.setProperty("prism.order", "d3d,sw");
        System.setProperty("prism.forceGPU", "true");
        System.setProperty("prism.vsync", "true");
        
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
        Scene scene = new Scene(root, 1280, 720);
        
        // 加载现代化设计样式
        scene.getStylesheets().add(getClass().getResource("/css/modern-design.css").toExternalForm());
        
        // 启用抗锯齿
        scene.setFill(javafx.scene.paint.Color.valueOf("#FAFAFA"));
        
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
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
