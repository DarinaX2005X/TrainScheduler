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

class ElectricTrain extends Train implements TrainOperations {

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
}

class DieselTrain extends Train implements TrainOperations {

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
    }
}

