package CometBooks;

/**
 *
 * @author Miles
 */
public class Book {
    public String name, edition, ISBN, author, imageName;
    
    public Book(String name, String edition, String ISBN, String author, String imageName) {
        this.name = name;
        this.edition = edition;
        this.ISBN = ISBN;
        this.author = author;
        this.imageName = imageName;
    }
}
