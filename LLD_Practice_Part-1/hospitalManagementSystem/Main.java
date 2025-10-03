package hospitalManagementSystem;

// import java.time.LocalDateTime;
// import java.util.*;

// class User {
//     private String name; 
//     private static int counter = 1;
//     private int id;
//     public User(String name) {
//         this.id = counter++;
//         this.name = name;
//     }
//     public String getName() { return name; }
// }

// class Patient extends User {
//     private String healthIssue;
//     public Patient(String name, String healthIssue) {
//         super(name);
//         this.healthIssue = healthIssue;
//     }
// }

// class Doctor extends User {
//     private String speciality;
//     private Set<Slot> slots = new HashSet<>();

//     public Doctor(String name, String speciality) {
//         super(name);
//         this.speciality = speciality;
//     }
//     public Set<Slot> getSlots() { return slots; }
//     public Optional<Slot> getSlot(LocalDateTime start, LocalDateTime end) {
//         return slots.stream()
//             .filter(s -> s.getStartTime().equals(start) && s.getEndTime().equals(end))
//             .findFirst();
//     }
//     public void addSlot(Slot slot){ this.slots.add(slot); }
//     public String getSpeciality() { return speciality; }
// }

// class Slot {
//     private static int counter = 1;
//     private final int id;
//     private LocalDateTime startTime, endTime;
//     private boolean isBooked = false; 
//     public Slot(LocalDateTime startTime, LocalDateTime endTime) {
//         this.id = counter++;
//         this.startTime = startTime;
//         this.endTime = endTime;
//     }
//     public LocalDateTime getStartTime() { return startTime; }
//     public LocalDateTime getEndTime() { return endTime; }
//     public boolean isBooked() { return isBooked; }
//     public void updateStatus(boolean value) { this.isBooked = value; }
//     @Override
//     public boolean equals(Object o) {
//         if (this == o) return true;
//         if (!(o instanceof Slot)) return false;
//         Slot other = (Slot)o;
//         return startTime.equals(other.startTime) && endTime.equals(other.endTime);
//     }
//     @Override
//     public int hashCode() {
//         return Objects.hash(startTime, endTime);
//     }
// }

// class Appointment {
//     private static int counter = 1;
//     private final int id;
//     private final Doctor doctor;
//     private final Patient patient;
//     private Slot slot;
//     private boolean isCompleted = false; 

//     public Appointment(Doctor doctor, Patient patient, Slot slot){
//         this.id = counter++;
//         this.doctor = doctor; 
//         this.patient = patient;
//         this.slot = slot;
//     }
//     public Doctor getDoctor() { return doctor; }
//     public Slot getSlot() { return slot; }
//     public void setSlot(Slot slot) { this.slot = slot; }
//     public boolean isCompleted() { return isCompleted; }
//     public void markAsComplete() { this.isCompleted = true; }
// }

// class AppointmentSystem {
//     List<Doctor> doctors = new ArrayList<>();
//     List<Patient> patients = new ArrayList<>();
//     List<Appointment> appointments = new ArrayList<>();

//     public void registerDoctor(Doctor doctor) { doctors.add(doctor); }
//     public void registerPatient(Patient patient) { patients.add(patient); }

//     public Appointment bookAppointment(Doctor doctor, Patient patient,
//                                        LocalDateTime start, LocalDateTime end){
//         Optional<Slot> opt = doctor.getSlot(start, end);
//         if(opt.isPresent() && !opt.get().isBooked()) {
//             Slot slot = opt.get();
//             slot.updateStatus(true);
//             Appointment newAppointment = new Appointment(doctor, patient, slot);
//             appointments.add(newAppointment);
//             System.out.println("Appointment booked for :"+patient.getName()+" with "+doctor.getName());
//             return newAppointment;
//         }
//         System.out.println("Slot not available, choose another slot!");
//         return null;
//     }

//     public void cancelAppointment(Appointment appointment){
//         Slot slot = appointment.getSlot();
//         slot.updateStatus(false);
//         appointments.remove(appointment);
//         System.out.println("Appointment Cancelled!");
//     }

//     public void rescheduleAppointment(Appointment appointment,
//                                       LocalDateTime newStart, LocalDateTime newEnd) {
//         Doctor doctor = appointment.getDoctor();
//         Optional<Slot> optNew = doctor.getSlot(newStart, newEnd);
//         if(optNew.isEmpty() || optNew.get().isBooked()) {
//             System.out.println("This slot is not available!");
//             return;
//         }
//         // free old slot
//         appointment.getSlot().updateStatus(false);
//         // assign new slot
//         Slot newSlot = optNew.get();
//         newSlot.updateStatus(true);
//         appointment.setSlot(newSlot);
//         System.out.println("Appointment rescheduled to: "
//             + newSlot.getStartTime() + " - " + newSlot.getEndTime());
//     }

//     public void showDoctorSlots(Doctor doctor) {
//         System.out.println("Slots for: " + doctor.getName());
//         boolean isAvailable = false;
//         for(Slot slot : doctor.getSlots()){
//             if(!slot.isBooked()){
//                 isAvailable = true;
//                 System.out.println(slot.getStartTime() + " - " + slot.getEndTime());
//             }
//         }
//         if(!isAvailable) System.out.println("No slots available");
//     }
// }

// public class Main {
//     public static void main(String[] args) {
//         AppointmentSystem appointmentSystem = new AppointmentSystem();

