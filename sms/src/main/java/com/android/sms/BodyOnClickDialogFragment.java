package com.android.sms;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BodyOnClickDialogFragment extends DialogFragment {

    MessageOptionsDialogClickHandler call;
    TextView forward,copy,delete;
    String bodyText;

    public BodyOnClickDialogFragment(){}

    public interface MessageOptionsDialogClickHandler {         //Creating a interface to communicate between the fragments
        void onForwardClcked();
        void onCopyClicked();
        void onDeleteClicked();
    }
    public void setListener(MessageOptionsDialogClickHandler listener) {
        call = listener;
    }

    public BodyOnClickDialogFragment newInstance() {
        BodyOnClickDialogFragment fragment = new BodyOnClickDialogFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.body_on_click_dialog, container, false);

        getDialog().setTitle("Message Options");

        forward = (TextView) rootView.findViewById(R.id.forward);
        copy = (TextView) rootView.findViewById(R.id.copy);
        delete = (TextView) rootView.findViewById(R.id.delete);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call!=null)
                call.onForwardClcked();
                dismiss();
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call!=null)
                call.onCopyClicked();
                dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call!=null)
                call.onDeleteClicked();
                dismiss();
            }
        });
        return rootView;
    }

}
