package com.satoaki.vertretungsplan;

import android.os.StrictMode;
import android.util.Log;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by satoa on 17.01.2017.
 */

public class HtmlAuslesen {
    String Klasse;
    ArrayList<String> Quellcode;

    public HtmlAuslesen(String Klasse){
        Quellcode = new ArrayList<>();
        this.Klasse = Klasse;
        getQuellcode();
    }
    void getQuellcode(){
        Scanner scanner;
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            scanner = new Scanner(new URL(createUrl(getKalenderWoche())).openStream());
            while (scanner.hasNextLine()) {
                Log.i(TAG, "getQuellcode: "+scanner.nextLine());
                //Quellcode.add(scanner.nextLine());
            }
            scanner.close();
        } catch (Exception e) {
            Log.i(TAG, "getQuellcode: Error: Kein Internet: "+createUrl(getKalenderWoche()));
            e.printStackTrace();
        }
    }
    private String createUrl(String kalWoche){
        return "http://www.fsg-marbach.de/fileadmin/bilder/unterricht/vertretungsplanung/Klassen/"+kalWoche+"/w/w000"+S.getKlassenId(Klasse)+".htm";
    }

    private String getKalenderWoche() {
        Calendar cal = Calendar.getInstance();
        int ICalW = cal.get(Calendar.WEEK_OF_YEAR);
        String SCalW = String.valueOf(ICalW);
        if (ICalW<10)
            SCalW = "0"+SCalW;
        return SCalW;
    }
    final String TAG = "main HtmlAuslesen";
}
