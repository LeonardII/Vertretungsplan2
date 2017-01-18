package com.satoaki.vertretungsplan;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Vertretungen extends Fragment {

    View v;
    TextView tv;
    ViewGroup Event_container;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_vertretungen, container, false);
        Event_container = (ViewGroup) v.findViewById(R.id.Vertretungen_EventContainer);
        getActivity().setTitle("Vertretungen");

        S.p.get(0).u2date = false;
        S.p.get(0).UpdateEvents();
        new Thread() {
            @Override
            public void run() {
                while (!S.p.get(0).u2date) {
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!S.hasInternet) {
                            Event e = new Event();
                            e.setFachAusgeschrieben("Kein Internet vorhanden!");
                            e.setVertText("Error:(#012322), B[d INt4rn3t,hack3d, 0987tyweuify8qhi3hef8329yrhifea;qknascqbjfealdn;'r4ojt;-gpo[pg5kjre");
                            addItem(e);
                        } else {
                            for (int e = S.p.get(0).event.size()-1;e>=0;e--)
                            addItem(S.p.get(0).event.get(e));
                        }
                    }
                });
            }
        }.start();
        return v;
    }
    boolean keineVertretung = false;
    private void addItem(Event e) {
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.vertretungen_kachel, Event_container, false);
        TextView Raum = (TextView) newView.findViewById(R.id.verkach_Raum);
        TextView Lehrer = (TextView) newView.findViewById(R.id.verkach_Lehrer);
        ImageButton btnExpand = (ImageButton) newView.findViewById(R.id.verkach_expand);
        final TextView Tag = ((TextView) newView.findViewById(R.id.verkach_Tag));
        final TextView Text = ((TextView) newView.findViewById(R.id.verkach_Text));
        final TextView Moreinfo = ((TextView) newView.findViewById(R.id.verkach_moreInfo));
        Moreinfo.setText(e.Fach+" in der "+e.Stunden+". Stunde");
        ((TextView) newView.findViewById(R.id.verkach_Art)).setText(e.Art);
        ((TextView) newView.findViewById(R.id.verkach_Fach)).setText(e.FachAusgeschrieben);
        Text.setText(e.VertText);
        Tag.setText(e.Tag);
        Lehrer.setText(e.Vertreter);
        Raum.setText(e.Raum);

        btnExpand.setOnClickListener(new View.OnClickListener() {
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
        if(e.Art.equals("Keine Vertretungen"))
            btnExpand.setVisibility(View.GONE);
        if(e.Tag!=""){
            if(keineVertretung){
                Event b = new Event();
                b.setArt("Keine Vertretungen");
                addItem(b);
            }
            keineVertretung = true;
            String htmlString="<u>"+e.Tag+"</u>";
            Tag.setText(Html.fromHtml(htmlString));
            Lehrer.setVisibility(View.GONE);
            Raum.setVisibility(View.GONE);
            btnExpand.setVisibility(View.GONE);
        }else
            keineVertretung = false;

        Event_container.addView(newView, 0);

    }
}
