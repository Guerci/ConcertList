package com.example.jordi.concertlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by User on 5/14/2018.
 */

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        IMainActivity,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;


    //widgets
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //vars
    private View mParentLayout;
    private ArrayList<Concert> mConcert = new ArrayList<>();
    private ConcertRecyclerViewAdapter mConcertRecyclerViewAdapter;
    private DocumentSnapshot mLastQueriedDocument;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFab = findViewById(R.id.fab);
        mParentLayout = findViewById(android.R.id.content);
        mRecyclerView = findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        mFab.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        setupFirebaseAuth();
        initRecyclerView();
        getConcerts();
    }

    @Override
    public void deleteConcert(final Concert concert) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference concertRef = db
                .collection("concerts")
                .document(concert.getConcert_id());

        concertRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    makeSnackBarMessage("Deleted concert");
                    mConcertRecyclerViewAdapter.removeConcert(concert);
                }
                else{
                    makeSnackBarMessage("Failed. Check log.");
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getConcerts();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void getConcerts() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference concertCollectionRef = db
                .collection("concerts");

        Query concertQuery = null;
        if (mLastQueriedDocument != null) {
            concertQuery = concertCollectionRef
                    .orderBy("data", Query.Direction.ASCENDING)
                    .startAfter(mLastQueriedDocument);
        } else {
            concertQuery = concertCollectionRef
                    .orderBy("data", Query.Direction.ASCENDING);
        }

        concertQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Concert concert = document.toObject(Concert.class);
                        mConcert.add(concert);
//                        Log.d(TAG, "onComplete: got a new note. Position: " + (mConcert.size() - 1));
                    }

                    if (task.getResult().size() != 0) {
                        mLastQueriedDocument = task.getResult().getDocuments()
                                .get(task.getResult().size() - 1);
                    }

                    mConcertRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    makeSnackBarMessage("Query Failed. Check Logs.");
                }
            }
        });
    }

    private void initRecyclerView() {
        if (mConcertRecyclerViewAdapter == null) {
            mConcertRecyclerViewAdapter = new ConcertRecyclerViewAdapter(this, mConcert);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mConcertRecyclerViewAdapter);
    }



    @Override
    public void updateConcert(final Concert concert) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference concertRef = db
                .collection("concerts")
                .document(concert.getConcert_id());

        concertRef.update(
                "Grup", concert.getGrup(),
                "Poblacio", concert.getPoblacio(),
                "Lloc", concert.getLloc()

        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    makeSnackBarMessage("Updated concert");
                    mConcertRecyclerViewAdapter.updateConcert(concert);
                } else {
                    makeSnackBarMessage("Failed. Check log.");
                }
            }
        });
    }

    @Override
    public void onConcertSelected(Concert concert) {
        ViewConcertDialog dialog = ViewConcertDialog.newInstance(concert);
        dialog.show(getSupportFragmentManager(), getString(R.string.dialog_view_concert));
    }

    @Override
    public void createNewConcert(String grup, String lloc, Date data, String poblacio, Double preu) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference newConcertRef = db
                .collection("concerts")
                .document();

        Concert concert = new Concert();
        concert.setGrup(grup);
        concert.setData(data);
        concert.setLloc(lloc);
        concert.setPoblacio(poblacio);
        concert.setPreu(preu);
        concert.setConcert_id(newConcertRef.getId());


        newConcertRef.set(concert).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    makeSnackBarMessage("Created new note");
                    getConcerts();
                } else {
                    makeSnackBarMessage("Failed. Check log.");
                }
            }
        });
    }

    private void makeSnackBarMessage(String message) {
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.fab: {
                //create a new note
                NewConcertDialog dialog = new NewConcertDialog();
                dialog.show(getSupportFragmentManager(), getString(R.string.dialog_new_concert));
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionSignOut:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        Log.d(TAG, "signOut: signing out");
        FirebaseAuth.getInstance().signOut();
    }

    /*
            ----------------------------- Firebase setup ---------------------------------
         */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}