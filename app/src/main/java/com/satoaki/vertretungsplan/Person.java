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
    boolean u2date = false;

    public Person(String name, String klasse) {
        Name = name;
        Klasse = klasse;
        event = new ArrayList<>();
        KlasseId = S.getKlassenId(Klasse);
        Log.i(TAG, "Person: " + Name + " " + Klasse + " " + KlasseId + " erfolgreich erstellt");
        Faecher = new ArrayList<>();
    }

    void setFaecher(String faecher) {
        String[]fach = faecher.split("<!>");
        for (int i=0;i<fach.length;i++)
            addFach(fach[i]);
    }

    void addFach(String Fach) {
        Faecher.add(Fach);
        Log.i(TAG, "addFach: Sucessfully added: " + Fach);
    }

    void UpdateEvents() {
        if (Faecher.size() > 0)
            new Thread(new com.satoaki.vertretungsplan.BackGroundProcess(createUrl(0),createUrl(1), this)).start();
    }

    private String createUrl(int woche) {
        return "http://www.fsg-marbach.de/fileadmin/bilder/unterricht/vertretungsplanung/Klassen/" + getKalenderWoche(woche) + "/w/w000" + S.getKlassenId(Klasse) + ".htm";
    }

    private String getKalenderWoche(int woche) {
        Calendar cal = Calendar.getInstance();
        int ICalW = cal.get(Calendar.WEEK_OF_YEAR)+woche;
        String SCalW = String.valueOf(ICalW);
        if (ICalW < 10)
            SCalW = "0" + SCalW;
        return SCalW;
    }

    static final String TAG = "main Person";
}

class BackGroundProcess implements Runnable {
    String url;
    String url2;
    Person person;

    public BackGroundProcess(String url,String url2, Person p) {
        super();
        person = p;
        this.url = url;
        this.url2 = url2;
    }

    public void run() {
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            Algorythm(new Scanner(new URL(url).openStream()));
            Algorythm(new Scanner(new URL(url2).openStream()));
            S.hasInternet = true;
        } catch (Exception e) {
            Log.i(TAG, "getQuellcode: Error: Kein Internet:");
            e.printStackTrace();
            S.hasInternet = false;
        }
        person.u2date = true;
    }
    private void Algorythm(Scanner scanner){
        while (scanner.hasNextLine()) {
            String actLine = scanner.nextLine();
            Log.i("XXX", "Quellcode: " + actLine);
            for (String p : person.Faecher)
                if (actLine.contains(p)) {
                    Event e = getEvent(actLine);
                    if (!e.Stunden.equals("Failed!"))
                        person.event.add(e);
                }
            if(actLine.contains("<tr><td colspan="))
                addNDT(actLine);
            if(actLine.contains("Vertretungen sind nicht freigegeben"))
                person.event.remove(person.event.size()-1);
            if (actLine.contains("Montag"))
                 person.event.add(getEventTag(actLine));
        }
        scanner.close();
        Log.i(TAG, "run: hasfinished");
    }

    private void addNDT(String actLine) {
        int c = 20;
        StringBuilder sb = new StringBuilder();
        while(actLine.charAt(c)!='<') {
            sb.append(actLine.charAt(c));
            c++;
        }
        person.event.get(person.event.size()-1).setNDT(sb.toString());
    }

    private Event getEvent(String actLine) {
        Event event = new Event();
        String[] Alles = actLine.split("</td><td class=\"list\" align=\"center\"");
        for (int q = 0; q < Alles.length; q++) {
            while (Alles[q].charAt(0) != '>')
                Alles[q] = Alles[q].substring(1);
            Alles[q] = Alles[q].substring(1);
            Log.i(TAG, "getEvent: " + Alles[q]);
        }
        try {
            event.setStunden(Alles[1]);
            event.setVertreter(Alles[2]);
            event.setRaum(Alles[3]);
            event.setFach(Alles[4]);
            event.setArt(Alles[5]);
            event.setVertVon(Alles[6]);
            int i = 0;
            StringBuilder sb = new StringBuilder();
            while (Alles[7].charAt(i) != '<') {
                sb.append(Alles[7].charAt(i));
                i++;
            }
            event.setVertText(sb.toString());
        } catch (Exception e) {
            Log.i(TAG, "getEvent: Warning cammot create event: " + actLine);
            event.setStunden("Failed!");
        }
        return event;
    }

    private Event getEventTag(String actLine) {
        Event event = new Event();
        StringBuilder Tag = new StringBuilder();
        String[] Datum = actLine.split("<b>");
        int ind = 0;
        while (Datum[1].charAt(ind) != '<') {
            Tag.append(Datum[1].charAt(ind));
            ind++;
        }
        event.setTag(Tag.toString());
        return event;
    }

    static final String TAG = "main Event";
}
