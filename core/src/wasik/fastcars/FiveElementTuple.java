package wasik.fastcars;
public class FiveElementTuple {
    Integer id;
    String mapName;
    String raceTime;
    Integer numberOfLaps;
    String bestLapTime;
    FiveElementTuple(Integer id, String mapName, String raceTime, Integer numberOfLaps, String bestLapTime){
        this.id = id;
        this.mapName = mapName;
        this.raceTime = raceTime;
        this.numberOfLaps = numberOfLaps;
        this.bestLapTime = bestLapTime;
    }

}
