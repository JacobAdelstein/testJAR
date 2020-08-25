import javax.swing.*;
import java.awt.*;

public class debugConsole {

    public final int INFO = 0;
    public final int WARNING = 1;


    JFrame console;
    TextArea textArea;
    public debugConsole() {
        console = new JFrame("Debug Console");
        JPanel textPanel = new JPanel();
        textArea = new TextArea();
        textArea.setColumns(100);
        textArea.setRows(20);
        textPanel.add(textArea);
        textArea.setEditable(false);
        console.add(textPanel);
        console.pack();

    }

    public void showConsole() {
        console.setVisible(true);
    }

    public void hideConsole() {
        console.setVisible(false);
    }

    public void println(String printString) {
        textArea.append(printString + "\n");
        System.out.println(printString);
    }

    public void println(String printString, int alertType) {
        textArea.append(printString + "\n");
        System.out.println(printString);
    }

    public void println(double printDouble) {
        textArea.append(String.valueOf(printDouble) + "\n");
    }


}
