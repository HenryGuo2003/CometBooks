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
        new SDummyUTDGalaxyAccount(1, "admin", "pass", "Jonathan R.", new Schedule(new Course[]{ dummyCoursesDB[0] })),
        new SDummyUTDGalaxyAccount(2, "willr", "word", "Will R.", new Schedule(new Course[]{ dummyCoursesDB[0] }))
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

    @Override
    public Book getBookByISBN(String ISBN) {
        for(Book b : dummyBooksDB)
            if(b.ISBN.equals(ISBN))
                return b;
        return null;
    }

    @Override
    public String getStudentName(long ID) {
        for(SDummyUTDGalaxyAccount a : dummyAccountsDB)
            if(a.ID == ID)
                return a.displayName;
        return "";
    }
}

class SDummyUTDGalaxyAccount {
    public String username, password, displayName;
    public Schedule schedule;
    public long ID;
    public SDummyUTDGalaxyAccount(long ID, String username, String password, String displayName, Schedule schedule) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.schedule = schedule;
        this.displayName = displayName;
    }
}