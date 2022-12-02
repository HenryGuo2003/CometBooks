package CometBooks;

/**
 *
 * @author Miles
 * 
 * An interface boundary allowing the system to communicate with any variety of
 * DBMS systems. Implicitly uses an adapter pattern: to allow the system to
 * support a new DBMS wrapper/driver/interface, simply implement this
 * interface.
 * Decouples this system from attachment to any particular DB driver. Here,
 * a dummy static DB system will be used for simplicity.
 */
public interface IDatabase {
    void Select();
    void Insert();
}
