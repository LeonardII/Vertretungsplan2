package com.satoaki.vertretungsplan;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by satoa on 15.01.2017.
 */

public class Person {
    String Name;
    String Klasse;
    String KlasseId;
    ArrayList<String> Faecher;
    ArrayList<Event> event;

    public Person(String name, String klasse) {
        Name = name;
        Klasse = klasse;
        event = new ArrayList<>();
        KlasseId = S.getKlassenId(Klasse);
        Log.i(TAG, "Person: " + Name + " " + Klasse + " " + KlasseId + " erfolgreich erstellt");
        Faecher = new ArrayList<>();
    }

    void setFaecher(String faecher) {

    }

    void addFach(String Fach) {
        Faecher.add(Fach);
        Log.i(TAG, "addFach: Sucessfully added: " + Fach);
    }

    void UpdateEvents() {
        if (Faecher.size() > 0)
            new Thread(new com.satoaki.vertretungsplan.BackGroundProcess(createUrl(),this)).start();
    }

    private String createUrl() {
        return "http://www.fsg-marbach.de/fileadmin/bilder/unterricht/vertretungsplanung/Klassen/" + getKalenderWoche() + "/w/w000" + S.getKlassenId(Klasse) + ".htm";
    }

    private String getKalenderWoche() {
        Calendar cal = Calendar.getInstance();
        int ICalW = cal.get(Calendar.WEEK_OF_YEAR);
        String SCalW = String.valueOf(ICalW);
        if (ICalW < 10)
            SCalW = "0" + SCalW;
        return SCalW;
    }

    static final String TAG = "main Person";
}

class BackGroundProcess implements Runnable {
    String url;
    Person person;

    public BackGroundProcess(String url, Person p) {
        super();
        person = p;
        this.url = url;
    }

    public void run() {
        Scanner scanner;
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            scanner = new Scanner(new URL(url).openStream());
            while (scanner.hasNextLine()) {
                String actLine = scanner.nextLine();
                Log.i("XXX", "Quellcode: " + actLine);
                for (String p : person.Faecher)
                    if (actLine.contains(p))
                        person.event.add(getEvent(actLine));
                if (actLine.contains("Montag"))
                    person.event.add(getEventTag(actLine));
            }
            scanner.close();
            S.hasInternet = true;
        } catch (Exception e) {
            Log.i(TAG, "getQuellcode: Error: Kein Internet:");
            e.printStackTrace();
            S.hasInternet = false;
        }
    }

    private Event getEvent(String actLine) {
        Event event = new Event();
        Log.i(TAG, "main" + actLine);
        String[] Alles = actLine.split("</td><td class=\"list\" align=\"center\"");
        for(int q = 0;q<Alles.length;q++){
           while(Alles[q].charAt(0)!='>')
                Alles[q] = Alles[q].substring(1);
            Alles[q] = Alles[q].substring(1);
            Log.i(TAG, "getEvent: "+Alles[q]);
        }
        event.setStunden(Alles[1]);
        event.setVertreter(Alles[2]);
        event.setRaum(Alles[3]);
        event.setFach(Alles[4]);
        event.setArt(Alles[5]);
        Log.i(TAG, "getEvent: ");
        return event;
    }
    private Event getEventTag(String actLine) {
        Event event = new Event();
        StringBuilder Tag = new StringBuilder();
        ///Log.i(TAG, "YYY" + actLine);
        String[] Datum = actLine.split("<b>");
        int ind = 0;
        while(Datum[1].charAt(ind)!='<'){
            Tag.append(Datum[1].charAt(ind));
            ind++;
        }
        event.setTag(Tag.toString());
        return event;
    }

    static final String TAG = "main Event";
}
