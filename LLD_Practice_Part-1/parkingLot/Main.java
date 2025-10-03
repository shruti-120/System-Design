package parkingLot;
import java.util.*;
abstract class Vehicle {
    private String vehicleNo;
    Vehicle(String vehicleNo){
        this.vehicleNo = vehicleNo;
    }
    public String getVehicleNumber() { return vehicleNo; }
}
class Car extends Vehicle {
    public Car(String vehicleNumber){ super(vehicleNumber); }
}
class Bike extends Vehicle {
    public Bike(String vehicleNumber){ super(vehicleNumber); }
}
class Slot {
    private final int slotNumber;
    private boolean isOccupied;
    private Vehicle vehicle;
    public Slot(int slotNumber){
        this.slotNumber = slotNumber;
        this.isOccupied = false;
    }
    public boolean park(Vehicle vehicle){
        if(isOccupied)return false;
        this.vehicle = vehicle;
        isOccupied = true;
        return true;
    }
    public void unpark(Vehicle vehicle){
        this.vehicle = null;
        isOccupied = false;
    }
    public boolean isAvailable(){ return (!isOccupied); }
    public Vehicle getVehicle() { return vehicle; }
}
class Floor{
    private final int floorNo;
    private List<Slot>  slots;
    public Floor(int floorNo, int numberOfSlots){
        this.floorNo = floorNo;
        this.slots = new ArrayList<>();
        for(int i = 0; i<numberOfSlots; i++)slots.add(new Slot(i));
    } 
    public List<Slot> getSlots() { return slots; }
    public int getFloorNumber() { return floorNo; }
}
class Ticket {
    private static int idCounter = 1;
    private final int ticketId;
    private final String vehicleNumber;
    private final int floorNumber;
    private final int slotNumber;

    public Ticket(String vehicleNumber, int floorNumber, int slotNumber) {
        this.ticketId = idCounter++;
        this.vehicleNumber = vehicleNumber;
        this.floorNumber = floorNumber;
        this.slotNumber = slotNumber;
    }

    public int getTicketId() { return ticketId; }
    public String getVehicleNumber() { return vehicleNumber; }
    public int getFloorNumber() { return floorNumber; }
    public int getSlotNumber() { return slotNumber; }

    @Override
    public String toString() {
        return "TicketID: " + ticketId + ", Vehicle: " + vehicleNumber +
               ", Floor: " + floorNumber + ", Slot: " + slotNumber;
    }
}

class ParkingLot {
    private List<Floor> floors;
    public ParkingLot(int numberOfFloors, int numberOfSlotsPerFloor) {
        this.floors = new ArrayList<>();
        for(int i = 0; i<numberOfSlotsPerFloor; i++){
            floors.add(new Floor(i, numberOfSlotsPerFloor));
        }
    }
    public Ticket parkVehicle(Vehicle vehicle) {
        for (Floor floor : floors) {
            List<Slot> slots = floor.getSlots();
            for (int i = 0; i < slots.size(); i++) {
                Slot slot = slots.get(i);
                if (slot.isAvailable()) {
                    if (slot.park(vehicle)) {
                        return new Ticket(vehicle.getVehicleNumber(), floor.getFloorNumber(), i);
                    }
                }
            }
        }
        return null; // No available slot
    }
    
    public void unparkVehicle(Ticket ticket) {
        Floor floor = floors.get(ticket.getFloorNumber());
        Slot slot = floor.getSlots().get(ticket.getSlotNumber());
    
        if (!slot.isAvailable() && slot.getVehicle().getVehicleNumber().equals(ticket.getVehicleNumber())) {
            slot.unpark(slot.getVehicle());
            System.out.println("Vehicle with ticket " + ticket.getTicketId() + " unparked.");
        } else {
            System.out.println("Invalid ticket or vehicle not found.");
        }
    }
    
}
public class Main {
    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLot(2, 5);

        Vehicle car1 = new Car("KA-01-HH-1234");

        Ticket ticket = parkingLot.parkVehicle(car1);
        if (ticket != null) {
            System.out.println("Parking successful: " + ticket);
        } else {
            System.out.println("Parking failed!");
        }

        // Later when unparking
        parkingLot.unparkVehicle(ticket);
    }
}
