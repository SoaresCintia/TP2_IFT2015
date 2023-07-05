import java.util.TreeSet;

public class Stock {

    // private ArrayList<Drug> drugs;


    private TreeSet<Drug> drugs;  

    public Stock(TreeSet<Drug> drugs) {
        this.drugs = drugs;
    }

    public TreeSet<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(TreeSet<Drug> drugs) {
        this.drugs = drugs;
    }


    
}
