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
        JFrame newFrame = new JFrame("Showing Top Left");
        JPanel newJpanel = new JPanel();
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(gui.camera.capture));
        newJpanel.add(label);
        newFrame.add(newJpanel);
        newFrame.setVisible(true);

        measurements currentMeasure = new measurements(gui.currentCapture[1], gui.camera.capture, gui.currentCapture[0]);



        //Do Image Analysis
        ImagePlus imp = new ImagePlus("capture", gui.camera.capture);
        imp.changes = false;

        IJ.run(imp, "8-bit", "");
        IJ.setThreshold(imp, currentSettings.lowerThreshold, currentSettings.upperThreshold);
        IJ.setProperty("BlackBackground", currentSettings.blackBackground);
        IJ.run(imp, "Convert to Mask", "");
        StringBuilder scale = new StringBuilder();
        scale.append("distance=" + currentSettings.distance + " known=" + currentSettings.known + " pixel=" + currentSettings.pixel + " unit=um");
        StringBuilder analysis = new StringBuilder();
        analysis.append("size=" + currentSettings.sizeMin + "-" + currentSettings.sizeMax + " circularity=" + currentSettings.circularityMin + "-" + currentSettings.circularityMax + " show=[Overlay Masks] clear include in-situ");


        IJ.run(imp, "Set Scale...", scale.toString());
        IJ.run(imp, "Set Measurements...", "feret's");
//        IJ.run(imp, "Analyze Particles...", "size=800-Infinity circularity=0.70-1.00 show=[Overlay Masks] clear include in-situ");
        IJ.run(imp, "Analyze Particles...", analysis.toString());



        ResultsTable rt = ResultsTable.getResultsTable();
        feretCol = rt.getColumnAsDoubles(19);










        //Save image and analysis results
        for (measurementsCol measurement : gui.storage) {
            if (measurement.partNum == gui.currentCapture[1]) {
                if (gui.currentCapture[0] == 1) {
                    measurement.TL = currentMeasure;
                    System.out.println("TL Saved");
                }
            }
        }



//        for (int i = 1; i <= gui.tabbedPane.getComponentCount(); i++) {
//            gui.tabbedPane.getComponentAt(i);
//
//
//
//        }


    }
}
