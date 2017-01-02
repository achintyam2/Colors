package com.android.calllogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class LogsDialogFragment extends DialogFragment {

    public LogsDialogFragment() {}

    public static LogsDialogFragment newInstance()
    {
        LogsDialogFragment fragment = new LogsDialogFragment();
        return fragment;
    }

    String[] items = {"Call","Add to contacsts","Show history","Send SMS","Remove from call log","Copy phone number","Block","Call Reminder"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.log_click_list_dialog_fragment, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.activity_listview, items);
        ListView lv = (ListView) rootView.findViewById(R.id.list_action);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"You Clicked "+items[position],Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

}
