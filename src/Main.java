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

    public Train(TrainBuilder builder) {
        this.trainId = builder.trainId;
        this.trainType = builder.trainType;
        this.departureTime = builder.departureTime;
        this.arrivalTime = builder.arrivalTime;
        this.status = builder.status;
    }

    public String getTrainId() {
        return trainId;
    }

    public String getTrainType() {
        return trainType;
    }
    public String getDepartureTime() {
        return departureTime;
    }
    public String getArrivalTime() {
        return arrivalTime;
    }
    public String getStatus() {
        return status;
    }

    @Override
    public Train clone() {
        return new Train.TrainBuilder()
                .withTrainId(trainId)
                .withTrainType(trainType)
                .withDepartureTime(departureTime)
                .withArrivalTime(arrivalTime)
                .withStatus(status)
                .build();
    }

    public void displayTrainInfo() {
        System.out.println("Train ID: " + trainId + ", Type: " + trainType +
                ", Departure: " + departureTime + ", Arrival: " + arrivalTime + ", Status: " + status);
    }

    public static class TrainBuilder {
        private String trainId;
        private String trainType;
        private String departureTime;
        private String arrivalTime;
        private String status;
        protected double cargoWeight;

        public TrainBuilder withTrainId(String trainId) {
            this.trainId = trainId;
            return this;
        }

        public TrainBuilder withTrainType(String trainType) {
            this.trainType = trainType;
            return this;
        }

        public TrainBuilder withDepartureTime(String departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        public TrainBuilder withArrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
            return this;
        }

        public TrainBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public TrainBuilder withCargoWeight(double cargoWeight) {
            this.cargoWeight = cargoWeight;
            return this;
        }

        public Train build() {
            return new Train(this);
        }
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

    private ElectricTrain(ElectricTrainBuilder builder) {
        super(builder);
    }
    public ElectricTrain clone() {
        return (ElectricTrain) new ElectricTrainBuilder()
                .withTrainId(getTrainId())
                .withTrainType(getTrainType())
                .withDepartureTime(getDepartureTime())
                .withArrivalTime(getArrivalTime())
                .withStatus(getStatus())
                .build();
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

    public static class ElectricTrainBuilder extends TrainBuilder {
        @Override
        public ElectricTrain build() {
            return new ElectricTrain(this);
        }
    }
}


class DieselTrain extends Train implements TrainOperations, MaintenanceOperations {

    private DieselTrain(DieselTrainBuilder builder) {
        super(builder);
    }

    @Override
    public DieselTrain clone() {
        return (DieselTrain) new DieselTrainBuilder()
                .withTrainId(getTrainId())
                .withTrainType(getTrainType())
                .withDepartureTime(getDepartureTime())
                .withArrivalTime(getArrivalTime())
                .withStatus(getStatus())
                .build();
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

    public static class DieselTrainBuilder extends TrainBuilder {
        @Override
        public DieselTrain build() {
            return new DieselTrain(this);
        }
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
            Train updatedTrain = new Train.TrainBuilder()
                    .withTrainId(train.getTrainId())
                    .withTrainType(train.getTrainType())
                    .withDepartureTime(train.getDepartureTime())
                    .withArrivalTime(train.getArrivalTime())
                    .withStatus(newStatus)
                    .build();
            schedule.addTrain(updatedTrain);
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
            Train updatedTrain = new Train.TrainBuilder()
                    .withTrainId(train.getTrainId())
                    .withTrainType(train.getTrainType())
                    .withDepartureTime(departureTime)
                    .withArrivalTime(arrivalTime)
                    .withStatus(train.getStatus())
                    .build();
            schedule.addTrain(updatedTrain);
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

    private CargoTrain(CargoTrainBuilder builder) {
        super(builder);
        this.cargoWeight = builder.cargoWeight;
    }
    public CargoTrain clone() {
        return (CargoTrain) new CargoTrainBuilder()
                .withTrainId(getTrainId())
                .withTrainType(getTrainType())
                .withDepartureTime(getDepartureTime())
                .withArrivalTime(getArrivalTime())
                .withStatus(getStatus())
                .withCargoWeight(cargoWeight)
                .build();
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

    public static class CargoTrainBuilder extends TrainBuilder {

        private double cargoWeight;

        public CargoTrainBuilder withCargoWeight(double cargoWeight) {
            this.cargoWeight = cargoWeight;
            return this;
        }

        public CargoTrain build() {
            return new CargoTrain(this);
        }
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
        Train electricTrain = new ElectricTrain.ElectricTrainBuilder()
                .withTrainId("E123")
                .withTrainType("Electric")
                .withDepartureTime("10:00")
                .withArrivalTime("12:00")
                .withStatus("On Time")
                .build();

        Train dieselTrain = new DieselTrain.DieselTrainBuilder()
                .withTrainId("D456")
                .withTrainType("Diesel")
                .withDepartureTime("12:00")
                .withArrivalTime("14:00")
                .withStatus("Delayed")
                .build();

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



        CargoTrain cargoTrain = (CargoTrain) new CargoTrain.CargoTrainBuilder()
                .withTrainId("C789")
                .withTrainType("Cargo")
                .withDepartureTime("14:00")
                .withArrivalTime("20:00")
                .withStatus("On time")
                .withCargoWeight(50.0)
                .build();
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
