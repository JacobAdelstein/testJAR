import javax.swing.*;
import java.awt.*;

public class settingsEntry extends JPanel {


    JTextField settingField;
    JLabel settingLabel;


    public settingsEntry(String label, String initial) {

        settingLabel = new JLabel(label);
        settingField = new JTextField(initial, 15);
        add(settingLabel);
        add(settingField);
        setLayout(new GridLayout(1,2));


    }



    public String getText() {
        return this.settingField.getText();
    }


}
