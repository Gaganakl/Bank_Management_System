package models;

import javafx.beans.property.*;

public class CustomerModel {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty email;
    private StringProperty position;
    private IntegerProperty userId;

    public CustomerModel(int id, String name, String email, String position, int userId) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.position = new SimpleStringProperty(position);
        this.userId = new SimpleIntegerProperty(userId);
    }

    // Getters and setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    public String getPosition() { return position.get(); }
    public void setPosition(String position) { this.position.set(position); }
    public StringProperty positionProperty() { return position; }

    // New getter and setter for userId
    public int getUserId() { return userId.get(); }
    public void setUserId(int userId) { this.userId.set(userId); }
    public IntegerProperty userIdProperty() { return userId; }
}
