package CometBooks;

/**
 *
 * @author Miles
 */
public class DummyUTDGalaxy implements IUTDGalaxy {
    @Override
    public boolean IsValid(String netIDToken, String passwordToken) {
        return netIDToken.equals("admin") && passwordToken.equals("pass");
    }
}
