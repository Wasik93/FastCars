package wasik.fastcars;


import java.util.ArrayList;

public interface DatabaseInterface {
    void insertIntoDatabase(FiveElementTuple fiveElementTuple);

    ArrayList<FiveElementTuple> getFromDatabase();

    void resetDatabase();
}