package CometBooks;

/**
 *
 * @author Miles
 * 
 * Defines an interface boundary where a real connection to the UTD Galaxy
 * accounts system can be plugged in. In this simulation, a dummy implementer
 * pretending to be UTD Galaxy exists instead.
 */
public interface IUTDGalaxy {
    
    boolean isValid(long accessToken);
    /**
     * Checks that the information entered matches a valid UTD student account
     * provided by UTD Galaxy. Note that it is never a good idea, nor is it
     * intended, for a username and password to be sent in plaintext over the
     * internet. The inputs are called "Token" because it is expected that
     * these should be encrypted values.
     */
    long login(String netIDToken, String passwordToken);
    Schedule getStudentSchedule(long accessToken);
    Book getBookByISBN(String ISBN);
    String getStudentName(long ID);
}
