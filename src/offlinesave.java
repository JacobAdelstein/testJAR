import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class offlinesave {


    public static void filewrite(measurementsCol data, int techid) throws IOException {


        int partNum = 999;
        boolean success = false;
        String fileExtension = ".txt";

        System.out.println("test");
        String baseDir = gui.currentSettings.offlineSaveDirectory;

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println("YEAR: " + year + " MONTH: " + month + " day: " + dayOfMonth);

        StringBuilder filePath = new StringBuilder(baseDir);
        filePath.append("\\");
        filePath.append(year);
        filePath.append("\\");
        filePath.append(month);
        filePath.append("\\");
        filePath.append(dayOfMonth);


        try {
            File file = new File(filePath.toString());
            success = file.mkdirs();
            System.out.println("Created? " + success);
        } catch (Exception e) {
            e.printStackTrace();
        }


        filePath.append("\\");
        filePath.append(String.valueOf(partNum) + fileExtension);


        File txt = new File(filePath.toString());
        if (!txt.exists()) {
            System.out.println("File created? " + txt.createNewFile());
        } else {
            System.out.println("FILE EXISTS");
        }

        System.out.println(filePath);




        FileWriter fw = new FileWriter(txt);
        BufferedWriter bw = new BufferedWriter(fw);

        try {
            bw.write("TechID:"+ techid);
            bw.write("\nDate:"+ LocalDate.now() + " "+ "Time:" + LocalTime.now());
              bw.write("\nProfile Type:" + data.currentProfile.profileType);

              bw.write("\nProfile Name:" + data.currentProfile.profileName);

              bw.write("\nSubmit Address:" + data.currentProfile.submitAddress);

              bw.write("\nSize Min:" + data.currentProfile.sizeMin);

              bw.write("\nSize Max:" + data.currentProfile.sizeMax);

              bw.write("\nCircularity Min:" + data.currentProfile.circularityMin);

              bw.write("\nCircularity Max:" + data.currentProfile.circularityMax);

              bw.write("\nDistance:" + data.currentProfile.distance);

              bw.write( "\nKnown:" + data.currentProfile.known);

              bw.write("\nPixel:" + data.currentProfile.pixel);

              bw.write( "\nenterMin:" + data.currentProfile.enterMin);

              bw.write("\nenterMax:" + data.currentProfile.enterMax);

              bw.write( "\nexitMin:" + data.currentProfile.exitMin);

              bw.write( "\nexitMax:" + data.currentProfile.exitMax);

              bw.write( "\nlowerThreshold:" + data.currentProfile.lowerThreshold);

              bw.write( "\nupperThreshold:" + data.currentProfile.upperThreshold);

              bw.write( "\nholeCount:" + data.currentProfile.holeCount);



              for (measurements measurement : data.measureList) {
                  bw.write("\n\nData for hole: " + measurement.positionname());
                  for (double result : measurement.results) {
                      bw.write("\n  " + result);
                  }
              }



            System.out.println("File written Successfully");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally
        {
            try{
                if(bw!=null)
                    bw.close();
            }catch(Exception ex){
                System.out.println("Error in closing the BufferedWriter"+ex);
            }
        }
    }

    }












    

