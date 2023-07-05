package TP2;

import java.util.ArrayList;

public class Stock {

    private ArrayList<Drug> drugs;

    public Stock(ArrayList<Drug> drugs) {
        this.drugs = drugs;
    }

    public ArrayList<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(ArrayList<Drug> drugs) {
        this.drugs = drugs;
    }


    
}
