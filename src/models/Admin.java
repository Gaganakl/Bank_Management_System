package models;

public class Admin extends Customer {
    public Admin(int id, String name, String email, String position, int userId) {
        super(id, name, email, position, userId);
    }

    // Possibility to add admin-specific properties or methods if needed
}
