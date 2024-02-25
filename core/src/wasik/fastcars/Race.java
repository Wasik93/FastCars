package wasik.fastcars;

import java.util.ArrayList;
public class Race {
    private final Integer numberLaps;

    private int currentLaps;

    private final FastCars game;

    private boolean raceStarted = false;

    private boolean sector1 = true;

    private boolean sector2 = true;

    private long raceTime;

    private long raceEnd;

    private long raceStart;

    private long lapStart;

    private final String map;

    private ArrayList<Long> lapTimes;

    Race(FastCars fastCars, String map, Integer numberLaps){
        this.map = map;
        this.numberLaps = numberLaps;
        this.game = fastCars;
        this.currentLaps = 0;
        this.lapTimes = new ArrayList<>();
        this.raceTime = 0;
        this.raceStart = 0;
        this.lapStart = 0;
    }

    public void raceStarted(){
        raceStarted = true;
        raceStart = System.currentTimeMillis();
    }

    public void endRace(){
        if(sector1 && sector2) {
            raceEnd = System.currentTimeMillis();
            lapTimes.add(raceEnd - lapStart);
            game.setScreen(new RaceEndScreen(game, this));
        }
    }

    public void addLap(){
        if(!raceStarted){
            raceStarted();
        }
        if(sector1 && sector2) {
            currentLaps++;
            sector1 = false;
            sector2 = false;
            raceTime = System.currentTimeMillis();
            if(lapStart != 0){
                lapTimes.add(raceTime - lapStart);
            }
            lapStart = System.currentTimeMillis();
        }
    }

    public void acceptSector1(){
        if(!sector2 && !sector1){
            sector1 = true;
        }
    }

    public void acceptSector2(){
        if(sector1 && !sector2){
            sector2 = true;
        }
    }

    public int getNumberLaps(){
        return numberLaps;
    }

    public int getCurrentLaps(){
        return currentLaps;
    }

    public int remainingLaps(){
        return numberLaps - currentLaps;
    }

    public long getRaceStart(){
        return raceStart;
    }

    public boolean isRaceStarted() {
        return raceStarted;
    }

    public long getRaceEnd() {
        return raceEnd;
    }

    public String getMap() {
        return map;
    }

    public ArrayList<Long> getLapTimes() {
        return lapTimes;
    }
}
