package TP2;

import java.util.ArrayList;
import java.util.UUID;

public class Prescription {

    private UUID prescriptionId;
    private ArrayList<Drug> drugs = new ArrayList<Drug>();
    private int oneCycleDose;
    private int numberOfRep;

    public Prescription(UUID PrescriptionId, ArrayList<Drug>Drugs, int oneCycleDose, int numberOfRep){
        this.prescriptionId = PrescriptionId;
        this.drugs = drugs;
        this.oneCycleDose = oneCycleDose;
        this.numberOfRep = numberOfRep;
    }

    public ArrayList<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(ArrayList<Drug> drugs) {
        this.drugs = drugs;
    }

    public int getNumberOfRep() {
        return numberOfRep;
    }

    public void setNumberOfRep(int numberOfRep) {
        this.numberOfRep = numberOfRep;
    }

    public int getOneCycleDose() {
        return oneCycleDose;
    }

    public void setOneCycleDose(int oneCycleDose) {
        this.oneCycleDose = oneCycleDose;
    }
    
}
