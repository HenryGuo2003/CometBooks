package CometBooks;

/**
 *
 * @author Miles
 */
public class DummyUTDGalaxy implements IUTDGalaxy {
    private final Book[] dummyBooksDB = {
        new Book("Applying UML and Patterns", "Third Edition", "0131489062", "Craig Larman", "applyingumlandpatterns3.png"),
        new Book("The Unified Modeling Language User Guide", "Second Edition", "0321267974", "Booch, Rumbaugh and Jacobson", "unifiedmodelinglanguageuserguide2.png"),
        new Book("UML in Practice: The Art of Modeling Software Systems Demonstrated through Worked Examples and Solutions", "First Edition", "9780470848319", "Pascal Roques", "umlinpractice.png"),
        new Book("UML 2 and the Unified Process: Practical Object-Oriented Analysis and Design", "Second Edition", "0321321278", "Jim Arlow, Ila Neustadt", "uml2andtheunifiedprocess2.png")
    };
    private final Course[] dummyCoursesDB = {
        new Course(new Book[]{ dummyBooksDB[0], dummyBooksDB[1], dummyBooksDB[2], dummyBooksDB[3] }, "Fall 2022", "SE6329", "Object Oriented Software Engineering")
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