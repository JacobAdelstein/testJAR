import com.github.sarxos.webcam.*;
import com.github.sarxos.webcam.ds.civil.LtiCivilDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


interface cameraListener {
    void imageTaken();
}

public class cameraControl extends gui implements WebcamDiscoveryListener, MouseListener, WindowListener, WebcamListener{
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


//        webcam = Webcam.getDefault();
//        webcam.close();
        picker();

        //Sometimes the webcam can remain open when restarting the program, causing a crash.
        //So, we'll attempt to wait a second and try again if we can't open the webcam
//        try {
//            webcam.open();
//        } catch (WebcamLockException ex) {
//            System.out.println("Waiting for camera to close.... Trying again");
//            sleep(2000);
//            webcam.open();
//        }
//        panel = new WebcamPanel(webcam);
//
//
//        gui.sysConsole.println("Camera Initialized");


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

    public void picker() {
        Webcam.addDiscoveryListener((WebcamDiscoveryListener) this);



//        addWindowListener((WindowListener) this);
//        addMouseListener((MouseListener) this);



        JButton b = new JButton("Submit");
        WebcamPicker picker = new WebcamPicker();



        JFrame p = new JFrame();

        p.add(picker);
        p.setVisible(true);



//        webcam.addWebcamListener((WebcamListener) this);





        p.setVisible(true);

        p.getContentPane().setLayout(new FlowLayout());

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                webcam = picker.getSelectedWebcam();
                webcam.open();
                panel = new WebcamPanel(webcam);
                p.dispose();

            }

        });
        p.add(b);

        p.setTitle("Select Webcam");
        p.setSize(600, 100);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        p.setBounds((int) ((width - p.getWidth()) / 2), (int) ((height - p.getHeight()) / 2), 600, 100);



    }




    @Override
    public void webcamFound(WebcamDiscoveryEvent event) {

    }

    @Override
    public void webcamGone(WebcamDiscoveryEvent event) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {



    }

    @Override
    public void mouseReleased(MouseEvent e) {
        startLive();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void webcamOpen(WebcamEvent we) {

    }

    @Override
    public void webcamClosed(WebcamEvent we) {

    }

    @Override
    public void webcamDisposed(WebcamEvent we) {

    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {

    }
}

