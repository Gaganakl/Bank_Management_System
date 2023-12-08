package utils;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptHash {
    // Generate a hash for the given password
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // Verify a password against a hashed password
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}

