package com.pet.management.view.controller;

import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

@Controller
public class AboutController {

    @FXML
    private Button closeButton;

    @FXML
    private void initialize() {
        // 关闭按钮事件处理
        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }
}