import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

interface ClonableTrain {
    Train clone();
}

interface Command {
    void execute();
}

abstract class TrainOperationTemplate {
    public final void manageOperation() {
        startOperation();
        performOperation();
        endOperation();
    }

    protected abstract void startOperation();
    protected abstract void performOperation();
    protected abstract void endOperation();
}

class ElectricTrainOperation extends TrainOperationTemplate {
    private ElectricTrain train;

    public ElectricTrainOperation(ElectricTrain train) {
        this.train = train;
    }

    @Override
    protected void startOperation() {
        train.startEngine();
        System.out.println("Electric train operation started.");
    }

    @Override
    protected void performOperation() {
        System.out.println("Performing electric train operation...");
    }

    @Override
    protected void endOperation() {
        train.stopEngine();
        System.out.println("Electric train operation ended");
    }
}

class DieselTrainOperation extends TrainOperationTemplate {
    private DieselTrain train;

    public DieselTrainOperation(DieselTrain train) {
        this.train = train;
    }

    @Override
    protected void startOperation() {
        train.startEngine();
        System.out.println("Diesel train operation started.");
    }

    @Override
    protected void performOperation() {
        System.out.println("Performing diesel train operation...");
    }

    @Override
    protected void endOperation() {
        train.stopEngine();
        System.out.println("Diesel train operation ended.");
    }
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

// Memento
class TrainMemento {
    private final String trainId;
    private final String trainType;
    private final String departureTime;
    private final String arrivalTime;
    private final String status;

    public TrainMemento(String trainId, String trainType, String departureTime, String arrivalTime, String status) {
        this.trainId = trainId;
        this.trainType = trainType;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = status;
    }

    public String getTrainId() { return trainId; }
    public String getTrainType() { return trainType; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public String getStatus() { return status; }
}

interface TrainObserver {
    void update(String trainId, String message);
}

// Memento Manager
class MementoManager {
    private final Map<String, List<TrainMemento>> mementoMap = new HashMap<>();

    public void saveMemento(String trainId, TrainMemento memento) {
        mementoMap.putIfAbsent(trainId, new ArrayList<>());
        mementoMap.get(trainId).add(memento);
    }

    public TrainMemento getMemento(String trainId, int index) {
        List<TrainMemento> mementos = mementoMap.get(trainId);
        if (mementos != null && index < mementos.size()) {
            return mementos.get(index);
        }
        return null;
    }
}

class Train implements Visitable {
    private String trainId;
    private String trainType;
    private String departureTime;
    private String arrivalTime;
    private String status;
    private TrainContext trainContext;
    private MementoManager mementoManager = new MementoManager();
    private List<TrainObserver> observers = new ArrayList<>();

    public Train(TrainBuilder builder) {
        this.trainId = builder.trainId;
        this.trainType = builder.trainType;
        this.departureTime = builder.departureTime;
        this.arrivalTime = builder.arrivalTime;
        this.status = builder.status;
        this.trainContext = new TrainContext();
    }

    public Train(String trainId, String trainType, String departureTime, String arrivalTime, String status) {
        this.trainId = trainId;
        this.trainType = trainType;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = status;
        this.trainContext = new TrainContext();
    }

    public void setTrainState(TrainState state) {
        this.trainContext.setState(state);
    }

    public void applyState() {
        this.trainContext.applyState();
    }

    public String getTrainId() {
        return trainId;
    }

    public String getTrainType() {
        return trainType;
    }


    // Add an observer
    public void addObserver(TrainObserver observer) {
        observers.add(observer);
    }

    // Remove an observer
    public void removeObserver(TrainObserver observer) {
        observers.remove(observer);
    }

    // Notify all observers of a change
    private void notifyObservers(String message) {
        for (TrainObserver observer : observers) {
            observer.update(trainId, message);
        }
    }

    // Getter and Setter methods with notifications to observers

    public String getDepartureTime() {
        return departureTime;
    }
    public String getArrivalTime() {
        return arrivalTime;
    }
    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
        notifyObservers("Departure time updated to " + departureTime);
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
        notifyObservers("Arrival time updated to " + arrivalTime);
    }

