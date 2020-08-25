import java.awt.*;
import java.util.ArrayList; // import the ArrayList class

public class measurementsCol {
    inspectionProfile currentProfile;
    Integer partNum;
    ArrayList<measurements> measureList;
    String batchDate;


    public measurementsCol(Integer partNumber, inspectionProfile insProfile) {
        partNum = partNumber;
        measureList = new ArrayList<measurements>();
        currentProfile = insProfile;
        gui.sysConsole.println("MeasureList Size: " + measureList.size());

        for (int i = 0; i < 10; i++) {
            measureList.add(new measurements(i + 1, partNumber, currentProfile));
        }

        for (int i = 0; i < measureList.size(); i++) {
            gui.sysConsole.println("Setting measurement for partNum: " + partNum + " and Position: " + (i + 1));
        }

        gui.sysConsole.println("Printing toString: ");
        gui.sysConsole.println(this.toString());
    }


    public String toString() {
        gui.sysConsole.println("Generating toString: ");

        StringBuilder returnString = new StringBuilder();
        for (int i = 0; i < measureList.size(); i++) {
            returnString.append(measureList.get(i).toString() + "\n");
        }
        return returnString.toString();
    }

    public String getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(String date) {
        this.batchDate = date;
    }




}