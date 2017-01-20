package com.satoaki.vertretungsplan;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Vertretungen extends Fragment {

    ArrayList<View> VEvents;
    View v;
    TextView tv;
    LinearLayout Event_container;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_vertretungen, container, false);
        setHasOptionsMenu(true);
        Event_container = (LinearLayout) v.findViewById(R.id.Vertretungen_EventContainer);
        getActivity().setTitle("Vertretungen");

        S.p.get(0).u2date = false;
        S.p.get(0).UpdateEvents();
        new Thread() {
            @Override
            public void run() {
                while (!S.p.get(0).u2date) {}
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long t = System.currentTimeMillis();
                        if (!S.hasInternet) {
                            S.p.get(0).event.clear();
                            Event e = new Event();
                            e.setTag("Kein Internet vorhanden!");
                            e.setNDT("Error:(#012322), B[d INt4rn3t,hack3d, 0987tyweuify8qhi3hef8329yrhifea;qknascqbjfealdn;'r4ojt;-gpo[pg5kjre");
                            addItem(e);
                        } else {
                            new Thread() {
                                public void run() {
                                    for (S.cou = 0; S.cou < S.p.get(0).event.size(); S.cou++) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                addItem(S.p.get(0).event.get(S.cou));
                                            }
                                        });
                                        long y = System.currentTimeMillis();
                                        while(y+80>System.currentTimeMillis()){}
                                    }
                                }
                            }.start();
                        }
                        ((TextView)v.findViewById(R.id.Vertretungen_lade)).animate().translationYBy(-1000).setDuration(600);
                        ((ProgressBar)v.findViewById(R.id.Vertretungen_progressbar)).animate().translationYBy(-1000).setDuration(600);
                        ((TextView)v.findViewById(R.id.Vertretungen_lade)).setVisibility(View.GONE);
                        ((ProgressBar)v.findViewById(R.id.Vertretungen_progressbar)).setVisibility(View.GONE);
                    }});}}.start();
        return v;
    }



    boolean keineVertretung = false;
    private void addItem(Event e) {
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.vertretungen_kachel, Event_container, false);
        final ViewGroup container = (ViewGroup) newView.findViewById(R.id.verkach_container);
        TextView Raum = (TextView) newView.findViewById(R.id.verkach_Raum);
        TextView Lehrer = (TextView) newView.findViewById(R.id.verkach_Lehrer);
        final TextView Tag = ((TextView) newView.findViewById(R.id.verkach_Tag));
        final TextView Text = ((TextView) newView.findViewById(R.id.verkach_Text));
        final TextView Moreinfo = ((TextView) newView.findViewById(R.id.verkach_moreInfo));
        final TextView NZT = (TextView)newView.findViewById(R.id.verkach_NDT);

        NZT.setText(e.getNDT());
        Moreinfo.setText(e.Fach+" in der "+e.Stunden+". Stunde");
        ((TextView) newView.findViewById(R.id.verkach_Art)).setText(e.Art);
        TextView Fach =((TextView) newView.findViewById(R.id.verkach_Fach));
        Fach.setText(e.FachAusgeschrieben);
        Text.setText(e.VertText);
        Tag.setText(e.Tag);
        Lehrer.setText(e.Vertreter);
        Raum.setText(e.Raum);

        if(e.Tag!=""){
            Tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(NZT.getVisibility()==View.GONE)
                        NZT.setVisibility(View.VISIBLE);
                    else
                        NZT.setVisibility(View.GONE);
                }
            });
            if(keineVertretung) {
                addItem(new Event("Keine Vertretungen"));
            }
            keineVertretung = true;
            String htmlString=e.Tag;
            Tag.setText(Html.fromHtml(htmlString));
            container.setVisibility(View.GONE);
        }else {
            keineVertretung = false;
            Tag.setVisibility(View.INVISIBLE);
        }
        if(e.Art.equals("Keine Vertretungen")){
            Raum.setVisibility(View.GONE);
            Fach.setVisibility(View.GONE);
            container.setBackgroundColor(Color.LTGRAY);
        }else
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("main Vertretungen", "expand...");
                    if(Moreinfo.getVisibility()==View.GONE) {
                        Moreinfo.setVisibility(View.VISIBLE);
                        if(!Text.getText().equals("&nbsp;"))
                            Text.setVisibility(View.VISIBLE);
                    }else{
                        Moreinfo.setVisibility(View.GONE);
                        Text.setVisibility(View.GONE);
                    }
                }
            });
        if(e.Art.equals("Vertretung"))
            container.setBackgroundColor(Color.CYAN);
        if(e.Art.contains("Unterricht"))
            container.setBackgroundColor(Color.RED);
        if(e.Art.contains("ohne Lehrer"))
            container.setBackgroundColor(Color.GREEN);
        if(e.Art.equals("Raum"))
            container.setBackgroundColor(Color.YELLOW);

        Event_container.addView(newView);
        ((ViewGroup)newView.findViewById(R.id.verkach_All)).animate().translationY(0).setDuration(350).setInterpolator(new DecelerateInterpolator());

    }
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem btnRefresh = menu.findItem(R.id.actionBar_addFach);
        btnRefresh.setVisible(true);
        btnRefresh.setIcon(R.drawable.ic_refresh);
        btnRefresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.content_main,new Vertretungen()).commit();
             return false;
            }
        });
    }

}
