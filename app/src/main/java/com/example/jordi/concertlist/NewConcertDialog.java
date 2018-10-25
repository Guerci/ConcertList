package com.example.jordi.concertlist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class NewConcertDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "NewConcertDialog";

    //widgets
    private EditText mGrup, mPoblacio,mLloc,mPreu ;

    private Button bData, bHora;
    private TextView mCreate, mCancel, mDate, mHora;

    private int day, month, year, hour, minutes;

    //vars
    private IMainActivity mIMainActivity;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_concert_dialog, container, false);

        mGrup = view.findViewById(R.id.concert_grup);
        mPoblacio = view.findViewById(R.id.concert_poblacio);
        mLloc = view.findViewById(R.id.concert_lloc);
        mPreu = view.findViewById(R.id.concert_preu);
        mDate = view.findViewById(R.id.textData);
        mHora = view.findViewById(R.id.textHora);
        bData = view.findViewById(R.id.buttonData);
        bHora = view.findViewById(R.id.buttonHora);

        mCreate = view.findViewById(R.id.create);
        mCancel = view.findViewById(R.id.cancel);

        bData.setOnClickListener(this);
        bHora.setOnClickListener(this);

        mCancel.setOnClickListener(this);
        mCreate.setOnClickListener(this);

        getDialog().setTitle("New Concert");

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme_Holo_Light_Dialog;
        setStyle(style, theme);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.create:{

                // insert the new note

                String grup = mGrup.getText().toString();
                String poblacio = mPoblacio.getText().toString();
                String lloc = mLloc.getText().toString();
                Double preu = Double.parseDouble(mPreu.getText().toString());
                Long dateMiliseconds = System.currentTimeMillis();
                Calendar c = Calendar.getInstance();
                c.set(year,month,day,hour,minutes);
                Date d = new Date(c.getTimeInMillis());


                if(!grup.equals("")){

                    mIMainActivity.createNewConcert(grup, lloc, d, poblacio, preu);
                    getDialog().dismiss();
                }
                else{
                    Toast.makeText(getActivity(), "Enter a Grup", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.cancel:{
                getDialog().dismiss();
                break;
            }

            case R.id.buttonData:{
               final Calendar c = Calendar.getInstance();
               day = c.get(Calendar.DAY_OF_MONTH);
               month = c.get(Calendar.MONTH);
               year = c.get(Calendar.YEAR);

               datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mDate.setText(dayOfMonth+"/"+month+"/"+year);

                    }
                },year,month,day);
                datePickerDialog.show();
                break;
            }
            case R.id.buttonHora:{
                final Calendar c=Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minutes = c.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHora.setText(hourOfDay+":"+minute);
                    }
                },hour,minutes,false);
                timePickerDialog.show();
                break;
            }
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIMainActivity = (IMainActivity)getActivity();
    }
}
