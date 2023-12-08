package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Admin;
import models.Dao;
import application.Main;
import utils.BcryptHash;

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
    private Dao dao;

    public SignupController() {
        dao = new Dao();
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
        int userId = dao.createUser(username, password, "admin");

        if (userId != -1) {
        	Admin admin = new Admin(userId, name, email, position, userId);
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
