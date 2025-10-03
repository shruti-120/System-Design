package courseEnrollmentSystem;

import java.util.HashSet;

class Course{
    private final int ID, capacity;
    private final String name;
    private HashSet<Student> studentsEnrolled;
    Course(int ID, int capacity, String name){
        this.ID = ID;
        this.name = name;
        this.capacity = capacity;
        this.studentsEnrolled = new HashSet<>();
    }
    public int getId(){ return ID; };
    public String getName(){ return name; }
    public int getcapacity(){ return capacity; }
    public HashSet<Student> getStudentsEnrolled(){ return studentsEnrolled; }
    public void setStudentsEnrolled(Student student){ this.studentsEnrolled.add(student); }
}

class Student{
    private final int ID;
    private final String name;
    private HashSet<Course> coursesEnrolledIn;
    Student(int ID,  String name){
        this.ID = ID;
        this.name = name;
        this.coursesEnrolledIn = new HashSet<>();
    }
    public int getId(){ return ID; };
    public String getName(){ return name; }
    public HashSet<Course> getCoursesEnrolledIn(){ return coursesEnrolledIn; }
    public void setCoursesEnrolledIn(Course course){ this.coursesEnrolledIn.add(course); }
}

class EnrollmentSystem {
    HashSet<Course> courses = new HashSet<>();
    public void enroll(Student s, Course c){
        if(c.getcapacity() > c.getStudentsEnrolled().size()){
            s.getCoursesEnrolledIn().add(c);
            c.getStudentsEnrolled().add(s);
            courses.add(c);
            System.out.println("enrolled "+s.getName()+" ID: "+s.getId()+" to course "+c.getName()+" CID: "+c.getId());
        }
        else System.out.println("cannot enroll , capacity full!");
    }
    public void unenroll(Student s, Course c){
        s.getCoursesEnrolledIn().remove(c);
        c.getStudentsEnrolled().remove(s);
        System.out.println("Unenrolled"+s.getName()+" ID: "+s.getId()+" from course "+c.getName()+" CID: "+c.getId());
    }
    public void viewAllEnrolledCourses(){
        for(Course c: courses){
            System.out.println(c.getName());
            System.out.println("------------------------------");
        }
    }
}

public class Main {
    public static void main(String[] args){
        Course c1 = new Course(1,30,"physics");
        Course c2 = new Course(2,2,"chemistry");
        Course c3 = new Course(3,25,"maths");
        Course c4 = new Course(4,15,"computer");

        Student s1 = new Student(1, "John");
        Student s2 = new Student(2, "megan");
        Student s3 = new Student(3, "alice");
        Student s4 = new Student(4, "bob");

        EnrollmentSystem  es = new EnrollmentSystem();
        es.enroll(s1, c1);
        es.enroll(s2, c2);
        es.enroll(s3, c2);
        es.enroll(s4, c2);
        es.enroll(s4, c4);
        es.enroll(s1, c1);
        es.viewAllEnrolledCourses();

    }
}