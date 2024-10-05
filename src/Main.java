class Train {
    private String trainId;
    private String trainType;
    private String departureTime;

    public Train(String trainId, String trainType, String departureTime) {
        this.trainId = trainId;
        this.trainType = trainType;
        this.departureTime = departureTime;
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

    public void displayTrainInfo() {
        System.out.println("Train ID: " + trainId + ", Type: " + trainType + ", Departure: " + departureTime);
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

    public ElectricTrain(String trainId, String trainType, String departureTime) {
        super(trainId, trainType, departureTime);
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

    public DieselTrain(String trainId, String trainType, String departureTime) {
        super(trainId, trainType, departureTime);
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

    public CargoTrain(String trainId, String trainType, String departureTime, double cargoWeight) {
        super(trainId, trainType, departureTime);
        this.cargoWeight = cargoWeight;
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

public class Main {
    public static void main(String[] args) {
        Train electricTrain = new ElectricTrain("E123", "Electric", "10:00");
        Train dieselTrain = new DieselTrain("D456", "Diesel", "12:00");


        TrainController electricController = new TrainController((TrainOperations) electricTrain);
        TrainController dieselController = new TrainController((TrainOperations) dieselTrain);


        electricController.startJourney();
        electricTrain.displayTrainInfo();
        electricController.endJourney();

        dieselController.startJourney();
        dieselTrain.displayTrainInfo();
        dieselController.endJourney();


        CargoTrain cargoTrain = new CargoTrain("C789", "Cargo", "14:00", 50);
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
    }
}
