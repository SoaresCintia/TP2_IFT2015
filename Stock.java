import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

public class Stock {

    // private ArrayList<Drug> drugs;


    private TreeMap < String, PriorityQueue < Drug > > drugs;  

    public Stock( TreeMap < String, PriorityQueue< Drug > > drugs) {
        this.drugs = drugs;
    }

    public TreeMap < String, PriorityQueue< Drug > > getDrugs() {
        return drugs;
    }

    public void setDrugs(TreeMap < String, PriorityQueue < Drug > > drugs) {
        this.drugs = drugs;
    }

    

    
}
