import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.civil.LtiCivilDriver;

import javax.swing.*;
import java.awt.*;

public class cameracontrol {

    static Webcam webcam = Webcam.getDefault();

   static JFrame window = new JFrame("Live View");

   static WebcamPanel panel = new WebcamPanel(webcam);

    public static void returner (JFrame aquirebutton) {
        panel.resume();
        System.out.println("Webcam Acquired: " + webcam.getName());
//        webcam.setViewSize(new Dimension(1920, 1080));
        System.out.println("Resolution Set");
        webcam.open();

        aquirebutton.setVisible(true);

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
        public static Image captureclass  (JFrame aquirebutton) {
            panel.pause();
            Image capture = panel.getImage();
            window.setVisible(false);
            aquirebutton.setVisible(false);
            JFrame ImpShow = new JFrame();
            ImpShow.setSize(800, 600);
            JLabel impshowlabel = new JLabel();
            ImpShow.getContentPane().add(impshowlabel);
            impshowlabel.setIcon(new ImageIcon(capture));
            ImpShow.setVisible(true);




            return capture;
        }


        //Show capture



    }

