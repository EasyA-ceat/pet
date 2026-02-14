import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.InputStream;

public class TestJavaFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 设置窗口标题
        primaryStage.setTitle("JavaFX测试应用程序");
        
        // 创建界面组件
        Label titleLabel = new Label("宠物管理系统");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #3498db; -fx-font-weight: bold;");
        
        Label infoLabel = new Label("JavaFX界面能正常显示！");
        infoLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2ecc71;");
        
        Label projectInfoLabel = new Label("项目架构已完成：");
        projectInfoLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-margin: 10px 0 5px 0;");
        
        Label[] features = {
            new Label("• Spring Boot架构"),
            new Label("• JavaFX界面"),
            new Label("• 完整的业务层"),
            new Label("• 数据库模型")
        };
        
        for (Label feature : features) {
            feature.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        }
        
        Button closeButton = new Button("关闭");
        closeButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8px 16px; -fx-background-radius: 4px;");
        closeButton.setOnAction(event -> primaryStage.close());
        
        // 布局组件
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 30px; -fx-alignment: center; -fx-background-color: #f5f7fa;");
        root.getChildren().add(titleLabel);
        root.getChildren().add(infoLabel);
        root.getChildren().add(projectInfoLabel);
        
        for (Label feature : features) {
            root.getChildren().add(feature);
        }
        
        root.getChildren().add(closeButton);
        
        // 创建场景
        Scene scene = new Scene(root, 400, 350);
        
        // 设置场景到舞台
        primaryStage.setScene(scene);
        
        // 显示窗口
        primaryStage.show();
        
        System.out.println("JavaFX界面已成功显示！");
    }
}
