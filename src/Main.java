import java.util.ArrayList;
import java.util.List;

interface ClonableTrain {
    Train clone();
}

interface Command {
    void execute();
}

interface MaintenanceFactory {
    Maintenance createMaintenance(String lastServiceDate, int serviceIntervalDays);
}
interface PassengerFactory {
    Passenger createPassenger(String name, String ticketNumber, String seatNumber);
}

interface StationFactory {
    Station createStation(String stationName, String location);
}
interface TransportFactory {
    Maintenance createMaintenance(String lastServiceDate, int serviceIntervalDays);
    Passenger createPassenger(String name, String ticketNumber, String seatNumber);
    Station createStation(String stationName, String location);
}
interface TrainIterator {
    boolean hasNext();
    Train next();
}


class DefaultTransportFactory implements TransportFactory {
    @Override
    public Maintenance createMaintenance(String lastServiceDate, int serviceIntervalDays) {
        return new DefaultMaintenanceFactory().createMaintenance(lastServiceDate, serviceIntervalDays);
    }

    @Override
    public Passenger createPassenger(String name, String ticketNumber, String seatNumber) {
        return new DefaultPassengerFactory().createPassenger(name, ticketNumber, seatNumber);
    }

    @Override
    public Station createStation(String stationName, String location) {
        return new DefaultStationFactory().createStation(stationName, location);
    }
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

class TrainScheduleIterator implements TrainIterator {
    private List<Train> trains;
    private int position = 0;

    public TrainScheduleIterator(List<Train> trains) {
        this.trains = trains;
    }

    @Override
    public boolean hasNext() {
        return position < trains.size();
    }

    @Override
    public Train next() {
        if (hasNext()) {
            return trains.get(position++);
        }
        return null;
    }
}



interface TrainOperations {
    void startEngine();
    void stopEngine();
}


interface MaintenanceOperations {
    void performMaintenance();
}

class Ticket {
    private String ticketId;
    private String passengerName;
    private String seatNumber;
    private boolean isBooked;

    public Ticket(String ticketId, String passengerName, String seatNumber) {
        this.ticketId = ticketId;
        this.passengerName = passengerName;
        this.seatNumber = seatNumber;
        this.isBooked = false;
    }

    public void book() {
        if (!isBooked) {
            System.out.println("Booking ticket for " + passengerName + " on seat " + seatNumber);
            isBooked = true;
        } else {
            System.out.println("Ticket is already booked.");
        }
    }

    public void cancel() {
        if (isBooked) {
            System.out.println("Canceling ticket for " + passengerName);
            isBooked = false;
        } else {
            System.out.println("Ticket is not booked yet.");
        }
    }

    public void modify(String newSeat) {
        if (isBooked) {
            System.out.println("Modifying seat for " + passengerName + " from " + seatNumber + " to " + newSeat);
            seatNumber = newSeat;
        } else {
            System.out.println("Cannot modify unbooked ticket.");
        }
    }

    public void displayTicketInfo() {
        System.out.println("Ticket ID: " + ticketId + ", Passenger: " + passengerName + ", Seat: " + seatNumber + ", Status: " + (isBooked ? "Booked" : "Not Booked"));
    }
}
// Command to book a ticket
class BookTicketCommand implements Command {
    private Ticket ticket;

    public BookTicketCommand(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public void execute() {
        ticket.book();
    }
}

// Command to cancel a ticket
class CancelTicketCommand implements Command {
    private Ticket ticket;

    public CancelTicketCommand(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public void execute() {
        ticket.cancel();
    }
}

class ModifyTicketCommand implements Command {
    private Ticket ticket;
    private String newSeat;

    public ModifyTicketCommand(Ticket ticket, String newSeat) {
        this.ticket = ticket;
        this.newSeat = newSeat;
    }

    @Override
    public void execute() {
        ticket.modify(newSeat);
    }
}
class TicketManager {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }
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



    public Train getTrainById(String trainId) {
        for (Train train : trainList) {
            if (train.getTrainId().equals(trainId)) {
                return train;
            }
        }
        return null;
    }
    public TrainIterator iterator() {
        return new TrainScheduleIterator(trainList);
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
class DefaultPassengerFactory implements PassengerFactory {
    @Override
    public Passenger createPassenger(String name, String ticketNumber, String seatNumber) {
        return new Passenger(name, ticketNumber, seatNumber);
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

class DefaultStationFactory implements StationFactory {
    @Override
    public Station createStation(String stationName, String location) {
        return new Station(stationName, location);
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

class DefaultMaintenanceFactory implements MaintenanceFactory {
    @Override
    public Maintenance createMaintenance(String lastServiceDate, int serviceIntervalDays) {
        return new Maintenance(lastServiceDate, serviceIntervalDays);
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
        TransportFactory factory = new DefaultTransportFactory();

        Station station = factory.createStation("Central", "Astana");
        station.displayStationInfo();

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

        TrainController electricController = new TrainController((TrainOperations) electricTrain);
        TrainController dieselController = new TrainController((TrainOperations) dieselTrain);


        TrainSchedule schedule = new TrainSchedule();
        schedule.addTrain(electricTrain);
        schedule.addTrain(dieselTrain);
        TrainIterator iterator = schedule.iterator();
        System.out.println("Train Schedule (using Iterator):");
        while (iterator.hasNext()) {
            Train train = iterator.next();
            train.displayTrainInfo();
        }


        Ticket ticket1 = new Ticket("T123", "John Doe", "12A");


        TicketManager ticketManager = new TicketManager();


        Command bookCommand = new BookTicketCommand(ticket1);
        ticketManager.setCommand(bookCommand);
        ticketManager.executeCommand();
        ticket1.displayTicketInfo();


        Command modifyCommand = new ModifyTicketCommand(ticket1, "14B");
        ticketManager.setCommand(modifyCommand);
        ticketManager.executeCommand();
        ticket1.displayTicketInfo();


        Command cancelCommand = new CancelTicketCommand(ticket1);
        ticketManager.setCommand(cancelCommand);
        ticketManager.executeCommand();
        ticket1.displayTicketInfo();

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






        Train electricTrainClone = electricTrain.clone();
        Train dieselTrainClone = dieselTrain.clone();


        electricTrainClone.displayTrainInfo();
        dieselTrainClone.displayTrainInfo();

        TrainManager manager1 = TrainManager.getInstance();
        TrainManager manager2 = TrainManager.getInstance();


        System.out.println(manager1 == manager2);


        manager1.manageTrain(electricTrain);
        manager2.manageTrain(dieselTrain);
        
        Maintenance maintenance = factory.createMaintenance("2024-01-01", 180);
        maintenance.displayMaintenanceInfo();
        maintenance.isServiceDue("2024-06-01");

    }
}
