
import ij.IJ;
import ij.ImagePlus;
import ij.measure.ResultsTable;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class imp {

    public static double[] feretCol;


    public static double[] impmethod(settings currentSettings)  {

        //Print out settings


        try {



            InputStream in = imp.class.getResourceAsStream("config.txt");
            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = input.readLine()) != null) {
                if (line.startsWith("[WindowTitle]")) {
                    currentSettings.windowTitle = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[sizeMin]")) {
                    currentSettings.sizeMin = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[sizeMax]")) {
                    currentSettings.sizeMax = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[circularityMin]")) {
                    currentSettings.circularityMin = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[circularityMax]")) {
                    currentSettings.circularityMax = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[lowerThreshold]")) {
                    currentSettings.lowerThreshold = new Double(line.substring(line.indexOf("=") + 1));
                }
                if (line.startsWith("[upperThreshold]")) {
                    currentSettings.upperThreshold = new Double(line.substring(line.indexOf("=") + 1));
                }
                if (line.startsWith("[feretMax]")) {
                    currentSettings.feretMax = new Double(line.substring(line.indexOf("=") + 1));
                }
                if (line.startsWith("[feretMin]")) {
                    currentSettings.feretMin = new Double(line.substring(line.indexOf("=") + 1));
                }
                if (line.startsWith("[distance]")) {
                    currentSettings.distance = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[known]")) {
                    currentSettings.known = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[pixel]")) {
                    currentSettings.pixel = line.substring(line.indexOf("=") + 1);
                }
                if (line.startsWith("[blackBackground]")) {
                    if (line.substring(line.indexOf("=") + 1).trim().equalsIgnoreCase("TRUE")) {
                        currentSettings.blackBackground = true;
                    } else {
                        currentSettings.blackBackground = false;
                    }


                }
            }
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        Image capture = cameracontrol.captureclass();



        ImagePlus imp = new ImagePlus("capture", capture);
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







        for (double curr : feretCol){
            System.out.println("Printing FERETCOL: " + curr);


        }




        StringBuilder message = new StringBuilder();
        message.delete(0, message.length());
        message.append("Results: \n");


return feretCol;
    }









}




















