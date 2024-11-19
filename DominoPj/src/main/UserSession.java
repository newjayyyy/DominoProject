package main;

public class UserSession {
    private static UserSession instance;
    private User loggedInUser;

    // Private constructor to prevent instantiation
    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void clearSession() {
        this.loggedInUser = null;
    }
}
