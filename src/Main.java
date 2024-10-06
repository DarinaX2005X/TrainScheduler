import java.util.ArrayList;
import java.util.List;
interface ClonableTrain {
    Train clone();
}
class Train implements ClonableTrain {
    private String trainId;
    private String trainType;
    private String departureTime;
    private String arrivalTime;
    private String status;

    public Train(String trainId, String trainType, String departureTime, String arrivalTime, String status) {
        this.trainId = trainId;
        this.trainType = trainType;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = status;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Train clone() {
        return new Train(trainId,trainType,departureTime,arrivalTime,status);
    }

    public void displayTrainInfo() {
        System.out.println("Train ID: " + trainId + ", Type: " + trainType +
                ", Departure: " + departureTime + ", Arrival: " + arrivalTime + ", Status: " + status);
    }
}


interface TrainOperations {
    void startEngine();
    void stopEngine();
}


interface MaintenanceOperations {
    void performMaintenance();
}


class ElectricTrain extends Train implements TrainOperations, MaintenanceOperations {

    public ElectricTrain(String trainId, String trainType, String departureTime, String arrivalTime, String status) {
        super(trainId, trainType, departureTime, arrivalTime, status);
    }
    public ElectricTrain clone(){
        return new ElectricTrain(getTrainId(),getTrainType(),getDepartureTime(),getArrivalTime(),getStatus());
    }

    @Override
    public void startEngine() {
        System.out.println("Electric engine started.");
    }

    @Override
    public void stopEngine() {
        System.out.println("Electric engine stopped.");
    }

    @Override
    public void performMaintenance() {
        System.out.println("Performing maintenance on electric train.");
    }
}


class DieselTrain extends Train implements TrainOperations, MaintenanceOperations {

    public DieselTrain(String trainId, String trainType, String departureTime, String arrivalTime, String status) {
        super(trainId, trainType, departureTime, arrivalTime, status);
    }

    @Override
    public DieselTrain clone() {
        return new DieselTrain(getTrainId(),getTrainType(),getDepartureTime(),getArrivalTime(),getStatus());
    }

    @Override
    public void startEngine() {
        System.out.println("Diesel engine started.");
    }

    @Override
    public void stopEngine() {
        System.out.println("Diesel engine stopped.");
    }

    @Override
    public void performMaintenance() {
        System.out.println("Performing maintenance on diesel train.");
    }
}


class TrainController {
    private final TrainOperations train;

    public TrainController(TrainOperations train) {
        this.train = train;
    }

    public void startJourney() {
        train.startEngine();
        System.out.println("Journey started.");
    }

    public void endJourney() {
        train.stopEngine();
        System.out.println("Journey ended.");
    }
}

class TrainStatusLogger {
    public void logStatusChange(String trainId, String oldStatus, String newStatus) {
        System.out.println("Train " + trainId + " status changed from " + oldStatus + " to " + newStatus);
    }
}


class TrainSchedule {
    private List<Train> trainList = new ArrayList<>();

    public void addTrain(Train train) {
        trainList.add(train);
    }

    public void displayAllTrains() {
        System.out.println("Train Schedule:");
        for (Train train : trainList) {
            train.displayTrainInfo();
        }
    }

    public Train getTrainById(String trainId) {
        for (Train train : trainList) {
            if (train.getTrainId().equals(trainId)) {
                return train;
            }
        }
        return null;
    }
}


interface TrainUpdaterStrategy {
    void update(TrainSchedule schedule, String trainId);
}


class TrainStatusUpdater implements TrainUpdaterStrategy {
    private String newStatus;
    private TrainStatusLogger logger = new TrainStatusLogger();

    public TrainStatusUpdater(String newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    public void update(TrainSchedule schedule, String trainId) {
        Train train = schedule.getTrainById(trainId);
        if (train != null) {
            String oldStatus = train.getStatus();
            train.setStatus(newStatus);
            logger.logStatusChange(trainId, oldStatus, newStatus);
        } else {
            System.out.println("Train not found.");
        }
    }
}


class TrainTimeUpdater implements TrainUpdaterStrategy {
    private String departureTime;
    private String arrivalTime;

    public TrainTimeUpdater(String departureTime, String arrivalTime) {
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public void update(TrainSchedule schedule, String trainId) {
        Train train = schedule.getTrainById(trainId);
        if (train != null) {
            train.setDepartureTime(departureTime);
            train.setArrivalTime(arrivalTime);
            System.out.println("Train " + trainId + " times updated.");
        } else {
            System.out.println("Train not found.");
        }
    }
}

class Passenger {
    private String name;
    private String ticketNumber;
    private String seatNumber;

