package com.satoaki.vertretungsplan;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by satoa on 14.01.2017.
 */
//TODO: if Kursstufe Chi ->Chin
public class S {

    static ArrayList<Person> p;
    static boolean hasInternet = false;
    static File Ordner;
    static File assets;
    static Context context;
    static int cou = 0;


    static boolean init(Context c){
        context = c;
        p = new ArrayList<>();
        Ordner =  new File(Environment.getExternalStorageDirectory(),"Vertretungsplan");
        assets = new File(Ordner,"assets.txt");
        setValidKlassen();
        Log.i(TAG, "Initializing Done");
        if(!Ordner.exists()) {
            try {
                Ordner.mkdir();
                Log.i(TAG, "init: Made Direction");
            }catch (Exception e){
                Toast.makeText(context,"Die App benötigt die Berechtigung Dateien zu schreiben",Toast.LENGTH_LONG).show();
                System.exit(0);
            }
            return true;
        }else{
            Update();
            return false;
        }
    }
    static void Update(){
        if (p.size()>0) {
            for (Person pp : p)
                pp.Faecher.clear();
            p.clear();
        }
        try{
            BufferedReader bf = new BufferedReader(new FileReader(assets));
            EinstellWerte = bf.readLine();
            p.add(new Person(bf.readLine(), bf.readLine()));
            p.get(0).setFaecher(bf.readLine());
            String allEvents = bf.readLine();
            Log.i(TAG, "Update: "+allEvents);
            String[] getEvent = allEvents.split("<?>");
            for(int i=0;i<getEvent.length;i++)
                p.get(0).event.add(new Event(getEvent[i],0));
            Log.i(TAG, "Update Complete");
        }catch (Exception e){
            //Toast.makeText(context,"Dateinen Korrupt!",Toast.LENGTH_LONG).show();
            //Log.i(TAG, "ERROR: Something went wrong reading File");
            e.printStackTrace();
            System.exit(0);
        }

    }
    static void Speichern(){
        try {
            OutputStream os = new FileOutputStream(assets);
            os.write(EinstellWerte.getBytes());
            os.write("\n".getBytes());
            os.write(S.p.get(0).Name.getBytes());
            os.write("\n".getBytes());
            os.write(S.p.get(0).Klasse.getBytes());
            os.write("\n".getBytes());
            for(String f: S.p.get(0).Faecher) {
                os.write(f.getBytes());
                os.write("<!>".getBytes());
            }
            os.write("\n".getBytes());
            for (Event f:S.p.get(0).event)
                os.write((f.MergeEvent()+"<?>").getBytes());
            os.close();
            Log.i(TAG, "Speichern Done");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    static String getKlassenId(String klasse){
        String klassenId = "";
        for (int i=0;i<ValidKlassen.length;i++)
            if(ValidKlassen[i].equals(klasse)) {
                if (i<10)
                    klassenId = "0";
                klassenId += String.valueOf(i);
            }
        if(klassenId=="")
            Log.i(TAG, "ERROR: Invalid Klass: "+klassenId);
        else
            Log.i(TAG, "Successfully Generated KlassenId: "+klassenId);
        return klassenId;
    }

    private static void setValidKlassen(){
        char[] kuerzel = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'k', 'l', 'm' };
        int[] anz = { 11, 10, 11, 11, 12, 10 };
        int k = 1;
        ValidKlassen[0]="";
        for (int i = 5; i < 11; i++) {
            for (int j = 0; j < anz[i - 5]; j++) {
                ValidKlassen[k] = (String.valueOf(i) + String.valueOf(kuerzel[j]));
                k++;
            }
        }
        ValidKlassen[k]="Ks-1";
        ValidKlassen[k+1]="Ks-2";
    }
    static String EinstellWerte = "001";
    static String[] VaidFaecherName = {"Spanisch","Italienisch","Evangelische Religion","Katholische Religion","Religion(Leistungskurs)","Deutsch","Englisch","Französisch","Latein","Chinesisch","Russisch",
            "Mathe","Ethik", "Informatik","Physik","Chemie","Biologie","Natur Phänomene","Naturwissenschaften und Technik",
            "Bildene Kunst","Gemeinschaftskunde","Geschichte","Erdkunde","Medien","Sport(Gemischt)","Sport(Weiblich)","Sport(Männlich)","Klassenlehrerin",
            "Literatur","Astronomie","Wirtschaft","Darstellende Geometrie","Psychologie","Seminarkurs","Kunstprofil intermedialer Kommunikation","Musik"};
    static String[] ValidFaecherKuerzel = {"Spa","Ita","evR","kR","Rel","D","E","F","L","Chi","rus",
            "M","Eth","inf","Ph","Ch","Bio","NP","NwT","Bk","Gk","G","Ek","Medien","SPO","SW","SM","KL",
            "lit","ast","wir","dg","psy","sem","KimKo","Mu"};
    static String[] ValidKlassen = new String[68];
    static final int unbearbeitetLassen = 3;
    static final String TAG = "main Static";
}

