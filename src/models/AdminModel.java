package models;

public class AdminModel extends CustomerModel {
    public AdminModel(int id, String name, String email, String position, int userId) {
        super(id, name, email, position, userId);
    }

    // Possibility to add admin-specific properties or methods if needed
}
