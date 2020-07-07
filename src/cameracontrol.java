import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.civil.LtiCivilDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


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



    public cameraControl() {
        Webcam.setDriver(new LtiCivilDriver());
        webcam = Webcam.getDefault();
        panel = new WebcamPanel(webcam);



        System.out.println("CameraContol Active");




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
                System.out.println("Acquire Clicked");
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
//        acquireFrame.setVisible(false);
//        window.setVisible(false);

        panel.pause();
        capture = panel.getImage();
        panel.resume();
        for (cameraListener hl : listeners)
            hl.imageTaken();


    }



//    static Webcam webcam = Webcam.getDefault();
//
//    static JFrame window = new JFrame("Live View");
//
//    static WebcamPanel panel = new WebcamPanel(webcam);



//    public static JFrame aquirebutton = new JFrame("aquirebutton");
//    static JButton thebutton = new JButton("Aquire");



    public static void setsizegetcontent() {


//        aquirebutton.setSize(400, 70);

//        aquirebutton.getContentPane().add(thebutton);

//        aquirebutton.setVisible(true);
    }



//    public static void returner () {
//
//        //Acquire button action listener
//
//        panel.resume();
//        System.out.println("Webcam Acquired: " + webcam.getName());
////        webcam.setViewSize(new Dimension(1920, 1080));
//        System.out.println("Resolution Set");
//        webcam.open();
//
//
//
//        panel.setFPSDisplayed(true);
//        panel.setDisplayDebugInfo(true);
//        panel.setImageSizeDisplayed(true);
//        panel.setMirrored(false);
//
//
//
//        window.add(panel);
//        window.setResizable(true);
//        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        window.pack();
//        window.setVisible(true);
//        window.setSize(1360, 768);
//
//
//
//    }
//
//
//    public static void buttonevent() {
//
//
//
//        thebutton.addActionListener(new ActionListener() {
//
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                captureclass();
//
//
//            }
//
//
//        });
//    }
//    public static Image captureclass  () {
//        panel.pause();
//        Image capture = panel.getImage();
//        window.setVisible(false);
//        JFrame ImpShow = new JFrame();
//        ImpShow.setSize(800, 600);
//        JLabel impshowlabel = new JLabel();
//        ImpShow.getContentPane().add(impshowlabel);
//        impshowlabel.setIcon(new ImageIcon(capture));
//        ImpShow.setVisible(true);
//
//        return capture;
//        }




}

