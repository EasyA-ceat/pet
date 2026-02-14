import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SimpleJavaFXTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("JavaFX界面能正常显示！");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 300, 200);
        
        primaryStage.setTitle("JavaFX测试");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        System.out.println("JavaFX界面已成功显示！");
    }

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        launch(args);
    }
}
