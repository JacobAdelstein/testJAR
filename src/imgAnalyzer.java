import ij.IJ;
import ij.ImagePlus;
import ij.measure.ResultsTable;

import javax.swing.*;


public class imgAnalyzer implements cameraListener {


    public imgAnalyzer() {
        System.out.println("Image Analysis Loaded");

    }

    @Override

    public void imageTaken() {
        System.out.println("An Image was taken");
//        JFrame newFrame = new JFrame("Showing Top Left");
//        JPanel newJpanel = new JPanel();
//        JLabel label = new JLabel();
//        label.setIcon(new ImageIcon(gui.camera.capture));
//        newJpanel.add(label);
//        newFrame.add(newJpanel);
//        newFrame.setVisible(true);
//        measurements currentMeasure = new measurements(gui.currentCapture[1], gui.camera.capture, gui.currentCapture[0]);

        double[] results = new double[1];
        //First we determine which tab took the image
        for (measurementsCol measurement : gui.storage) {
            if (measurement.partNum == gui.currentCapture[0]) {
                //Do Image Analysis
                ImagePlus imp = new ImagePlus("capture", gui.camera.capture);
                imp.changes = false;

                IJ.run(imp, "8-bit", "");
                IJ.setThreshold(imp, measurement.currentProfile.lowerThreshold, measurement.currentProfile.upperThreshold);
                IJ.setProperty("BlackBackground", measurement.currentProfile.blackBackground);
                IJ.run(imp, "Convert to Mask", "");

                String scale = ("distance=" + measurement.currentProfile.distance + " known=" + measurement.currentProfile.known + " pixel=" + measurement.currentProfile.pixel + " unit=um");
                String analysis = ("size=" + measurement.currentProfile.sizeMin + "-" + measurement.currentProfile.sizeMax + " circularity=" + measurement.currentProfile.circularityMin + "-" + measurement.currentProfile.circularityMax + " show=[Overlay Masks] clear include in-situ");
                IJ.run(imp, "Set Scale...", scale);
                IJ.run(imp, "Set Measurements...", "feret's");
//        IJ.run(imp, "Analyze Particles...", "size=800-Infinity circularity=0.70-1.00 show=[Overlay Masks] clear include in-situ");
                IJ.run(imp, "Analyze Particles...", analysis);


                System.out.println("SCALE: " + scale);
                System.out.println("Analysis: " + analysis);
                ResultsTable rt = ResultsTable.getResultsTable();
                results = rt.getColumnAsDoubles(19);
                try {
                    System.out.println("--------------RESULTS--------------");
                    System.out.println("Number of results: " + results.length);
                    for (int i = 0; i < results.length; i++) {
                        System.out.println(results[i]);
                    }
                    System.out.println("--------------END RESULTS--------------");
                } catch (NullPointerException  ex) {
                    JOptionPane.showMessageDialog(null, "No holes found");
                    results = new double[1];
                }




            }
        }















        //Save image and analysis results
        for (measurementsCol measurement : gui.storage) {
            if (measurement.partNum == gui.currentCapture[0]) {
                for (int i = 0; i < measurement.measureList.size(); i++){
                    if (measurement.measureList.get(i).position == gui.currentCapture[1]){
                        measurement.measureList.get(i).setImage(gui.camera.capture);
                        measurement.measureList.get(i).results = results;
                        //lol good luck trying to decipher this bullshit
                    }
                }

            }
        }



//        for (int i = 1; i <= gui.tabbedPane.getComponentCount(); i++) {
//            gui.tabbedPane.getComponentAt(i);
//
//
//
//        }

        guiHandler.updateTabbedPane();


    }
}
