package com.satoaki.vertretungsplan;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class addFaecher extends Fragment {

    View v;
    ViewGroup mContainerView;
    ImageButton addFach;
    AutoCompleteTextView inp_Fach;
    TextView imp_Id;
    Button cb_leistung;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_faecher, container, false);
        getActivity().setTitle("Fächer hinzufügen");
        mContainerView = (ViewGroup)v.findViewById(R.id.addFach_container);
        addFach = (ImageButton) v.findViewById(R.id.addFach_addBtn);
        imp_Id = (TextView)v.findViewById(R.id.addFach_inp_num);
        cb_leistung = (Button)v.findViewById(R.id.addFach_leistung);
        if(Integer.parseInt(S.p.get(0).KlasseId)<66){
            imp_Id.setVisibility(View.INVISIBLE);
            cb_leistung.setVisibility(View.INVISIBLE);
        }
        cb_leistung.animate().translationXBy(600).setDuration(1);
        imp_Id.animate().translationYBy(400).setDuration(1);
        inp_Fach = (AutoCompleteTextView)v.findViewById(R.id.addFach_input);
        inp_Fach.animate().translationXBy(-1000).setDuration(1);
        addFach.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toggle();
            }
        });
        keyListener onkl = new keyListener();
        inp_Fach.setOnKeyListener(onkl);
        imp_Id.setOnKeyListener(onkl);
        inp_Fach.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,S.VaidFaecherName));
        cb_leistung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLeistung();
            }
        });
        return v;
    }
    private void checkFach(){
        boolean found = false;
        for(int b=0;b<S.VaidFaecherName.length;b++)
            if ((imp_Id.getText().length()>0&&!found||Integer.parseInt(S.p.get(0).KlasseId)<66) && S.VaidFaecherName[b].toLowerCase().equals(inp_Fach.getText().toString().toLowerCase())) {
                found = true;
                View kb = getActivity().getCurrentFocus();
                if (kb != null) {
                }
                v.findViewById(R.id.addFach_empty).setVisibility(View.INVISIBLE);
                if(imp_Id.getText().length()>0)
                    inp_Fach.setText(inp_Fach.getText()+"-"+imp_Id.getText());
                if(LeistungsKurs)
                    inp_Fach.setText("(LK) "+inp_Fach.getText());
                toggle();
                addItem();
            }
        if (!found){
            Toast.makeText(getContext(),"das Fach existiert nicht!",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onKey: Invalid Fach");
        }
    }
    boolean LeistungsKurs = false;
    private void toggleLeistung(){
        if(LeistungsKurs){
            LeistungsKurs = false;
            cb_leistung.setText(" 2-Stündig");
        }else{
            LeistungsKurs = true;
            cb_leistung.setText(" 4-Stündig");
        }
    }
    boolean toggle = true;
    private void toggle() {
        if (toggle){
            toggle=false;
            addFach.setFocusable(false);
            cb_leistung.animate().translationXBy(-600).setDuration(800);
            imp_Id.setFocusableInTouchMode(true);
            imp_Id.animate().translationYBy(-400).setDuration(800);
            inp_Fach.setFocusableInTouchMode(true);
            inp_Fach.animate().translationXBy(1000).setDuration(800);
            addFach.setVisibility(View.GONE);
            addFach.animate().translationYBy(-400).setDuration(1200);
        }else{
            toggle=true;
            addFach.animate().translationYBy(400).setDuration(1200);
            addFach.setFocusableInTouchMode(true);
            cb_leistung.animate().translationXBy(600).setDuration(800);
            imp_Id.setFocusableInTouchMode(false);
            imp_Id.animate().translationYBy(400).setDuration(800);
            inp_Fach.setFocusableInTouchMode(false);
            inp_Fach.animate().translationXBy(-1000).setDuration(800);
            addFach.setVisibility(View.VISIBLE);

            imp_Id.setText("");
        }
    }

    private void addItem() {
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.addfaecher_kachel, mContainerView, false);
        ((TextView) newView.findViewById(R.id.addFach_listFach)).setText(inp_Fach.getText());

        newView.findViewById(R.id.addFach_btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContainerView.removeView(newView);
                if (mContainerView.getChildCount() == 0) {
                    v.findViewById(R.id.addFach_empty).setVisibility(View.VISIBLE);
                }
            }
        });
        mContainerView.addView(newView, 0);
        inp_Fach.setText("");
    }

    final String TAG = "main addFach";
    class keyListener implements View.OnKeyListener {
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if(KeyEvent.ACTION_DOWN==keyEvent.getAction()&&(i==KeyEvent.KEYCODE_NUMPAD_ENTER||i==KeyEvent.KEYCODE_ENTER))
                checkFach();
            return false;
        }
    }
}

