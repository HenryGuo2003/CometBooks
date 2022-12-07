package CometBooks;

public class Schedule {
    private final Course[] courses;
    
    public Schedule(Course[] courses) { this.courses = courses; }
    
    public Course[] requestCourseList() { return courses; }
}
