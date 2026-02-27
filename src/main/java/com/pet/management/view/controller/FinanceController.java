package com.pet.management.view.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pet.management.model.Clerk;
import com.pet.management.model.Customer;
import com.pet.management.model.Transaction;
import com.pet.management.service.ClerkService;
import com.pet.management.service.CustomerService;
import com.pet.management.service.TransactionService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@Controller
public class FinanceController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ClerkService clerkService;

    @FXML
    private TextField searchField;
    @FXML
    private Button searchBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button calculateCommissionBtn;
    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, Long> idColumn;
    @FXML
    private TableColumn<Transaction, String> customerNameColumn;
    @FXML
    private TableColumn<Transaction, String> petNameColumn;
    @FXML
    private TableColumn<Transaction, String> clerkNameColumn;
    @FXML
    private TableColumn<Transaction, String> serviceTypeColumn;
    @FXML
    private TableColumn<Transaction, LocalDateTime> transactionDateColumn;
    @FXML
    private TableColumn<Transaction, BigDecimal> amountColumn;
    @FXML
    private TableColumn<Transaction, BigDecimal> commissionColumn;
    @FXML
    private TableColumn<Transaction, String> notesColumn;
    @FXML
    private Label totalAmountLabel;
    @FXML
    private Label totalCommissionLabel;
    @FXML
    private Label transactionCountLabel;
    @FXML
    private Label averageAmountLabel;
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // 初始化表格列
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(cellData -> {
            Customer customer = cellData.getValue().getCustomer();
            return customer != null ? new javafx.beans.property.SimpleStringProperty(customer.getCustomerName()) : null;
        });
        petNameColumn.setCellValueFactory(cellData -> {
            Customer customer = cellData.getValue().getCustomer();
            return customer != null ? new javafx.beans.property.SimpleStringProperty(customer.getPetName()) : null;
        });
        clerkNameColumn.setCellValueFactory(cellData -> {
            Clerk clerk = cellData.getValue().getClerk();
            return clerk != null ? new javafx.beans.property.SimpleStringProperty(clerk.getClerkName()) : null;
        });
        serviceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        transactionDateColumn.setCellFactory(column -> new TableCell<Transaction, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setCellFactory(column -> new TableCell<Transaction, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item.doubleValue()));
                }
            }
        });
        commissionColumn.setCellValueFactory(new PropertyValueFactory<>("commission"));
        commissionColumn.setCellFactory(column -> new TableCell<Transaction, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item.doubleValue()));
                }
            }
        });
        
        // 添加空值检查以防止NullPointerException
        if (notesColumn != null) {
            notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        } else {
            System.err.println("Warning: notesColumn is null - FXML binding may be missing");
        }

        // 添加操作列
        TableColumn<Transaction, Void> actionColumn = new TableColumn<>("操作");
        actionColumn.setPrefWidth(140);
        actionColumn.setCellFactory(col -> {
            TableCell<Transaction, Void> cell = new TableCell<>() {
                private final Button editBtn = new Button("编辑");
                private final Button deleteBtn = new Button("删除");
                
                {
                    editBtn.getStyleClass().addAll("btn", "btn-sm", "btn-secondary");
                    deleteBtn.getStyleClass().addAll("btn", "btn-sm", "btn-danger");
                    
                    editBtn.setOnAction(event -> {
                        Transaction transaction = getTableView().getItems().get(getIndex());
                        transactionTable.getSelectionModel().select(transaction);
                        editTransaction();
                    });
                    
                    deleteBtn.setOnAction(event -> {
                        Transaction transaction = getTableView().getItems().get(getIndex());
                        transactionTable.getSelectionModel().select(transaction);
                        deleteTransaction();
                    });
                }
                
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox buttons = new HBox(5);
                        buttons.getChildren().addAll(editBtn, deleteBtn);
                        setGraphic(buttons);
                    }
                }
            };
            return cell;
        });
        transactionTable.getColumns().add(actionColumn);

        // 绑定事件
        searchBtn.setOnAction(event -> searchTransactions());
        refreshBtn.setOnAction(event -> loadTransactionData());
        addBtn.setOnAction(event -> addTransaction());
        calculateCommissionBtn.setOnAction(event -> calculateCommission());

        // 初始化加载数据
        loadTransactionData();
        updateStats();
    }

    // 加载交易数据
    @FXML
    public void loadTransactionData() {
        try {
            List<Transaction> transactions = transactionService.findAll();
            transactionTable.getItems().clear();
            transactionTable.getItems().addAll(transactions);
            statusLabel.setText("数据加载成功");
            updateStats();
        } catch (Exception e) {
            statusLabel.setText("数据加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 添加交易记录
    @FXML
    public void addTransaction() {
        try {
            // 创建对话框
            Dialog<Transaction> dialog = new Dialog<>();
            dialog.setTitle("添加交易");
            dialog.setHeaderText("请输入交易信息");

            // 创建表单
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            // 顾客选择
            ComboBox<Customer> customerComboBox = new ComboBox<>();
            List<Customer> customers = customerService.findAll();
            customerComboBox.getItems().addAll(customers);

            // 员工选择
            ComboBox<Clerk> clerkComboBox = new ComboBox<>();
            List<Clerk> clerks = clerkService.findAll();
            clerkComboBox.getItems().addAll(clerks);

            TextField serviceTypeField = new TextField();
            TextField amountField = new TextField();
            TextArea notesArea = new TextArea();

            grid.add(new Label("顾客:"), 0, 0);
            grid.add(customerComboBox, 1, 0);
            grid.add(new Label("员工:"), 0, 1);
            grid.add(clerkComboBox, 1, 1);
            grid.add(new Label("服务类型:"), 0, 2);
            grid.add(serviceTypeField, 1, 2);
            grid.add(new Label("金额:"), 0, 3);
            grid.add(amountField, 1, 3);
            grid.add(new Label("备注:"), 0, 4);
            grid.add(notesArea, 1, 4);

            dialog.getDialogPane().setContent(grid);

            // 添加按钮
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // 设置结果转换器
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    Transaction transaction = new Transaction();
                    transaction.setCustomer(customerComboBox.getValue());
                    transaction.setClerk(clerkComboBox.getValue());
                    transaction.setServiceType(serviceTypeField.getText());
                    transaction.setAmount(new BigDecimal(amountField.getText()));
                    
                    // 计算提成
                    if (clerkComboBox.getValue() != null && amountField.getText() != null && !amountField.getText().isEmpty()) {
                        BigDecimal commissionRate = clerkComboBox.getValue().getCommissionRate();
                        BigDecimal commission = new BigDecimal(amountField.getText()).multiply(commissionRate);
                        transaction.setCommission(commission);
                    }
                    
                    transaction.setNotes(notesArea.getText());
                    return transaction;
                }
                return null;
            });

            // 显示对话框
            Optional<Transaction> result = dialog.showAndWait();

            if (result.isPresent()) {
                Transaction transaction = result.get();
                transactionService.save(transaction);
                loadTransactionData();
                statusLabel.setText("交易添加成功");
            }
        } catch (Exception e) {
            statusLabel.setText("添加交易失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 编辑交易记录
    @FXML
    public void editTransaction() {
        try {
            Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
            if (selectedTransaction == null) {
                statusLabel.setText("请先选择要编辑的交易");
                return;
            }

            // 创建对话框
            Dialog<Transaction> dialog = new Dialog<>();
            dialog.setTitle("编辑交易");
            dialog.setHeaderText("请编辑交易信息");

            // 创建表单
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            // 顾客选择
            ComboBox<Customer> customerComboBox = new ComboBox<>();
            List<Customer> customers = customerService.findAll();
            customerComboBox.getItems().addAll(customers);
            customerComboBox.setValue(selectedTransaction.getCustomer());

            // 员工选择
            ComboBox<Clerk> clerkComboBox = new ComboBox<>();
            List<Clerk> clerks = clerkService.findAll();
            clerkComboBox.getItems().addAll(clerks);
            clerkComboBox.setValue(selectedTransaction.getClerk());

            TextField serviceTypeField = new TextField(selectedTransaction.getServiceType());
            TextField amountField = new TextField(selectedTransaction.getAmount().toString());
            TextArea notesArea = new TextArea(selectedTransaction.getNotes());

            grid.add(new Label("顾客:"), 0, 0);
            grid.add(customerComboBox, 1, 0);
            grid.add(new Label("员工:"), 0, 1);
            grid.add(clerkComboBox, 1, 1);
            grid.add(new Label("服务类型:"), 0, 2);
            grid.add(serviceTypeField, 1, 2);
            grid.add(new Label("金额:"), 0, 3);
            grid.add(amountField, 1, 3);
            grid.add(new Label("备注:"), 0, 4);
            grid.add(notesArea, 1, 4);

            dialog.getDialogPane().setContent(grid);

            // 添加按钮
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // 设置结果转换器
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    selectedTransaction.setCustomer(customerComboBox.getValue());
                    selectedTransaction.setClerk(clerkComboBox.getValue());
                    selectedTransaction.setServiceType(serviceTypeField.getText());
                    selectedTransaction.setAmount(new BigDecimal(amountField.getText()));
                    
                    // 重新计算提成
                    if (clerkComboBox.getValue() != null && amountField.getText() != null && !amountField.getText().isEmpty()) {
                        BigDecimal commissionRate = clerkComboBox.getValue().getCommissionRate();
                        BigDecimal commission = new BigDecimal(amountField.getText()).multiply(commissionRate);
                        selectedTransaction.setCommission(commission);
                    }
                    
                    selectedTransaction.setNotes(notesArea.getText());
                    return selectedTransaction;
                }
                return null;
            });

            // 显示对话框
            Optional<Transaction> result = dialog.showAndWait();

            if (result.isPresent()) {
                Transaction transaction = result.get();
                transactionService.save(transaction);
                loadTransactionData();
                statusLabel.setText("交易编辑成功");
            }
        } catch (Exception e) {
            statusLabel.setText("编辑交易失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 删除交易记录
    @FXML
    public void deleteTransaction() {
        try {
            Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
            if (selectedTransaction == null) {
                statusLabel.setText("请先选择要删除的交易");
                return;
            }

            // 确认对话框
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("确认删除");
            alert.setHeaderText("确定要删除该交易吗？");
            alert.setContentText("此操作不可恢复，删除后相关数据也会被删除。");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                transactionService.deleteById(selectedTransaction.getId());
                loadTransactionData();
                statusLabel.setText("交易删除成功");
            }
        } catch (Exception e) {
            statusLabel.setText("删除交易失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 搜索交易记录
    @FXML
    public void searchTransactions() {
        try {
            String keyword = searchField.getText();
            List<Transaction> transactions = transactionService.findByCustomerNameContaining(keyword);
            transactionTable.getItems().clear();
            transactionTable.getItems().addAll(transactions);
            statusLabel.setText("搜索完成，共找到 " + transactions.size() + " 条记录");
            updateStats();
        } catch (Exception e) {
            statusLabel.setText("搜索交易失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 计算提成
    @FXML
    public void calculateCommission() {
        try {
            BigDecimal totalCommission = BigDecimal.ZERO;
            List<Transaction> transactions = transactionService.findAll();
            
            for (Transaction transaction : transactions) {
                if (transaction.getCommission() != null) {
                    totalCommission = totalCommission.add(transaction.getCommission());
                }
            }
            
            statusLabel.setText("总提成: " + String.format("%.2f", totalCommission.doubleValue()));
        } catch (Exception e) {
            statusLabel.setText("计算提成失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 更新统计信息
    private void updateStats() {
        try {
            List<Transaction> allTransactions = transactionService.findAll();
            transactionCountLabel.setText("交易数: " + allTransactions.size());

            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalCommission = BigDecimal.ZERO;

            for (Transaction transaction : allTransactions) {
                if (transaction.getAmount() != null) {
                    totalAmount = totalAmount.add(transaction.getAmount());
                }
                if (transaction.getCommission() != null) {
                    totalCommission = totalCommission.add(transaction.getCommission());
                }
            }

            totalAmountLabel.setText("总金额: " + String.format("%.2f", totalAmount.doubleValue()));
            totalCommissionLabel.setText("总提成: " + String.format("%.2f", totalCommission.doubleValue()));
            
            // 计算平均交易额
            BigDecimal averageAmount = BigDecimal.ZERO;
            if (allTransactions.size() > 0) {
                averageAmount = totalAmount.divide(new BigDecimal(allTransactions.size()), 2, java.math.RoundingMode.HALF_UP);
            }
            if (averageAmountLabel != null) {
                averageAmountLabel.setText("¥" + String.format("%.2f", averageAmount.doubleValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
