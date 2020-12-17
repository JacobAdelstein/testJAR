
import java.awt.*;


public class measurements {
    public static final int F_TopLeft = 1;
    public static final int F_TopRight = 2;
    public static final int F_Center = 3;
    public static final int F_BotLeft = 4;
    public static final int F_BotRight = 5;
    public static final int B_TopLeft = 6;
    public static final int B_TopRight = 7;
    public static final int B_Center = 8;
    public static final int B_BotRight = 9;
    public static final int B_BotLeft = 10;



    double[] results;
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
            case F_TopLeft:
                name = "Top Left Front";
                break;
            case F_TopRight:
                name = "Top Right Front";
                break;
            case F_Center:
                name = "Center Front";
                break;
            case F_BotLeft:
                name = "Bottom Left Front";
                break;
            case F_BotRight:
                name = "Bottom Right Front";
                break;
            case B_TopLeft:
                name = "Top Left Back";
                break;
            case B_TopRight:
                name = "Top Right Back";
                break;
            case B_Center:
                name = "Center Back";
                break;
            case B_BotLeft:
                name = "Bottom Left Back";
                break;
            case B_BotRight:
                name = "Bottom Right Back";
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


    public void setResults(double[] results) {
        this.results = results;




    }

    public void setPass(Boolean pass) {
        this.pass = pass;
    }


}
