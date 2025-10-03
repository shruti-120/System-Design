package attendanceManagementSystem;
import java.util.*;

class User {
    private static int counter = 1;
    private int id;
    private String name;

    public User(String name) {
        this.id = counter++;
        this.name = name;
    }
    public int getId() { return id; }
    public String getName() { return name; }
}

class Student extends User{
    private List<Course> enrolledCourses = new ArrayList<>();
    
    public Student(String name){
        super(name);
    }
    public List<Course> getEnrolledCourses() { return enrolledCourses; }
    public void addCourse(Course course) { this.enrolledCourses.add(course); }
}

class Instructor extends User{
    public Instructor(String name) { super(name); }
}

class Course {
    private String name;
    private static int counter = 1;
    private int id;
    private List<Student> studentsEnrolled = new ArrayList<>();
    private List<Session> sessions = new ArrayList<>();

    public Course(String name) { 
        this.id = counter++;
        this.name = name;
    }
    public List<Student> getEnrolledStudents() { return studentsEnrolled; }
    public List<Session> getSessions() { return sessions; }
    public void addStudents(Student student) { this.studentsEnrolled.add(student); }
    public void addSessions(Session session) { this.sessions.add(session); }
}

class Session {
    private static int counter = 1;
    private int id;
    private Course course;
    private String time;
    private List<AttendanceRecord> records = new ArrayList<>();

    public Session(Course course, String time){
        this.id = counter++;
        this.course = course;
        this.time = time;
    }
    public String getTime() { return time; } 
    public List<AttendanceRecord> getAttendanceRecords() { return records; }
    public void addRecord(AttendanceRecord attendanceRecord) { this.records.add(attendanceRecord); }
}

enum AttendenceStatus { PRESENT, ABSENT, LATE }

class AttendanceRecord {
    private final Student student;
    private AttendenceStatus status;
    public AttendanceRecord(Student student, AttendenceStatus status){
        this.student = student;
        this.status = status;
    }
    public Student getStudent() { return student; }
    public AttendenceStatus status() { return status; }
    public void setStatus(AttendenceStatus status) { this.status = status; }
}

class AttendanceManagementSystem {
    List<Course> courses = new ArrayList<>();
    List<Student> students = new ArrayList<>();

    public void addStudent(Student student) { students.add(student); }
    public void addCourse(Course course) { courses.add(course); }

    public void markAttendence(int courseId, int sessionId, int studentId, AttendenceStatus status){
        AttendanceRecord record = new AttendanceRecord(students.get(studentId-1), status);
        courses.get(courseId-1).getSessions().get(sessionId-1).addRecord(record);
    }
    public void getStudentsAttendence(int studentId, int courseId) {
        int present = 0, totalSession = 0;
        for(Session session : courses.get(courseId-1).getSessions()) {
            totalSession++;
            for(AttendanceRecord record: session.getAttendanceRecords()){
                if(record.getStudent().getId() == studentId && (record.status() == AttendenceStatus.PRESENT || record.status() == AttendenceStatus.LATE)) present++;
            }
        }
        double percent = present * 100.0 / totalSession;
        System.out.println("Name : "+students.get(studentId-1).getName()+" Attendence : "+percent+"%");
    }
    public void getSessionReport(int sessionId, int courseId) {
        Session session = courses.get(courseId-1).getSessions().get(sessionId-1);
        for(AttendanceRecord record: session.getAttendanceRecords()){
            System.out.println(record.getStudent().getId() +" , "+record.getStudent().getName()+" : "+record.status());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        AttendanceManagementSystem attendenceService = new AttendanceManagementSystem();
        Student s1 = new Student("Alice");
        attendenceService.addStudent(s1);
        Student s2 = new Student("Bob");
        attendenceService.addStudent(s2);

        Course c1 = new Course("Computer Science");
        c1.addSessions(new Session(c1, "10:00 AM - 11:00 AM"));
        c1.addSessions(new Session(c1, "11:00 AM - 12:00 AM"));
        attendenceService.addCourse(c1);

        attendenceService.markAttendence(1, 1, 1, AttendenceStatus.PRESENT);
        attendenceService.markAttendence(1, 2, 1, AttendenceStatus.PRESENT);
        attendenceService.markAttendence(1, 1, 2, AttendenceStatus.PRESENT);
        attendenceService.markAttendence(1, 2, 2, AttendenceStatus.ABSENT);

        attendenceService.getStudentsAttendence(1, 1);
        attendenceService.getStudentsAttendence(2, 1);
        attendenceService.getSessionReport(1, 1);
    }
}
