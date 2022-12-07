package CometBooks;

/**
 *
 * @author Miles
 */
public class DummyUTDGalaxy implements IUTDGalaxy {
    private final Book[] dummyBooksDB = {
        new Book("Applying UML and Patterns (3rd Edition)", "3rd", "0131489062", "Craig Larman", "applyingumlandpatterns3.png")
    };
    private final Course[] dummyCoursesDB = {
        new Course(new Book[]{ dummyBooksDB[0] }, "Fall 2017", "SE6538", "Object Oriented Software Engineering")
    };
    private final SDummyUTDGalaxyAccount[] dummyAccountsDB = {
        new SDummyUTDGalaxyAccount("admin", "pass", new Schedule(new Course[]{ dummyCoursesDB[0] }))
    };
    
    @Override
    public long login(String netIDToken, String passwordToken) {
        for(int i = 0; i < dummyAccountsDB.length; i++)
            if(dummyAccountsDB[i].username.equals(netIDToken) && dummyAccountsDB[i].password.equals(passwordToken))
                return (i + 1);
        return 0;
    }

    @Override
    public boolean isValid(long accessToken) {
        accessToken--;
        return accessToken >= 0 && dummyAccountsDB.length > accessToken;
    }

    @Override
    public Schedule getStudentSchedule(long accessToken) {
        if(isValid(accessToken))
            return dummyAccountsDB[(int)(accessToken - 1)].schedule;
        return null;
    }
}

class SDummyUTDGalaxyAccount {
    public String username = "", password = "";
    public Schedule schedule;
    public SDummyUTDGalaxyAccount(String username, String password, Schedule schedule) {
        this.username = username;
        this.password = password;
        this.schedule = schedule;
    }
}