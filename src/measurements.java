import java.awt.*;


public class measurements {
    public static final int TopLeft = 1;

    result[] results;
    inspectionProfile profile;
    Boolean pass;
    Boolean hasImage;
    Image capture;
    Image overlay;
    String name;
    Integer position;
    Integer partNum;

    public measurements(Integer pos, Integer partNumber, inspectionProfile currentProfile) {
        position = pos;
        partNum = partNumber;
        hasImage = false;
        pass = false;
        profile = currentProfile;

        switch (position) {
            case TopLeft:
                name = "Top Left";
                break;
            case 2:
                name = "Top Right";
                break;
            case 3:
                name = "Center";
                break;
            case 4:
                name = "Bottom Left";
                break;
            case 5:
                name = "Bottom Right";
                break;


        }

    }

    public String toString() {
        return "Part: " + partNum + " Position: " + position + " Display Name: " + name + " Has Image: " + hasImage;

    }

    public void setImage(Image image) {
        capture = image;
        hasImage = true;
    }

    public void removeImage() {
        capture = null;
        pass = false;
        hasImage = false;
    }

    public boolean checkPass() {
        //This method compares each result stored in results[] to the feret min/max stored in inspection profile
        //This method sets the passing status of each result along with the overall pass stored in measurements
        //This method will also return a boolean with passing status
        boolean allPass = true;
        gui.sysConsole.println("Results length: " + String.valueOf(results.length));
        for (int i =0; i < results.length; i++) {
            gui.sysConsole.println("Printing for " + String.valueOf(i) + " , " + String.valueOf(results[i].getResult()));
            gui.sysConsole.println("FeretMax " + String.valueOf(profile.feretMax));
            gui.sysConsole.println("FeretMin " + String.valueOf(profile.feretMin));

            if (results[i].getResult() > profile.feretMax || results[i].getResult() < profile.feretMin) {
                results[i].setPass(false);
                allPass = false;
            } else {
                results[i].setPass(true);
            }
        }
        if (results.length == 0) {
            allPass = false;
        }
        pass = allPass;
        return allPass;
    }
}
