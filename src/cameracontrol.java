import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.civil.LtiCivilDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;



public class cameracontrol extends gui {

    static Webcam webcam = Webcam.getDefault();

    static JFrame window = new JFrame("Live View");

    static WebcamPanel panel = new WebcamPanel(webcam);



    public static JFrame aquirebutton = new JFrame("aquirebutton");
     static JButton thebutton = new JButton("Aquire");



public static void setsizegetcontent() {


        aquirebutton.setSize(400, 70);

        aquirebutton.getContentPane().add(thebutton);

        aquirebutton.setVisible(true);
    }



    public static void returner () {

        //Acquire button action listener

        panel.resume();
        System.out.println("Webcam Acquired: " + webcam.getName());
//        webcam.setViewSize(new Dimension(1920, 1080));
        System.out.println("Resolution Set");
        webcam.open();



        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(false);



        window.add(panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        window.setSize(1360, 768);



    }


    public static void buttonevent() {

        aquirebuttontf = true;

        thebutton.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gui.acquirehandler(currentSettings);
                    gui.checkxbox(currentSettings);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }


            }


        });
    }
        public static Image captureclass  () {
            panel.pause();
            Image capture = panel.getImage();
            window.setVisible(false);
            JFrame ImpShow = new JFrame();
            ImpShow.setSize(800, 600);
            JLabel impshowlabel = new JLabel();
            ImpShow.getContentPane().add(impshowlabel);
            impshowlabel.setIcon(new ImageIcon(capture));
            ImpShow.setVisible(true);




            return capture;
        }




}

