package com.example.jordi.concertlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewConcertDialog extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "ViewConcertDialog";

    //widgets
    private EditText mGrup, mPoblacio, mLloc, mPreu, mData;
    private TextView mSave, mDelete;

    //vars
    private IMainActivity mIMainActivity;
    private Concert mConcert;

    public static ViewConcertDialog newInstance(Concert concert) {
        ViewConcertDialog dialog = new ViewConcertDialog();

        Bundle args = new Bundle();
        args.putParcelable("concerts", concert);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme_Holo_Light_Dialog;
        setStyle(style, theme);

        mConcert = getArguments().getParcelable("concerts");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_view_concert_dialog, container, false);
        mGrup = view.findViewById(R.id.concert_grup);
        mPoblacio = view.findViewById(R.id.concert_poblacio);
        mLloc = view.findViewById(R.id.concert_lloc);
        mPreu = view.findViewById(R.id.concert_preu);
        mData = view.findViewById(R.id.concert_data);



        mSave = view.findViewById(R.id.save);
        mDelete = view.findViewById(R.id.delete);

        mSave.setOnClickListener(this);
        mDelete.setOnClickListener(this);

        getDialog().setTitle("Concert");

        setInitialProperties();

        return view;
    }
    private void setInitialProperties(){
        mGrup.setText(mConcert.getGrup());
        mPoblacio.setText(mConcert.getPoblacio());
        mLloc.setText(mConcert.getLloc());
        if(mConcert.getPreu() != null){
            mPreu.setText(Double.toString(mConcert.getPreu()));
        }else{
            mPreu.setText("0");
        }

        Date d = mConcert.getData();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        mData.setText(format.format(d));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.save:{

                String grup = mGrup.getText().toString();
                String poblacio = mPoblacio.getText().toString();

                if(!grup.equals("")){

                    mConcert.setGrup(grup);
                    mConcert.setPoblacio(poblacio);

                    mIMainActivity.updateConcert(mConcert);
                    getDialog().dismiss();
                }
                else{
                    Toast.makeText(getActivity(), "Enter a title", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.delete:{
                mIMainActivity.deleteConcert(mConcert);
                getDialog().dismiss();
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
