import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamLockException;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.civil.LtiCivilDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;


interface cameraListener {
    void imageTaken();
}

public class cameraControl extends gui {
    Webcam webcam;
    static WebcamPanel panel;
    Integer position;
    Image capture;
    private List<cameraListener> listeners = new ArrayList<cameraListener>();

    public void addListener(cameraListener toAdd) {
        listeners.add(toAdd);
    }



    public cameraControl() throws InterruptedException {
        Webcam.setDriver(new LtiCivilDriver());

        webcam = Webcam.getDefault();
        webcam.close();

        //Sometimes the webcam can remain open when restarting the program, causing a crash.
        //So, we'll attempt to wait a second and try again if we can't open the webcam
        try {
            webcam.open();
        } catch (WebcamLockException ex) {
            System.out.println("Waiting for camera to close.... Trying again");
            sleep(2000);
            webcam.open();
        }
        panel = new WebcamPanel(webcam);



        gui.sysConsole.println("Camera Initialized");




    }

    public String toString() {
        return webcam.getName();
    }

    public WebcamPanel getPanel() {
        return panel;
    }

    public void startLive() {
        JFrame window = new JFrame("Live View");
        window.add(panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        window.setSize(1360, 768);

        panel.resume();
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(false);
        window.pack();
        window.setSize(1360, 768);

        JButton acquireButton = new JButton("Acquire");
        JFrame acquireFrame = new JFrame("Acquire Button");
        acquireFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        acquireFrame.setSize(400, 70);


        acquireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.sysConsole.println("Acquire Clicked");
                camera.getImage();
                acquireFrame.setVisible(false);
                window.setVisible(false);

            }
        });
        acquireFrame.add(acquireButton);
        window.setVisible(true);
        acquireFrame.setVisible(true);


    }
    public void stopLive() {
        panel.pause();
    }

    public void getImage() {

        panel.pause();
        capture = panel.getImage();
        panel.resume();
        for (cameraListener hl : listeners)
            hl.imageTaken();


    }
}

