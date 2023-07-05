import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeSet;

public class Process {

private String readFile;
private String writeFile;
//private Stock  stoc;
private TreeSet<Drug> stock;

public final String APPROV = "APPROV";
public final String DATE = "DATE";
public final String STOCK = "STOCK";
public final String PRESCRIPTION = "PRESCRIPTION";

public Process (String [] args){
    this.readFile = args[0];
    this.writeFile = args[1];
    this.stock = new TreeSet<>();


}

private  void processDataFile(){
    try {
        File myObj = new File(this.readFile);
        Scanner myReader = new Scanner(myObj);
        
        while (myReader.hasNextLine()) {
          String data = myReader.nextLine();
          String[] line = data.split(" :");
          String command = line[0];
          
         
         
            
            switch (command) {
                case APPROV: // lire la liste des medicaments 
                    while(myReader.hasNextLine()){
                        
                            String med = myReader.nextLine();
                            String[] medication = med.split(" ");
                            if(medication.length >= 2){
                                // System.out.println("name" + medication[0] + " quantity" + Integer.parseInt(medication[1]) + " date"  + medication[2] );
                                this.stock.add(new Drug(medication[0], Integer.parseInt(medication[1]), new Date(medication[2])));
                                System.out.println(stock);
                            }
                            else{
                                break;
                            }
                           
    
                    }         // et ajouter dans le stock
                             // écrire: APPROV OK

                    break;
                case DATE: // garder la date ? 
                            // écrire : OK ou date COMMANDES : liste des medicaments

                    break;
                case STOCK:

                    break;
                case PRESCRIPTION:

                    break;
        
                default:
                    break;
        }
        }
        myReader.close();
      } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
}

public void compute(){
    processDataFile();

}

}