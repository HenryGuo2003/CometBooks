package CometBooks;

public class Course {
    private final Book[] bookList;
    public String semester, code, name;
    
    public Course(Book[] bookList, String semester, String code, String name) {
        this.bookList = bookList;
        this.semester = semester;
        this.code = code;
        this.name = name;
    }
    
    public Book[] requestBookList() { return bookList; }
}
