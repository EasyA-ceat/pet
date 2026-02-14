package com.pet.management.view.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pet.management.model.Customer;
import com.pet.management.model.Photo;
import com.pet.management.service.CustomerService;
import com.pet.management.service.PhotoService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private PhotoService photoService;

    @FXML
    private TextField searchField;
    @FXML
    private Button searchBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Long> idColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> petNameColumn;
    @FXML
    private TableColumn<Customer, String> petTypeColumn;
    @FXML
    private TableColumn<Customer, String> petBreedColumn;
    @FXML
    private TableColumn<Customer, Integer> petAgeColumn;
    @FXML
    private TableColumn<Customer, LocalDateTime> createTimeColumn;
    @FXML
    private Label totalCustomersLabel;
    @FXML
    private Label activeCustomersLabel;
    @FXML
    private Label recentCustomersLabel;
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // 初始化表格列
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        petNameColumn.setCellValueFactory(new PropertyValueFactory<>("petName"));
        petTypeColumn.setCellValueFactory(new PropertyValueFactory<>("petType"));
        petBreedColumn.setCellValueFactory(new PropertyValueFactory<>("petBreed"));
        petAgeColumn.setCellValueFactory(new PropertyValueFactory<>("petAge"));
        createTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createTime"));

        // 添加操作列
        TableColumn<Customer, Void> actionColumn = new TableColumn<>("操作");
        actionColumn.setPrefWidth(150);
        customerTable.getColumns().add(actionColumn);

        // 绑定事件
        searchBtn.setOnAction(event -> searchCustomers());
        addBtn.setOnAction(event -> addCustomer());
        refreshBtn.setOnAction(event -> loadCustomerData());

        // 初始化加载数据
        loadCustomerData();
        updateStats();
    }

    // 加载顾客数据
    @FXML
    public void loadCustomerData() {
        try {
            List<Customer> customers = customerService.findAll();
            customerTable.getItems().clear();
            customerTable.getItems().addAll(customers);
            statusLabel.setText("数据加载成功");
            updateStats();
        } catch (Exception e) {
            statusLabel.setText("数据加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 添加顾客
    @FXML
    public void addCustomer() {
        try {
            // 创建对话框
            Dialog<Customer> dialog = new Dialog<>();
            dialog.setTitle("添加顾客");
            dialog.setHeaderText("请输入顾客信息");

            // 创建表单
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            TextField customerNameField = new TextField();
            TextField phoneField = new TextField();
            TextField petNameField = new TextField();
            TextField petTypeField = new TextField();
            TextField petBreedField = new TextField();
            TextField petAgeField = new TextField();
            TextArea notesArea = new TextArea();

            grid.add(new Label("顾客姓名:"), 0, 0);
            grid.add(customerNameField, 1, 0);
            grid.add(new Label("联系电话:"), 0, 1);
            grid.add(phoneField, 1, 1);
            grid.add(new Label("宠物姓名:"), 0, 2);
            grid.add(petNameField, 1, 2);
            grid.add(new Label("宠物类型:"), 0, 3);
            grid.add(petTypeField, 1, 3);
            grid.add(new Label("宠物品种:"), 0, 4);
            grid.add(petBreedField, 1, 4);
            grid.add(new Label("宠物年龄:"), 0, 5);
            grid.add(petAgeField, 1, 5);
            grid.add(new Label("备注:"), 0, 6);
            grid.add(notesArea, 1, 6);

            dialog.getDialogPane().setContent(grid);

            // 添加按钮
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // 设置结果转换器
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    Customer customer = new Customer();
                    customer.setCustomerName(customerNameField.getText());
                    customer.setPhone(phoneField.getText());
                    customer.setPetName(petNameField.getText());
                    customer.setPetType(petTypeField.getText());
                    customer.setPetBreed(petBreedField.getText());
                    if (!petAgeField.getText().isEmpty()) {
                        customer.setPetAge(Integer.parseInt(petAgeField.getText()));
                    }
                    customer.setNotes(notesArea.getText());
                    return customer;
                }
                return null;
            });

            // 显示对话框
            Optional<Customer> result = dialog.showAndWait();

            if (result.isPresent()) {
                Customer customer = result.get();
                customerService.save(customer);
                loadCustomerData();
                statusLabel.setText("顾客添加成功");
            }
        } catch (Exception e) {
            statusLabel.setText("添加顾客失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 编辑顾客
    @FXML
    public void editCustomer() {
        try {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                statusLabel.setText("请先选择要编辑的顾客");
                return;
            }

            // 创建对话框
            Dialog<Customer> dialog = new Dialog<>();
            dialog.setTitle("编辑顾客");
            dialog.setHeaderText("请编辑顾客信息");

            // 创建表单
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            TextField customerNameField = new TextField(selectedCustomer.getCustomerName());
            TextField phoneField = new TextField(selectedCustomer.getPhone());
            TextField petNameField = new TextField(selectedCustomer.getPetName());
            TextField petTypeField = new TextField(selectedCustomer.getPetType());
            TextField petBreedField = new TextField(selectedCustomer.getPetBreed());
            TextField petAgeField = new TextField(selectedCustomer.getPetAge() != null ? String.valueOf(selectedCustomer.getPetAge()) : "");
            TextArea notesArea = new TextArea(selectedCustomer.getNotes());

            grid.add(new Label("顾客姓名:"), 0, 0);
            grid.add(customerNameField, 1, 0);
            grid.add(new Label("联系电话:"), 0, 1);
            grid.add(phoneField, 1, 1);
            grid.add(new Label("宠物姓名:"), 0, 2);
            grid.add(petNameField, 1, 2);
            grid.add(new Label("宠物类型:"), 0, 3);
            grid.add(petTypeField, 1, 3);
            grid.add(new Label("宠物品种:"), 0, 4);
            grid.add(petBreedField, 1, 4);
            grid.add(new Label("宠物年龄:"), 0, 5);
            grid.add(petAgeField, 1, 5);
            grid.add(new Label("备注:"), 0, 6);
            grid.add(notesArea, 1, 6);

            dialog.getDialogPane().setContent(grid);

            // 添加按钮
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // 设置结果转换器
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    selectedCustomer.setCustomerName(customerNameField.getText());
                    selectedCustomer.setPhone(phoneField.getText());
                    selectedCustomer.setPetName(petNameField.getText());
                    selectedCustomer.setPetType(petTypeField.getText());
                    selectedCustomer.setPetBreed(petBreedField.getText());
                    if (!petAgeField.getText().isEmpty()) {
                        selectedCustomer.setPetAge(Integer.parseInt(petAgeField.getText()));
                    }
                    selectedCustomer.setNotes(notesArea.getText());
                    return selectedCustomer;
                }
                return null;
            });

            // 显示对话框
            Optional<Customer> result = dialog.showAndWait();

            if (result.isPresent()) {
                Customer customer = result.get();
                customerService.save(customer);
                loadCustomerData();
                statusLabel.setText("顾客编辑成功");
            }
        } catch (Exception e) {
            statusLabel.setText("编辑顾客失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 删除顾客
    @FXML
    public void deleteCustomer() {
        try {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                statusLabel.setText("请先选择要删除的顾客");
                return;
            }

            // 确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("确认删除");
            alert.setHeaderText("确定要删除该顾客吗？");
            alert.setContentText("此操作不可恢复，删除后该顾客的所有相关数据也会被删除。");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                customerService.deleteById(selectedCustomer.getId());
                loadCustomerData();
                statusLabel.setText("顾客删除成功");
            }
        } catch (Exception e) {
            statusLabel.setText("删除顾客失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 搜索顾客
    @FXML
    public void searchCustomers() {
        try {
            String keyword = searchField.getText();
            List<Customer> customers = customerService.search(keyword);
            customerTable.getItems().clear();
            customerTable.getItems().addAll(customers);
            statusLabel.setText("搜索完成，共找到 " + customers.size() + " 条记录");
            updateStats();
        } catch (Exception e) {
            statusLabel.setText("搜索顾客失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 上传照片
    @FXML
    public void uploadPhoto() {
        try {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                statusLabel.setText("请先选择要上传照片的顾客");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择照片");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                Photo photo = photoService.uploadPhoto(selectedFile, selectedCustomer.getId(), null, "other", "顾客照片");
                statusLabel.setText("照片上传成功: " + photo.getFileName());
            }
        } catch (Exception e) {
            statusLabel.setText("上传照片失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 查看照片
    @FXML
    public void viewPhotos() {
        try {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                statusLabel.setText("请先选择要查看照片的顾客");
                return;
            }

            List<Photo> photos = photoService.findByCustomerId(selectedCustomer.getId());
            if (photos.isEmpty()) {
                statusLabel.setText("该顾客暂无照片");
                return;
            }

            // 创建照片查看对话框
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("照片查看");
            dialog.setHeaderText("顾客: " + selectedCustomer.getCustomerName() + " 的照片");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            int row = 0;
            for (Photo photo : photos) {
                // 显示缩略图
                File thumbnailFile = new File(photoService.getThumbnailPath(photo));
                if (thumbnailFile.exists()) {
                    Image image = new Image(thumbnailFile.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);

                    grid.add(imageView, 0, row);
                    grid.add(new Label(photo.getFileName()), 1, row);
                    row++;
                }
            }

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
            dialog.showAndWait();
        } catch (Exception e) {
            statusLabel.setText("查看照片失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 删除照片
    @FXML
    public void deletePhoto() {
        try {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                statusLabel.setText("请先选择要删除照片的顾客");
                return;
            }

            List<Photo> photos = photoService.findByCustomerId(selectedCustomer.getId());
            if (photos.isEmpty()) {
                statusLabel.setText("该顾客暂无照片");
                return;
            }

            // 创建照片选择对话框
            Dialog<Photo> dialog = new Dialog<>();
            dialog.setTitle("选择要删除的照片");
            dialog.setHeaderText("请选择要删除的照片");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            ComboBox<Photo> photoComboBox = new ComboBox<>();
            photoComboBox.getItems().addAll(photos);
            photoComboBox.setCellFactory(lv -> new ListCell<Photo>() {
                @Override
                protected void updateItem(Photo item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getFileName());
                }
            });
            photoComboBox.setButtonCell(new ListCell<Photo>() {
                @Override
                protected void updateItem(Photo item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? "选择照片" : item.getFileName());
                }
            });

            grid.add(new Label("照片:"), 0, 0);
            grid.add(photoComboBox, 1, 0);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return photoComboBox.getValue();
                }
                return null;
            });

            Optional<Photo> result = dialog.showAndWait();
            if (result.isPresent()) {
                Photo photo = result.get();
                photoService.deleteById(photo.getId());
                statusLabel.setText("照片删除成功");
            }
        } catch (Exception e) {
            statusLabel.setText("删除照片失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 更新统计信息
    private void updateStats() {
        try {
            List<Customer> allCustomers = customerService.findAll();
            totalCustomersLabel.setText("总顾客数: " + allCustomers.size());

            // 简单的活跃顾客统计（最近一个月内有交易的顾客）
            // 这里简化处理，实际应根据交易记录判断
            activeCustomersLabel.setText("活跃顾客: " + allCustomers.size());

            // 本月新增顾客统计
            int recentCount = 0;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

            for (Customer customer : allCustomers) {
                if (customer.getCreateTime().isAfter(startOfMonth)) {
                    recentCount++;
                }
            }

            recentCustomersLabel.setText("本月新增: " + recentCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
