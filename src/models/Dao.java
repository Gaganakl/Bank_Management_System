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
import javafx.scene.Parent;
import javafx.stage.Stage;
import utils.BcryptHash;

public class Dao {
	
    private void log(String message) {
        System.out.println("[Dao] " + message);
    }
    
    
    // Add Employee
    public boolean addCustomer(Customer customer) {
        String query = "INSERT INTO bank_employees (name, email, position, user_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnect.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPosition());
            pstmt.setInt(4, customer.getUserId()); // Set the user_id
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


	// Update Employee
	public boolean updateCustomer(Customer customer) {
		String query = "UPDATE bank_employees SET name = ?, email = ?, position = ? WHERE id = ?";
		try (Connection connection = DBConnect.getConnection();
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
	public boolean deleteCustomer(int customerId) {
		String sql = "DELETE FROM bank_employees WHERE id = ?";

		try (Connection conn = DBConnect.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, customerId);

			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Get Employee by ID
	public Customer getCustomerById(int customerId) {
	    String query = "SELECT * FROM bank_employees WHERE id = ?";
	    try (Connection connection = DBConnect.getConnection();
	            PreparedStatement pstmt = connection.prepareStatement(query)) {

	        pstmt.setInt(1, customerId);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return new Customer(
	                    rs.getInt("id"),
	                    rs.getString("name"),
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

	// Get all Employees
	public List<Customer> getAllCustomer() {
	    List<Customer> customer = new ArrayList<>();
	    String query = "SELECT * FROM bank_employees";

	    try (Connection connection = DBConnect.getConnection();
	            Statement stmt = connection.createStatement();
	            ResultSet rs = stmt.executeQuery(query)) {

	        while (rs.next()) {
	            customer.add(new Customer(
	                    rs.getInt("id"),
	                    rs.getString("name"),
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
    public User getUserByUsername(String username) {
        String query = "SELECT u.*, e.user_id as employee_id FROM bank_user u " +
                       "LEFT JOIN bank_employees ON u.id = e.user_id " +
                       "WHERE u.username = ?";

        try (Connection connection = DBConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
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
        String sql = "INSERT INTO bank_user (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
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
        return -1;
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
	    try (Connection connection = DBConnect.getConnection();
	         PreparedStatement pstmt = connection.prepareStatement(query)) {

	        pstmt.setInt(1, userId);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            String hashedPassword = rs.getString("password");
	            return BcryptHash.verifyPassword(password, hashedPassword);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	// Update user password
	public boolean updateUserPassword(int userId, String newPassword) {
	    String query = "UPDATE bank_user SET password = ? WHERE id = ?";
	    try (Connection connection = DBConnect.getConnection();
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
