import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class settingsPanel extends JPanel {
    ArrayList<settingsEntry> settingsArray;
    inspectionProfile profile;


    public settingsPanel(inspectionProfile insProfile){
        settingsArray = new ArrayList<>(0);
        profile = insProfile;

        settingsArray.add(new settingsEntry("Size Min", String.valueOf(profile.getSizeMin())));
        settingsArray.add(new settingsEntry("Size Max", String.valueOf(profile.getSizeMax())));
        settingsArray.add(new settingsEntry("Circularity Min", String.valueOf(profile.getCircularityMin())));
        settingsArray.add(new settingsEntry("Circularity Max", String.valueOf(profile.getCircularityMax())));
        settingsArray.add(new settingsEntry("Distance", String.valueOf(profile.getDistance())));
        settingsArray.add(new settingsEntry("Known", String.valueOf(profile.getKnown())));
        settingsArray.add(new settingsEntry("Pixel", String.valueOf(profile.getPixel())));
        settingsArray.add(new settingsEntry("Lower Threshold", String.valueOf(profile.getLowerThreshold())));
        settingsArray.add(new settingsEntry("Upper Threshold", String.valueOf(profile.getUpperThreshold())));
        settingsArray.add(new settingsEntry("Black Background", String.valueOf(profile.getBlackBackground())));
        settingsArray.add(new settingsEntry("Hole Count", String.valueOf(profile.getHoleCount())));

        for (settingsEntry entry : settingsArray) {
            add(entry);
        }




        setLayout(new GridLayout(11,2));

    }

    private settingsEntry getEntry(String label) {
        for (settingsEntry entry: settingsArray) {
            if (entry.settingLabel.getText().equals(label)) {
                return entry;
            }
        }
        return null;
    }



    public inspectionProfile saveProfile() {
        profile.setSizeMin(Double.parseDouble(getEntry("Size Min").settingField.getText()));
        profile.setSizeMax(Double.parseDouble(getEntry("Size Max").settingField.getText()));
        profile.setCircularityMin(Double.parseDouble(getEntry("Circularity Min").settingField.getText()));
        profile.setCircularityMax(Double.parseDouble(getEntry("Circularity Max").settingField.getText()));
        profile.setPixel(Double.parseDouble(getEntry("Pixel").settingField.getText()));
        profile.setBlackBackground(Boolean.parseBoolean(getEntry("Black Background").settingField.getText()));
        profile.setKnown(Double.parseDouble(getEntry("Known").settingField.getText()));
        profile.setDistance(Double.parseDouble(getEntry("Distance").settingField.getText()));
        profile.setLowerThreshold(Double.parseDouble(getEntry("Lower Threshold").settingField.getText()));
        profile.setUpperThreshold(Double.parseDouble(getEntry("Upper Threshold").settingField.getText()));
        profile.setHoleCount(Integer.parseInt(getEntry("Hole Count").settingField.getText()));

        return profile;
    }






}
