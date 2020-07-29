import java.awt.*;


public class measurements {
    public static final int TopLeft = 1;

    double[] results;
    inspectionProfile profile;
    Boolean pass;
    Boolean hasImage;
    Image capture;
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


    public static void measurementsmethod(){





    }






}