    public String getStatus() {
        return status;
    }

    public TrainMemento saveState() {
        TrainMemento memento = new TrainMemento(trainId, trainType, departureTime, arrivalTime, status);
        mementoManager.saveMemento(trainId, memento);
        return memento;
    }

    public void restoreState(int index) {
        TrainMemento memento = mementoManager.getMemento(trainId, index);
        if (memento != null) {
            this.trainType = memento.getTrainType();
            this.departureTime = memento.getDepartureTime();
            this.arrivalTime = memento.getArrivalTime();
            this.status = memento.getStatus();
            System.out.println("State restored from memento");
        } else {
            System.out.println("No memento found at index: " + index);
        }
    }

    public void setStatus(String status) {
        this.status = status;
        notifyObservers("Status updated to " + status);
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

    @Override
    public void accept(TrainVisitor visitor) {
        visitor.visitTrain(this);
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
    public ElectricTrain(String trainId, String trainType, String departureTime, String arrivalTime, String status) {
        super(trainId, trainType, departureTime, arrivalTime, status);
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

    public DieselTrain(String trainId, String trainType, String departureTime, String arrivalTime, String status) {
        super(trainId, trainType, departureTime, arrivalTime, status);
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

class TrainStatusLogger implements TrainObserver {
    @Override
    public void update(String trainId, String message) {
        System.out.println("Train " + trainId + " notification: " + message);
    }

    public void logStatusChange(String trainId, String oldStatus, String newStatus) {
        System.out.println("Train ID: " + trainId + " changed status from " + oldStatus + " to " + newStatus);
    }
}


class TrainSchedule {
    private List<Train> trainList ;
    private List<Station> stations ;
    public TrainSchedule() {
        trainList = new ArrayList<>();
        stations = new ArrayList<>();
    }
    public void addTrain(Train train) {
        trainList.add(train);
    }

    public void addStation(Station station) {
        stations.add(station);
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


    public void displayAllStations() {
        for (Station station : stations) {
            station.displayStationInfo();
        }
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
            train.setStatus(newStatus); // This will automatically notify observers
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
            train.setDepartureTime(departureTime);
            train.setArrivalTime(arrivalTime);
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

class Passenger implements Visitable{
    private String name;
    private String ticketNumber;
    private String seatNumber;

    public Passenger(String name, String ticketNumber, String seatNumber) {
        this.name = name;
        this.ticketNumber = ticketNumber;
        this.seatNumber = seatNumber;
    }

    @Override
    public void accept(TrainVisitor visitor) {
        visitor.visitPassenger(this);
    }

    public void displayPassengerInfo() {
        System.out.println("Passenger: " + name + ", Ticket: " + ticketNumber + ", Seat: " + seatNumber);
    }

    public String getName() {
        return name;
    }
}

class DefaultStationFactory implements StationFactory {
    @Override
    public Station createStation(String stationName, String location) {
        return new Station(stationName, location);
    }
}

class Station implements Visitable {
    private String stationName;
    private String location;

    public Station(String stationName, String location) {
        this.stationName = stationName;
        this.location = location;
    }

    @Override
    public void accept(TrainVisitor visitor) {
        visitor.visitStation(this);
    }

    public String getStationName() {
        return stationName;
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

    public CargoTrain(String trainId, String trainType, String departureTime, String arrivalTime, String status, double cargoWeight) {
        super(trainId, trainType, departureTime,arrivalTime,status);
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

interface TrainState {
    void handle(TrainContext context);
}

class RunningState implements TrainState {
    @Override
    public void handle(TrainContext context) {
        System.out.println("The train is now running.");
        context.setState(this);
    }

    @Override
    public String toString() {
        return "Running State";
    }
}

class StoppedState implements TrainState {
    @Override
    public void handle(TrainContext context) {
        System.out.println("The train is stopped.");
        context.setState(this);
    }

    @Override
    public String toString() {
        return "Stopped State";
    }
}

class MaintenanceState implements TrainState {
    @Override
    public void handle(TrainContext context) {
        System.out.println("The train is under maintenance.");
        context.setState(this);
    }

    @Override
    public String toString() {
        return "Maintenance State";
    }
}

class TrainContext {
    private TrainState currentState;

    public TrainContext() {
        this.currentState = new StoppedState();
    }

    public void setState(TrainState state) {
        this.currentState = state;
    }

    public TrainState getState() {
        return currentState;
    }

    public void applyState() {
        this.currentState.handle(this);
    }
}

interface TrainVisitor {
    void visitTrain(Train train);
    void visitPassenger(Passenger passenger);
    void visitStation(Station station);
}

class ConcreteTrainVisitor implements TrainVisitor {
    @Override
    public void visitTrain(Train train) {
        System.out.println("Inspecting train: " + train.getTrainId());
    }

    @Override
    public void visitPassenger(Passenger passenger) {
        System.out.println("Inspecting passenger: " + passenger.getName());
    }

    @Override
    public void visitStation(Station station) {
        System.out.println("Inspecting station: " + station.getStationName());
    }
}

interface Visitable {
    void accept(TrainVisitor visitor);
}

abstract class TrainWithTemplate {

    public final void performService() {
        checkSystems();
        cleanTrain();
        refuelOrRecharge();
        testRun();
    }
    protected abstract void checkSystems();
    protected abstract void refuelOrRecharge();

    private void cleanTrain() {
        System.out.println("Train is being cleaned...");
    }

    private void testRun() {
        System.out.println("Performing test run...");
    }
}

class ElectricTrainWithTemplate extends TrainWithTemplate {
    @Override
    protected void checkSystems() {
        System.out.println("Checking electric systems...");
    }

    @Override
    protected void refuelOrRecharge() {
        System.out.println("Recharging batteries...");
    }
}

class DieselTrainWithTemplate extends TrainWithTemplate {
    @Override
    protected void checkSystems() {
        System.out.println("Checking diesel systems...");
    }

    @Override
    protected void refuelOrRecharge() {
        System.out.println("Refueling diesel tank...");
    }
}

class TrainSystem {
    public static void main(String[] args) {
        TrainSchedule schedule = new TrainSchedule();
        TicketManager ticketManager = new TicketManager();
        TrainManager trainManager = TrainManager.getInstance();
        ConcreteTrainVisitor visitor = new ConcreteTrainVisitor();

        TransportFactory transportFactory = new DefaultTransportFactory();

        MementoManager mementoManager = new MementoManager();

        TrainStatusLogger observer1 = new TrainStatusLogger();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Train Management System ---");
            System.out.println("1. Add Train");
            System.out.println("2. Display All Trains");
            System.out.println("3. Book Ticket");
            System.out.println("4. Cancel Ticket");
            System.out.println("5. Modify Ticket");
            System.out.println("6. Add Station");
            System.out.println("7. Display All Stations");
            System.out.println("8. Update Train Status");
            System.out.println("9. Train Operations start");
            System.out.println("10. Display info by id");
            System.out.println("11. Inspect");
            System.out.println("12. Change Train State");
            System.out.println("13. Clone Train");
            System.out.println("14. Restore Train state");
            System.out.println("13. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addTrain(schedule, scanner,transportFactory);
                case 2 -> displayAllTrains(schedule);
                case 3 -> bookTicket(ticketManager, scanner);
                case 4 -> cancelTicket(ticketManager, scanner);
                case 5 -> modifyTicket(ticketManager, scanner);
                case 6 -> addStation(schedule, scanner,transportFactory);
                case 7 -> displayAllStations(schedule);
                case 8 -> updateTrainStatus(schedule, scanner);
                case 9 -> startTrainOperations(schedule,scanner);
                case 10 -> manageTrain(trainManager,schedule,scanner);
                case 11 -> inspectObjects(visitor, schedule, scanner);
                case 12 ->  changeTrainState(schedule, scanner);
                case 13 -> cloneTrain(schedule, scanner);
                case 14 -> restoreTrainState(schedule,scanner,mementoManager);
                case 15 -> {
                    System.out.println("Exiting system...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addTrain(TrainSchedule schedule, Scanner scanner,TransportFactory transportFactory) {
        System.out.print("Enter Train ID: ");
        String trainId = scanner.nextLine();
        System.out.print("Enter Train Type: ");
        String trainType = scanner.nextLine();
        System.out.print("Enter Departure Time: ");
        String departureTime = scanner.nextLine();
        System.out.print("Enter Arrival Time: ");
        String arrivalTime = scanner.nextLine();
        System.out.print("Enter Status: ");
        String status = scanner.nextLine();

        Train train = new Train.TrainBuilder()
                .withTrainId(trainId)
                .withTrainType(trainType)
                .withDepartureTime(departureTime)
                .withArrivalTime(arrivalTime)
                .withStatus(status)
                .build();

        train.addObserver(new TrainStatusLogger());
        schedule.addTrain(train);
        System.out.println("Train added successfully!");
    }

    private static void displayAllTrains(TrainSchedule schedule) {
        TrainIterator iterator = schedule.iterator();
        while (iterator.hasNext()) {
            iterator.next().displayTrainInfo();
        }
    }

    private static void bookTicket(TicketManager ticketManager, Scanner scanner) {
        System.out.print("Enter Ticket ID: ");
        String ticketId = scanner.nextLine();
        System.out.print("Enter Passenger Name: ");
        String passengerName = scanner.nextLine();
        System.out.print("Enter Seat Number: ");
        String seatNumber = scanner.nextLine();

        Ticket ticket = new Ticket(ticketId, passengerName, seatNumber);
        ticketManager.setCommand(new BookTicketCommand(ticket));
        ticketManager.executeCommand();
    }

    private static void cancelTicket(TicketManager ticketManager, Scanner scanner) {
        System.out.print("Enter Ticket ID: ");
        String ticketId = scanner.nextLine();
        System.out.print("Enter Passenger Name: ");
        String passengerName = scanner.nextLine();
        System.out.print("Enter Seat Number: ");
        String seatNumber = scanner.nextLine();

        Ticket ticket = new Ticket(ticketId, passengerName, seatNumber);
        ticketManager.setCommand(new CancelTicketCommand(ticket));
        ticketManager.executeCommand();
    }

    private static void modifyTicket(TicketManager ticketManager, Scanner scanner) {
        System.out.print("Enter Ticket ID: ");
        String ticketId = scanner.nextLine();
        System.out.print("Enter Passenger Name: ");
        String passengerName = scanner.nextLine();
        System.out.print("Enter Current Seat Number: ");
        String currentSeat = scanner.nextLine();
        System.out.print("Enter New Seat Number: ");
        String newSeat = scanner.nextLine();

        Ticket ticket = new Ticket(ticketId, passengerName, currentSeat);
        ticketManager.setCommand(new ModifyTicketCommand(ticket, newSeat));
        ticketManager.executeCommand();
    }

    private static void updateTrainStatus(TrainSchedule schedule, Scanner scanner) {
        System.out.print("Enter Train ID: ");
        String trainId = scanner.nextLine();
        System.out.print("Enter New Status: ");
        String newStatus = scanner.nextLine();

        TrainUpdaterStrategy updater = new TrainStatusUpdater(newStatus);
        updater.update(schedule, trainId);
    }

    private static void startTrainOperations(TrainSchedule schedule, Scanner scanner) {
        System.out.print("Enter Train ID to start operations: ");
        String trainId = scanner.nextLine();
        Train train = schedule.getTrainById(trainId);

        if (train == null) {
            System.out.println("Train not found.");
            return;
        }

        TrainOperationTemplate operation;

        if ("Electric".equalsIgnoreCase(train.getTrainType())) {
            operation = new ElectricTrainOperation((ElectricTrain) train);
        } else if ("Diesel".equalsIgnoreCase(train.getTrainType())) {
            operation = new DieselTrainOperation((DieselTrain) train);
        } else {
            System.out.println("Invalid train type for operations.");
            return;
        }


        operation.manageOperation();
    }

    private static void manageTrain(TrainManager trainManager, TrainSchedule schedule, Scanner scanner) {
        System.out.print("Enter Train ID to manage: ");
        String trainId = scanner.nextLine();
        Train train = schedule.getTrainById(trainId);

        if (train == null) {
            System.out.println("Train not found.");
            return;
        }


        trainManager.manageTrain(train);
    }
    private static void inspectObjects(ConcreteTrainVisitor visitor, TrainSchedule schedule, Scanner scanner) {
        System.out.println("\n--- Inspection Menu ---");
        System.out.println("1. Inspect a Train");
        System.out.println("2. Inspect a Passenger");
        System.out.println("3. Inspect a Station");
        System.out.print("Choose an option: ");

        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1 -> {
                System.out.print("Enter Train ID to inspect: ");
                String trainId = scanner.nextLine();
                Train train = schedule.getTrainById(trainId);
                if (train != null) {
                    train.accept(visitor);
                } else {
                    System.out.println("Train not found.");
                }
            }
            case 2 -> {
                System.out.print("Enter Passenger Name to inspect: ");
                String passengerName = scanner.nextLine();
                Passenger passenger = findPassenger(passengerName, "A1", "11");
                if (passenger != null) {
                    passenger.accept(visitor);
                } else {
                    System.out.println("Passenger not found.");
                }
            }

            case 3 -> {
                System.out.print("Enter Station Name to inspect: ");
                String stationName = scanner.nextLine();
                Station station = findStation(stationName,"USA");
                if (station != null) {
                    station.accept(visitor);
                } else {
                    System.out.println("Station not found.");
                }
            }
            default -> System.out.println("Invalid option.");
        }
    }

    private static Passenger findPassenger(String name, String ticketNumber, String seatNumber) {
        return new Passenger(name, ticketNumber, seatNumber);
    }

    private static Station findStation(String stationName, String location) {
        return new Station(stationName, location);
    }
    private static void addStation(TrainSchedule schedule, Scanner scanner,TransportFactory transportFactory) {
        System.out.print("Enter Station Name: ");
        String stationName = scanner.nextLine();
        System.out.print("Enter Station Location: ");
        String location = scanner.nextLine();

        Station station = transportFactory.createStation(stationName, location);
        schedule.addStation(station);
        System.out.println("Station added successfully!");
    }

    private static void displayAllStations(TrainSchedule schedule) {
        System.out.println("--- All Stations ---");
        schedule.displayAllStations();
    }
    private static void cloneTrain(TrainSchedule schedule, Scanner scanner) {
        System.out.print("Enter Train ID to clone: ");
        String trainId = scanner.nextLine();
        Train originalTrain = schedule.getTrainById(trainId);

        originalTrain.saveState();

        if (originalTrain == null) {
            System.out.println("Train not found.");
            return;
        }


        Train clonedTrain = originalTrain.clone();
        clonedTrain.setTrainId(trainId + "_CLONE");

        schedule.addTrain(clonedTrain);
        System.out.println("Train cloned successfully!");
    }
    private static void changeTrainState(TrainSchedule schedule, Scanner scanner) {
        System.out.print("Enter Train ID to change state: ");
        String trainId = scanner.nextLine();
        Train train = schedule.getTrainById(trainId);

        if (train == null) {
            System.out.println("Train not found.");
            return;
        }

        System.out.println("\nSelect New State for Train: ");
        System.out.println("1. Running");
        System.out.println("2. Stopped");
        System.out.println("3. Under Maintenance");
        System.out.print("Enter your choice: ");
        int stateChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        train.saveState();

        switch (stateChoice) {
            case 1 -> train.setTrainState(new RunningState());
            case 2 -> train.setTrainState(new StoppedState());
            case 3 -> train.setTrainState(new MaintenanceState());
            default -> System.out.println("Invalid option.");
        }

        train.applyState();
    }
    private static void restoreTrainState(TrainSchedule schedule, Scanner scanner, MementoManager mementoManager) {
        System.out.print("Enter Train ID to restore state: ");
        String trainId = scanner.nextLine();
        Train train = schedule.getTrainById(trainId);

        if (train == null) {
            System.out.println("Train not found.");
            return;
        }


        System.out.print("Enter memento index to restore (0 for the most recent): ");
        int index = scanner.nextInt();
        scanner.nextLine();


        train.restoreState(index);
    }

}