package CometBooks;

/**
 *
 * @author Miles
 */
public class DummyUTDGalaxy implements IUTDGalaxy {
    private final SDummyUTDGalaxyAccount[] dummyAccountsDB = {
        new SDummyUTDGalaxyAccount("admin", "pass")
    };
    
    @Override
    public long Login(String netIDToken, String passwordToken) {
        for(int i = 0; i < dummyAccountsDB.length; i++)
            if(dummyAccountsDB[i].username.equals(netIDToken) && dummyAccountsDB[i].password.equals(passwordToken))
                return (i + 1);
        return 0;
    }

    @Override
    public boolean IsValid(long accessToken) {
        accessToken--;
        return accessToken >= 0 && dummyAccountsDB.length > accessToken;
    }
}

class SDummyUTDGalaxyAccount {
    public String username = "", password = "";
    public SDummyUTDGalaxyAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }
}