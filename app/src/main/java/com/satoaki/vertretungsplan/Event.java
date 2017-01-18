package com.satoaki.vertretungsplan;

import android.util.Log;

/**
 * Created by satoa on 17.01.2017.
 */

public class Event {
    String Stunden = "", Vertreter = "", Raum = "", Fach = "", Art = "", Tag = "",FachAusgeschrieben="",VertVon="",VertText="";

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

    public void setVertVon(String vertVon) {
        VertVon = vertVon;
    }

    public void setVertText(String vertText) {
        VertText = vertText;
    }

    public void setFachAusgeschrieben(String fachAusgeschrieben) {
        FachAusgeschrieben = fachAusgeschrieben;
    }

    final String TAG = "main Event ";
}
