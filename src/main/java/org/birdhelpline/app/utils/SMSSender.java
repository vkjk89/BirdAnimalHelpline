package org.birdhelpline.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SMSSender {
    // Replace with your username
    static String username = "vkj";

    // Replace with your API KEY (We have sent API KEY on activation email, also available on panel)
    static String apikey = "PsNxYiJte7DmXOPVE3FM";

    // Replace with the destination mobile Number to which you want to send sms
    static String mobile = "9029787026";

    // Replace if you have your own Sender ID, else donot change
    static String senderid = "MYTEXT";

    // Replace with your Message content
    static String message = "Test sms API";

    // For Plain Text, use "txt" ; for Unicode symbols or regional Languages like hindi/tamil/kannada use "uni"
    static String type = "txt";

    //Prepare Url
    static URLConnection myURLConnection = null;
    static URL myURL = null;
    static BufferedReader reader = null;

    //encoding message
    static String encoded_message = URLEncoder.encode(message);

    //Send SMS API
    static String mainUrl = "http://smshorizon.co.in/api/sendsms.php?";

    public static String sendSms() {
        //Prepare parameter string
        StringBuilder sbPostData = new StringBuilder(mainUrl);
        sbPostData.append("user=" + username);
        sbPostData.append("&apikey=" + apikey);
        sbPostData.append("&message=" + encoded_message);
        sbPostData.append("&mobile=" + mobile);
        sbPostData.append("&senderid=" + senderid);
        sbPostData.append("&type=" + type);

//final string
        mainUrl = sbPostData.toString();
        try {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            //reading response
            String response;
            while ((response = reader.readLine()) != null)
                //print response
                System.out.println(response);

            //finally close connection
            reader.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