    public Passenger(String name, String ticketNumber, String seatNumber) {
        this.name = name;
        this.ticketNumber = ticketNumber;
        this.seatNumber = seatNumber;
    }

    public void displayPassengerInfo() {
        System.out.println("Passenger: " + name + ", Ticket: " + ticketNumber + ", Seat: " + seatNumber);
    }
}


class Station {
    private String stationName;
    private String location;

    public Station(String stationName, String location) {
        this.stationName = stationName;
        this.location = location;
    }

    public void displayStationInfo() {
        System.out.println("Station: " + stationName + ", Location: " + location);
    }
}


class CargoTrain extends Train implements TrainOperations {
    private double cargoWeight;

    public CargoTrain(String trainId, String trainType, String departureTime, String arrivalTime, String status, double cargoWeight) {
        super(trainId, trainType, departureTime,arrivalTime,status);
        this.cargoWeight = cargoWeight;
    }
    public CargoTrain clone() {
        return new CargoTrain(getTrainId(), getTrainType(), getDepartureTime(), getArrivalTime(), getStatus(), cargoWeight);
    }

    @Override
    public void startEngine() {
        System.out.println("Cargo train engine started.");
    }

    @Override
    public void stopEngine() {
        System.out.println("Cargo train engine stopped.");
    }

    public void displayCargoInfo() {
        System.out.println("Cargo weight: " + cargoWeight + " tons");
    }
}


class Maintenance {
    private String lastServiceDate;
    private int serviceIntervalDays;

    public Maintenance(String lastServiceDate, int serviceIntervalDays) {
        this.lastServiceDate = lastServiceDate;
        this.serviceIntervalDays = serviceIntervalDays;
    }

    public void displayMaintenanceInfo() {
        System.out.println("Last service: " + lastServiceDate + ", Service interval: " + serviceIntervalDays + " days");
    }

    public boolean isServiceDue(String currentDate) {
        System.out.println("Checking if service is due...");
        return true;
    }
}
class TrainManager {
    private static TrainManager instance;
    private TrainManager(){
        System.out.println("TrainManager initialized");
    }
    public static TrainManager getInstance(){
        if(instance == null){
            instance = new TrainManager();
        }
        return instance;
    }
    public void manageTrain(Train train) {
        System.out.println("Managing train: " + train.getTrainId());
        train.displayTrainInfo();
    }

}
public class Main {
    public static void main(String[] args) {
        Train electricTrain = new ElectricTrain("E123", "Electric", "10:00", "12:00", "On Time");
        Train dieselTrain = new DieselTrain("D456", "Diesel", "12:00", "14:00", "Delayed");

        // Create controllers for trains
        TrainController electricController = new TrainController((TrainOperations) electricTrain);
        TrainController dieselController = new TrainController((TrainOperations) dieselTrain);

        // Manage train schedule
        TrainSchedule schedule = new TrainSchedule();
        schedule.addTrain(electricTrain);
        schedule.addTrain(dieselTrain);
        schedule.displayAllTrains();

        // Start and end journeys
        electricController.startJourney();
        electricTrain.displayTrainInfo();
        electricController.endJourney();

        dieselController.startJourney();
        dieselTrain.displayTrainInfo();
        dieselController.endJourney() ;


        TrainUpdaterStrategy statusUpdater = new TrainStatusUpdater("Arrived");
        statusUpdater.update(schedule, "D456");


        TrainUpdaterStrategy timeUpdater = new TrainTimeUpdater("09:00", "11:00");
        timeUpdater.update(schedule, "E123");



        CargoTrain cargoTrain = new CargoTrain("C789", "Cargo", "14:00", "20:00" , "On time" , 50);
        cargoTrain.startEngine();
        cargoTrain.displayCargoInfo();
        cargoTrain.stopEngine();


        Passenger passenger = new Passenger("John Doe", "T123", "12A");
        passenger.displayPassengerInfo();

        Station station = new Station("Central", "Astana");
        station.displayStationInfo();


        Maintenance maintenance = new Maintenance("2024-01-01", 180);
        maintenance.displayMaintenanceInfo();
        maintenance.isServiceDue("2024-06-01");


        Train electricTrainClone = electricTrain.clone();
        Train dieselTrainClone = dieselTrain.clone();


        electricTrainClone.displayTrainInfo();
        dieselTrainClone.displayTrainInfo();

        TrainManager manager1 = TrainManager.getInstance();
        TrainManager manager2 = TrainManager.getInstance();


        System.out.println(manager1 == manager2);


        manager1.manageTrain(electricTrain);
        manager2.manageTrain(dieselTrain);

    }
}
