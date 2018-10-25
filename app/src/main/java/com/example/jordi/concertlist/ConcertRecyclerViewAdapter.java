package com.example.jordi.concertlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ConcertRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "NoteRecyclerViewAdapter";

    private ArrayList<Concert> mConcerts = new ArrayList<>();
    private IMainActivity mIMainActivity;
    private Context mContext;
    private int mSelectedConcertIndex;

    public ConcertRecyclerViewAdapter(Context context, ArrayList<Concert> concerts) {
        mConcerts = concerts;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_concert_recycler_view_adapter, parent, false);

        holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder){
            ((ViewHolder)holder).grup.setText(mConcerts.get(position).getGrup());
            ((ViewHolder)holder).poblacio.setText(mConcerts.get(position).getPoblacio());
            if(mConcerts.get(position).getPreu() != null){
                ((ViewHolder)holder).preu.setText(Double.toString(mConcerts.get(position).getPreu()));
            }else{
                ((ViewHolder)holder).preu.setText("0");
            }

            ((ViewHolder)holder).lloc.setText(mConcerts.get(position).getLloc());
            Date d = mConcerts.get(position).getData();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            ((ViewHolder)holder).data.setText(format.format(d));
            //SimpleDateFormat spf = new SimpleDateFormat("MMM dd, yyyy");
            //String date = spf.format(mConcerts.get(position).getTimestamp());
            //((ViewHolder)holder).timestamp.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        return mConcerts.size();
    }


    public void updateConcert(Concert concert){
        mConcerts.get(mSelectedConcertIndex).setGrup(concert.getGrup());
        mConcerts.get(mSelectedConcertIndex).setPoblacio(concert.getPoblacio());
        notifyDataSetChanged();
    }

    public void removeConcert(Concert concert){
        mConcerts.remove(concert);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mIMainActivity = (IMainActivity) mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView grup, data, poblacio, preu, lloc;

        public ViewHolder(View itemView) {
            super(itemView);
            grup = itemView.findViewById(R.id.grup);
            data = itemView.findViewById(R.id.data);
            poblacio = itemView.findViewById(R.id.poblacio);
            preu = itemView.findViewById(R.id.preu);
            lloc = itemView.findViewById(R.id.lloc);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mSelectedConcertIndex = getAdapterPosition();
            mIMainActivity.onConcertSelected(mConcerts.get(mSelectedConcertIndex));
        }
    }
}

