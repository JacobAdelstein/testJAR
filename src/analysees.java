import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class analysees extends gui {


    public static void analysisclass (){


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






    }
}
