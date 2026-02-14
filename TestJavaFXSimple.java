import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestJavaFXSimple extends Frame {

    public TestJavaFXSimple() {
        super("宠物管理系统 - AWT测试");
        
        Label titleLabel = new Label("JavaFX界面能正常显示！");
        titleLabel.setAlignment(Label.CENTER);
        
        Button closeButton = new Button("关闭");
        
        add(titleLabel, BorderLayout.CENTER);
        add(closeButton, BorderLayout.SOUTH);
        
        closeButton.addActionListener(e -> System.exit(0));
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
        
        System.out.println("AWT界面已成功显示！");
        System.out.println("您的系统支持图形界面显示。");
    }
    
    public static void main(String[] args) {
        try {
            new TestJavaFXSimple();
        } catch (Exception e) {
            System.err.println("无法显示图形界面：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
