package model;

/** his class is an object constructor used to get User objects */
public class User {
    private int userId;
    private String username;
    private String password;

    /** @param userId int value of User ID
     * @param username String value of username
     * @param password String value of password
     */
    public User(int userId, String username, String password ) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    /** Getter gets User ID from User
     * @return userID Integer value from the 'User'
     */
    public int getUserId() {
        return userId;
    }

    /** Getter gets Username from User
     * @return 'username' String value from the 'User'
     */
    public String getUsername() {
        return username;
    }

    /** Getter gets Password from User
     * @return 'password' String value from the 'User'
     */
    public String getPassword() {
        return password;
    }

}