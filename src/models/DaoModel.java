package models;

import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;


public class DaoModel {
	
    private void log(String message) {
        System.out.println("[Dao] " + message);
    }
    
    
    // Add Employee
    public boolean addCustomer(CustomerModel customer) {
        String query = "INSERT INTO bank_employees (fname, email, position, user_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnectModel.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPosition());
            pstmt.setInt(4, customer.getUserId()); // Set the user_id
            int rowsAffected = pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


	// Update Employee
	public boolean updateCustomer(CustomerModel customer) {
		String query = "UPDATE bank_employees SET name = ?, email = ?, position = ? WHERE id = ?";
		try (Connection connection = DBConnectModel.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {

			pstmt.setString(1, customer.getName());
			pstmt.setString(2, customer.getEmail());
			pstmt.setString(3, customer.getPosition());
			pstmt.setInt(4, customer.getId());
			int rowsAffected = pstmt.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Delete Employee
	public boolean deleteCustomer(String customerName) {
		String sql = "DELETE FROM bank_employees WHERE fname = ?";

		try (Connection conn = DBConnectModel.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, customerName);

			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Get Employee by ID
	public CustomerModel getCustomerById(int customerId) {
	    String query = "SELECT * FROM bank_employees WHERE id = ?";
	    try (Connection connection = DBConnectModel.getConnection();
	            PreparedStatement pstmt = connection.prepareStatement(query)) {

	        pstmt.setInt(1, customerId);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return new CustomerModel(
	                    rs.getInt("id"),
	                    rs.getString("fname"),
	                    rs.getString("email"),
	                    rs.getString("position"),
	                    rs.getInt("user_id"));
	        } else {
	            log("No customer found for customer ID: " + customerId);
	        }
	    } catch (SQLException e) {
	        log("Error fetching customer information for user ID: " + customerId);
	        e.printStackTrace();
	    }
	    return null;
	}

	public CustomerModel getCustomerByUsername(String customerName) {
	    String query = "SELECT * FROM bank_employees WHERE fname = ?";
	    try (Connection connection = DBConnectModel.getConnection();
	            PreparedStatement pstmt = connection.prepareStatement(query)) {

	        pstmt.setString(1, customerName);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return new CustomerModel(
	                    rs.getInt("id"),
	                    rs.getString("fname"),
	                    rs.getString("email"),
	                    rs.getString("position"),
	                    rs.getInt("user_id"));
	        } else {
	            log("No customer found for customer ID: " + customerName);
	        }
	    } catch (SQLException e) {
	        log("Error fetching customer information for user ID: " + customerName);
	        e.printStackTrace();
	    }
	    return null;
	}
	// Get all Employees
	public List<CustomerModel> getAllCustomer() {
	    List<CustomerModel> customer = new ArrayList<>();
	    String query = "SELECT * FROM bank_employees";

	    try (Connection connection = DBConnectModel.getConnection();
	            Statement stmt = connection.createStatement();
	            ResultSet rs = stmt.executeQuery(query)) {

	        while (rs.next()) {
	            customer.add(new CustomerModel(
	                    rs.getInt("id"),
	                    rs.getString("fname"),
	                    rs.getString("email"),
	                    rs.getString("position"),
	                    rs.getInt("user_id")));
	        }
	        return customer;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return customer;
	}

	// Get User by username
    public UserModel getUserByUsername(String username) {
        String query = "SELECT u.*, e.user_id as employee_id FROM bank_user u LEFT JOIN bank_employees e ON u.id = e.user_id WHERE u.userID = ?";

        try (Connection connection = DBConnectModel.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                UserModel user = new UserModel(
                        resultSet.getInt("id"),
                        resultSet.getString("userID"),
                        resultSet.getString("password"),
                        resultSet.getString("role"));
                user.setCustomerId(resultSet.getInt("employee_id")); // Set the employee_id
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    

    // Create a user for a new employee
    public int createUser(String username, String password, String role) {
        String sql = "INSERT INTO bank_user (userID, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DBConnectModel.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

	public void loadView(String fxmlPath, String title) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle(title);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Check if password is correct for a given user
	public boolean isPasswordCorrect(int userId, String password) {
	    String query = "SELECT * FROM bank_user WHERE id = ?";
	    try (Connection connection = DBConnectModel.getConnection();
	         PreparedStatement pstmt = connection.prepareStatement(query)) {

	        pstmt.setInt(1, userId);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	// Update user password
	public boolean updateUserPassword(int userId, String newPassword) {
	    String query = "UPDATE bank_user SET password = ? WHERE id = ?";
	    try (Connection connection = DBConnectModel.getConnection();
	         PreparedStatement pstmt = connection.prepareStatement(query)) {

	        pstmt.setString(1, newPassword);
	        pstmt.setInt(2, userId);
	        int rowsAffected = pstmt.executeUpdate();

	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}


}
