package com.example.jordi.concertlist;

import java.util.Date;

public interface IMainActivity {

    void createNewConcert(String grup, String lloc, Date data, String poblacio, Double preu);

    void updateConcert(Concert concert);

    void onConcertSelected(Concert concert);

    void deleteConcert(Concert concert);
}