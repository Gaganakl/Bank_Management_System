package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.AdminModel;
import models.DaoModel;
import application.Main;


public class SignupController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField positionField;
    @FXML
    private Label signupMessageLabel;

    private Main main;
    private DaoModel dao;

    public SignupController() {
        dao = new DaoModel();
    }

    @FXML
    private void handleSignup() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String position = positionField.getText();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || position.isEmpty()) {
            signupMessageLabel.setText("Please fill in all fields.");
            return;
        }

//        String hashedPassword = BcryptHash.hashPassword(password);
        int userId = dao.createUser(username, password, "customer");

        if (userId != -1) {
        	AdminModel admin = new AdminModel(userId, name, email, position, userId);
            if (dao.addCustomer(admin)) {
                signupMessageLabel.setText("Admin account created successfully.");
            } else {
                signupMessageLabel.setText("Failed to add admin.");
            }
        } else {
            signupMessageLabel.setText("Failed to create user for the admin.");
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
