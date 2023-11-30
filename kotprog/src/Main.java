import game.engine.Engine;

public class Main {
    public static void main(String[] args) {
        String[] args1 = {"0", "game.racetrack.RaceTrackGame", "11", "27", "5", "0.1", "10", "1234567890", "1000", "Agent"};
        try{
            Engine.main(args1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
