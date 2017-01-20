package com.satoaki.vertretungsplan;

import android.util.Log;

/**
 * Created by satoa on 17.01.2017.
 */

public class Event {
    String Stunden = "", Vertreter = "", Raum = "", Fach = "", Art = "", Tag = "",FachAusgeschrieben="",VertVon="",VertText="",NDT="";

    public Event(){}
    public Event(String art){Art = art;}
    public Event(String Event, int g){
     String[] All = Event.split("<!>");
        setStunden(All[0].substring(1));
        setVertreter(All[1].substring(1));
        setRaum(All[2].substring(1));
        setFach(All[3].substring(1));
        setArt(All[4].substring(1));
        setTag(All[5].substring(1));
        setFachAusgeschrieben(All[6].substring(1));
        setVertVon(All[7].substring(1));
        setVertText(All[8].substring(1));
        }

    public void setArt(String art) {
        Art = art;
        Log.i(TAG, "setArt: "+art);
    }

    public void setFach(String fach) {
        Fach = fach;
        String[] kz = Fach.split("-");
        for(int i = 0;i<S.ValidFaecherKuerzel.length;i++){
            if(S.ValidFaecherKuerzel[i].toLowerCase().equals(kz[0].toLowerCase()))
                FachAusgeschrieben = S.VaidFaecherName[i];
        }
        Log.i(TAG, "setFach: "+fach);
    }

    public void setRaum(String raum) {
        Raum = raum;
        Log.i(TAG, "setRaum: "+raum);
    }

    public void setStunden(String stunden) {
        Stunden = stunden;
        Log.i(TAG, "setStunden: "+stunden);
    }

    public void setVertreter(String vertreter) {
        Vertreter = vertreter;
        Log.i(TAG, "setVertreter: "+vertreter);
    }

    public void setTag(String tag) {
        Tag = tag;
        Log.i(TAG, "setTag: "+tag);
    }
    public void setNDT(String Nachicht){
        NDT += Nachicht+"\n";
        Log.i(TAG, "setNDT: "+Tag+" "+Nachicht);
    }
    public String getNDT(){
        if(NDT=="")
            NDT = "Keine Nachichten zum Tag";
        return NDT.trim();
    }

    public void setVertVon(String vertVon) {
        VertVon = vertVon;
        Log.i(TAG, "setVertVon: "+vertVon);
    }

    public void setVertText(String vertText) {
        VertText = vertText;
        Log.i(TAG, "setVertText: "+vertText);
    }

    public void setFachAusgeschrieben(String fachAusgeschrieben) {
        FachAusgeschrieben = fachAusgeschrieben;
        Log.i(TAG, "setFachAusgeschrieben: "+fachAusgeschrieben);
    }

    final String TAG = "main Event ";
}
