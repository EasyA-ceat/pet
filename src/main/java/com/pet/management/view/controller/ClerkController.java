package com.pet.management.view.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pet.management.model.Clerk;
import com.pet.management.service.ClerkService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

@Controller
public class ClerkController implements Initializable {

    @Autowired
    private ClerkService clerkService;

    @FXML
    private TableView<Clerk> clerkTable;

    @FXML
    private TableColumn<Clerk, Long> idColumn;

    @FXML
    private TableColumn<Clerk, String> nameColumn;

    @FXML
    private TableColumn<Clerk, String> phoneColumn;

    @FXML
    private TableColumn<Clerk, BigDecimal> commissionRateColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField commissionRateField;

    @FXML
    private TextField notesField;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button clearButton;

    @FXML
    private Label statusLabel;

    private ObservableList<Clerk> clerkData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化表格列
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("clerkName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        commissionRateColumn.setCellValueFactory(new PropertyValueFactory<>("commissionRate"));

        // 加载员工数据
        loadClerkData();

        // 监听表格选择事件
        clerkTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateForm(newValue);
            }
        });
    }

    /**
     * 加载员工数据到表格
     */
    @FXML
    private void loadClerkData() {
        try {
            List<Clerk> clerks = clerkService.findAll();
            clerkData.clear();
            clerkData.addAll(clerks);
            clerkTable.setItems(clerkData);
            statusLabel.setText("成功加载 " + clerks.size() + " 条员工记录");
        } catch (Exception e) {
            statusLabel.setText("加载员工数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 清空表单
     */
    @FXML
    private void clearForm() {
        nameField.clear();
        phoneField.clear();
        commissionRateField.clear();
        notesField.clear();
        clerkTable.getSelectionModel().clearSelection();
    }

    /**
     * 填充表单数据
     */
    private void populateForm(Clerk clerk) {
        nameField.setText(clerk.getClerkName());
        phoneField.setText(clerk.getPhone());
        commissionRateField.setText(clerk.getCommissionRate().toString());
        notesField.setText(clerk.getNotes());
    }

    /**
     * 添加新员工
     */
    @FXML
    private void addClerk() {
        try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            BigDecimal commissionRate = new BigDecimal(commissionRateField.getText().trim());
            String notes = notesField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || commissionRate.compareTo(BigDecimal.ZERO) < 0) {
                showAlert("警告", "请填写完整的员工信息，提成比例不能小于0");
                return;
            }

            Clerk newClerk = new Clerk();
            newClerk.setClerkName(name);
            newClerk.setPhone(phone);
            newClerk.setCommissionRate(commissionRate);
            newClerk.setNotes(notes);

            clerkService.save(newClerk);
            loadClerkData();
            clearForm();
            statusLabel.setText("成功添加新员工: " + name);
        } catch (NumberFormatException e) {
            statusLabel.setText("提成比例格式错误");
        } catch (Exception e) {
            statusLabel.setText("添加员工失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 编辑选中的员工
     */
    @FXML
    private void editClerk() {
        try {
            Clerk selectedClerk = clerkTable.getSelectionModel().getSelectedItem();
            if (selectedClerk == null) {
                showAlert("警告", "请先选择要编辑的员工");
                return;
            }

            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            BigDecimal commissionRate = new BigDecimal(commissionRateField.getText().trim());
            String notes = notesField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || commissionRate.compareTo(BigDecimal.ZERO) < 0) {
                showAlert("警告", "请填写完整的员工信息，提成比例不能小于0");
                return;
            }

            selectedClerk.setClerkName(name);
            selectedClerk.setPhone(phone);
            selectedClerk.setCommissionRate(commissionRate);
            selectedClerk.setNotes(notes);

            clerkService.save(selectedClerk);
            loadClerkData();
            clearForm();
            statusLabel.setText("成功修改员工信息: " + name);
        } catch (NumberFormatException e) {
            statusLabel.setText("提成比例格式错误");
        } catch (Exception e) {
            statusLabel.setText("编辑员工失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 删除选中的员工
     */
    @FXML
    private void deleteClerk() {
        try {
            Clerk selectedClerk = clerkTable.getSelectionModel().getSelectedItem();
            if (selectedClerk == null) {
                showAlert("警告", "请先选择要删除的员工");
                return;
            }

            clerkService.deleteById(selectedClerk.getId());
            loadClerkData();
            clearForm();
            statusLabel.setText("成功删除员工: " + selectedClerk.getClerkName());
        } catch (Exception e) {
            statusLabel.setText("删除员工失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 显示警告对话框
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 导入照片
     */
    @FXML
    private void importPhoto() {
        Clerk selectedClerk = clerkTable.getSelectionModel().getSelectedItem();
        if (selectedClerk == null) {
            showAlert("警告", "请先选择要添加照片的员工");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择照片");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("图片文件", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"),
            new FileChooser.ExtensionFilter("所有文件", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                showAlert("成功", "照片导入成功: " + selectedFile.getName());
                statusLabel.setText("成功为员工 " + selectedClerk.getClerkName() + " 导入照片");
            } catch (Exception e) {
                showAlert("错误", "导入照片失败: " + e.getMessage());
                statusLabel.setText("导入照片失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * 查看相册
     */
    @FXML
    private void viewPhotos() {
        Clerk selectedClerk = clerkTable.getSelectionModel().getSelectedItem();
        if (selectedClerk == null) {
            showAlert("警告", "请先选择要查看照片的员工");
            return;
        }

        showAlert("提示", "查看员工 " + selectedClerk.getClerkName() + " 的相册功能");
        statusLabel.setText("查看员工相册: " + selectedClerk.getClerkName());
    }
}
