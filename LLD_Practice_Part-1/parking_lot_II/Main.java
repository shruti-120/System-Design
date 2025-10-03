package parking_lot_II;

import java.util.*;

enum VehicleType { BIKE, TRUCK, CAR }

class Vehicle {
    String vehicleNo;
    VehicleType type;
    int allotedSlot;
    public Vehicle (String vehicleNo, VehicleType type) {
        this.vehicleNo = vehicleNo;
        this.type = type;
    }
    public void allotSlot(int slotId) { this.allotedSlot = slotId; }
}

class Slot {
    static int counter = 1;
    int id;
    boolean isAvailable;
    public Slot() {
        this.id = counter++; 
        this.isAvailable = true;
    }
}

class Floor {
    List<Slot> slots = new ArrayList<>();
    int floorNo;
    public Floor(int floorNo, List<Slot> slots) { 
        this.floorNo = floorNo;
        this.slots = slots;
    }
}

class ParkingLot {
    Map<String, Vehicle> vehicles = new HashMap<>();
    Map<Integer, Slot>  slots = new HashMap<>();
    TreeMap<Integer, Floor> floors = new TreeMap<>();

    public void createParkingLot(int noOfFloors, int slotPerFloor) {
        for(int i = 0; i<noOfFloors; i++){
            List<Slot> newSlots = new ArrayList<>();
            for(int j = 0; j<slotPerFloor; j++){
                Slot s = new Slot();
                newSlots.add(s);
                slots.put(s.id, s);
            }
            Floor floor = new Floor(i, newSlots);
            floors.put(i, floor);
        }
    }

    public void showAvailableSlots() {
        System.out.println("Available Slots Info: -> ");
        boolean foundSpot = false;
        for(Map.Entry<Integer, Floor> entry: floors.entrySet()) {
            System.out.print("Floor No - "+entry.getKey()+" --> ");
            for(Slot s: entry.getValue().slots){
                if(s.isAvailable){
                    foundSpot = true;
                    System.out.print("["+s.id+"] ");
                }
            }
            System.out.println();
        }
        if(!foundSpot)System.out.println("All alots are currently booked!");
    }
    public void parkVehicle(Vehicle vehicle, int slotId) {
        if(!slots.containsKey(slotId)){
            System.out.println("Invalid slot id");return;
        }
        Slot slot = slots.get(slotId);
        if(!slot.isAvailable){
            System.out.println("This slot is already booked!");
            return;
        }
        slot.isAvailable = false;
        vehicle.allotSlot(slotId);
        vehicles.put(vehicle.vehicleNo, vehicle);
        System.out.println("Vehicle Parked! Vehicle no:"+vehicle.vehicleNo+" vehicle Type- "+vehicle.type);
    }
    public void unparkVehicle(String vehicleNo) {
        if(!vehicles.containsKey(vehicleNo)){
            System.out.println("Invalid vehicle id");return;
        }
        int slotId = vehicles.get(vehicleNo).allotedSlot;
        Slot slot = slots.get(slotId);
        slot.isAvailable = true;
        System.out.println("Vehicle unparked! : "+vehicleNo+" Please pay the fare!");
    }
}

public class Main {
    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.createParkingLot(3, 10);
        parkingLot.showAvailableSlots();
        Vehicle v1 = new Vehicle("1JS19IS09", VehicleType.BIKE);
        parkingLot.parkVehicle(v1, 1);
        Vehicle v2 = new Vehicle("1JS19IS08", VehicleType.CAR);
        parkingLot.parkVehicle(v2, 1);
        Vehicle v3 = new Vehicle("1JS19IS07", VehicleType.TRUCK);
        parkingLot.parkVehicle(v3, 2);
        Vehicle v4 = new Vehicle("1JS19IS05", VehicleType.CAR);
        parkingLot.parkVehicle(v4, 12);
        parkingLot.showAvailableSlots();
        parkingLot.unparkVehicle("1JS19IS09");
        parkingLot.unparkVehicle("1JS19IS08");
        parkingLot.unparkVehicle("1JS19IS07");
        parkingLot.unparkVehicle("1JS19IS05");
    }
}
