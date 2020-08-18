import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.jruby.RubyProcess;
import org.json.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class httpSubmit {



    private static String getDateString() {
        DateTime date = new DateTime(); //Make a new date obj
        StringBuilder dateString = new StringBuilder();  //Create our stringbuilder
        if (date.monthOfYear().get() < 10) {
            //If the month is less than 10, prepend a 0
            dateString.append("0" + String.valueOf(date.monthOfYear().get()));
        } else {
            //Else just add the month to stringbuilder
            dateString.append(String.valueOf(date.monthOfYear().get()));
        }
        if (date.dayOfMonth().get() < 10) {
            //If the day is less than 10, prepend a 0
            dateString.append("0" + String.valueOf(date.dayOfMonth().get()));
        } else {
            //Else just add the day to stringbuilder
            dateString.append(String.valueOf(date.dayOfMonth().get()));
        }

        dateString.append(date.year().get());    //Finally, add the year
        return dateString.toString();
    }




    public static void testSubmit(Integer partNum, Integer techID) throws IOException, JSONException {


        Image img1 = null;
        CloseableHttpClient client = HttpClients.createDefault();
//        HttpGet httpget = new HttpGet("http://192.168.10.111:8000/APITest");
//        httpget.
//        System.out.print(EntityUtils.toString(response.getEntity()));
        HttpPost submitPost = new HttpPost(gui.currentSettings.serverURL);
        submitPost.setHeader("Content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        for (int i = 0; i < gui.storage.size(); i++) {
            if (gui.storage.get(i).partNum == partNum) {
                for (int b = 0; b < gui.storage.get(i).measureList.size(); b++) {
                    if (gui.storage.get(i).measureList.get(b).position == measurements.TopLeft) {
                        img1 = gui.storage.get(i).measureList.get(b).capture;
                    }
                }
            }
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) img1, "jpg", os);



//        ArrayList<NameValuePair> formparams = new ArrayList<NameValuePair>();
//        formparams.add(new BasicNameValuePair("partNum", String.valueOf(partNum)));
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody("img1", os.toByteArray(), ContentType.DEFAULT_BINARY, "img1.jpg")
                .addTextBody("partNum", String.valueOf(partNum))
                .addTextBody("date", getDateString())
                .addTextBody("tech", String.valueOf(techID))
                .setBoundary("----WebKitFormBoundary7MA4YWxkTrZu0gW").build();

//        System.out.println(EntityUtils.toString(entity));


        submitPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(submitPost);
        } catch (org.apache.http.conn.HttpHostConnectException ex) {
            JOptionPane.showMessageDialog(null, "Unable to connect to server");
            System.out.println(ex.getMessage());

        }
        gui.sysConsole.println(response.toString());
        String responseEntity = EntityUtils.toString(response.getEntity());
        gui.sysConsole.println(responseEntity);
        JSONObject obj = new JSONObject(responseEntity);
        int responseCode = obj.getInt("ResponseCode");
        if (responseCode == 1) {
            JOptionPane.showMessageDialog(null, "Submission Successful");
        } else if (responseCode == 0) {
            JOptionPane.showMessageDialog(null, obj.getString("error"));
        } else {
            JOptionPane.showMessageDialog(null, "Response Code: " + String.valueOf(responseCode) + "\n" + obj.getString("error"));

        }


    }






}
