package Entity;

/**
 * The abstract class, extended by AdminUser, RegularUser, and GuestUser
 */
public abstract class User {


    private String username;

    /**
     * create a user with the given username
     * @param username This user's username
     */
    public User(String username){
        this.username = username;
    }
    /**
     * Returns the user's username.
     * @return The user's username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Returns the user's password.
     * @return A string containing the user's password
     */
    public abstract String getPassword();

    /**
     * allowing the user to change his/her username
     * @param username The user's username
     */
    public void setUsername (String username) {
        this.username = username;
    }
}