//         Doctor d1 = new Doctor("Dr. Jhon", "Cardiologist");
//         appointmentSystem.registerDoctor(d1);
//         d1.addSlot(new Slot(LocalDateTime.of(2025,5,13,12,0), LocalDateTime.of(2025,5,13,13,0)));
//         d1.addSlot(new Slot(LocalDateTime.of(2025,5,13,13,0), LocalDateTime.of(2025,5,13,14,0)));

//         Patient p1 = new Patient("Shruti", "leg fracture");
//         appointmentSystem.registerPatient(p1);

//         // Correct booking via system
//         Appointment a1 = appointmentSystem.bookAppointment(d1, p1,
//             LocalDateTime.of(2025,5,13,12,0), LocalDateTime.of(2025,5,13,13,0));

//         // Attempt reschedule
//         appointmentSystem.rescheduleAppointment(a1,
//             LocalDateTime.of(2025,5,13,13,0), LocalDateTime.of(2025,5,13,14,0));

//         // Cancel
//         appointmentSystem.cancelAppointment(a1);

//         // Show remaining slots
//         appointmentSystem.showDoctorSlots(d1);
//     }
// }

import java.time.LocalDateTime;
import java.util.*;

class User {
    private static int counter = 1;
    protected final int id;
    protected final String name;

    public User(String name) {
        this.id = counter++;
        this.name = name;
    }

    public String getName() { return name; }
}

class Patient extends User {
    public Patient(String name) { super(name); }
}

class Doctor extends User {
    private final String specialty;
    private final List<Slot> slots = new ArrayList<>();

    public Doctor(String name, String specialty) {
        super(name);
        this.specialty = specialty;
    }

    public void addSlot(Slot slot) { slots.add(slot); }
    public List<Slot> getSlots() { return slots; }

    // Finds a slot by matching times, returns null if not found
    public Slot findSlot(LocalDateTime start, LocalDateTime end) {
        for (Slot s : slots) {
            if (s.getStart().equals(start) && s.getEnd().equals(end)) {
                return s;
            }
        }
        return null;
    }
}

class Slot {
    private static int counter = 1;
    private final int id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private boolean booked;

    public Slot(LocalDateTime start, LocalDateTime end) {
        this.id = counter++;
        this.start = start;
        this.end = end;
        this.booked = false;
    }

    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public boolean isBooked() { return booked; }
    public void setBooked(boolean b) { booked = b; }

    @Override
    public String toString() {
        return start + " to " + end + (booked ? " [Booked]" : " [Free]");
    }
}

class Appointment {
    private static int counter = 1;
    private final int id;
    private final Doctor doctor;
    private final Patient patient;
    private Slot slot;

    public Appointment(Doctor d, Patient p, Slot s) {
        this.id = counter++;
        this.doctor = d;
        this.patient = p;
        this.slot = s;
    }

    public Slot getSlot() { return slot; }
    public Doctor getDoctor() { return doctor; }
    public Patient getPatient() { return patient; }
    public void reschedule(Slot newSlot) { this.slot = newSlot; }

    @Override
    public String toString() {
        return "Appointment#" + id + ": " + patient.getName() + " with " + doctor.getName() + " @ " + slot;
    }
}

class AppointmentService {
    private final List<Appointment> appointments = new ArrayList<>();

    public Appointment bookAppointment(Doctor doc, Patient pat,
                                       LocalDateTime start, LocalDateTime end) {
        Slot slot = doc.findSlot(start, end);
        if (slot == null) {
            System.out.println("No such slot exists.");
            return null;
        }
        if (slot.isBooked()) {
            System.out.println("Slot already booked.");
            return null;
        }
        slot.setBooked(true);
        Appointment appt = new Appointment(doc, pat, slot);
        appointments.add(appt);
        System.out.println("Booked: " + appt);
        return appt;
    }

    public void cancelAppointment(Appointment appt) {
        if (appt == null) return;
        appt.getSlot().setBooked(false);
        appointments.remove(appt);
        System.out.println("Cancelled: " + appt);
    }

    public void rescheduleAppointment(Appointment appt,
                                      LocalDateTime newStart, LocalDateTime newEnd) {
        if (appt == null) return;
        Slot newSlot = appt.getDoctor().findSlot(newStart, newEnd);
        if (newSlot == null) {
            System.out.println("New slot not found.");
            return;
        }
        if (newSlot.isBooked()) {
            System.out.println("New slot already booked.");
            return;
        }
        // free old slot
        appt.getSlot().setBooked(false);
        // book new slot 
        newSlot.setBooked(true);
        appt.reschedule(newSlot);
        System.out.println("Rescheduled: " + appt);
    }

    public void showDoctorSlots(Doctor doc) {
        System.out.println("Slots for Dr. " + doc.getName() + ":");
        for (Slot s : doc.getSlots()) {
            System.out.println(s);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        AppointmentService service = new AppointmentService();
        Doctor dr = new Doctor("Dr. Amy", "Dermatologist");
        dr.addSlot(new Slot(LocalDateTime.of(2025,5,20,9,0), LocalDateTime.of(2025,5,20,9,30)));
        dr.addSlot(new Slot(LocalDateTime.of(2025,5,20,9,30),LocalDateTime.of(2025,5,20,10,0)));

        Patient pt = new Patient("John Doe");
        service.showDoctorSlots(dr);

        Appointment a1 = service.bookAppointment(dr, pt,
            LocalDateTime.of(2025,5,20,9,0), LocalDateTime.of(2025,5,20,9,30));
        service.rescheduleAppointment(a1,
            LocalDateTime.of(2025,5,20,9,30), LocalDateTime.of(2025,5,20,10,0));
        service.cancelAppointment(a1);

        service.showDoctorSlots(dr);
    }
}

