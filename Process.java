import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Process {

private String readFile;
private String writeFile;
//private Stock  stoc;
// private TreeSet<Drug> stock;
private TreeMap < String, PriorityQueue < Drug > > stock;
private TreeSet<Drug> order;

public final String APPROV = "APPROV";
public final String DATE = "DATE";
public final String STOCK = "STOCK";
public final String PRESCRIPTION = "PRESCRIPTION";
public final String OK = "OK";
public final String COMMANDE = "COMMANDE";

private Date actualDate;

// private FileWriter myWriter ;

public Process (String [] args){
    this.readFile = args[0];
    this.writeFile = args[1];

    // this.myWriter = new FileWriter(writeFile);

    this.stock = new TreeMap<>();
    this.order = new TreeSet<>();


}

private  void processDataFile(){
    try {
        File myObj = new File(this.readFile);
        Scanner myReader = new Scanner(myObj);
        
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] line = data.split(" ");
            String command = line[0];
            
            
            switch (command) {
                case APPROV: 
                    while(myReader.hasNextLine()){
                        String med = myReader.nextLine();
                        String[] medication = med.split(" ");
                        if(medication.length >= 2){
                            // System.out.println("name" + medication[0] + " quantity" + Integer.parseInt(medication[1]) + " date"  + medication[2] );
                            String drugName = medication[0];
                            Drug drug = new Drug(medication[0], Integer.parseInt(medication[1]), new Date(medication[2]));
                            
                            if(this.stock.containsKey(drugName)){
                                this.stock.get(drugName).add(drug);
                            }
                            else {
                                PriorityQueue<Drug> queue = new PriorityQueue<>();
                                queue.add(drug);
                                this.stock.put(drugName,queue);

                            }
                        }
                        else{
                            break;
                        }
                    }        
                    writeResult(APPROV + " " + OK + "\n");
                    
                    break;
                case DATE: // garder la date ? 
                            // écrire : OK ou date COMMANDES : liste des medicaments
                    System.out.print("in date");
                    String date = line[1];
                    // System.out.println();
                    actualDate = new Date(date);
                    
                    if(this.order.size() > 0){
                        writeResult(actualDate.toString() + " " + COMMANDE + " :");
                        for (Drug d : order) {
                            writeResult(d.getName() + "  " + d.getQuantity());
                            
                        }
                        writeResult("\n");
                    }else{
                        System.out.print("in else statement");
                        writeResult(actualDate.toString() + " " + OK + "\n");

                    }

                    break;
                case STOCK:
                    writeResult(STOCK + " " + actualDate.toString());
                    Set<String> names = stock.keySet();
                    for (String name  : names) {
                         stock.get(name).forEach( obj ->{
                            if(obj.getExpirationDate().compareTo(actualDate) >= 1){
                                writeResult(obj.toString());
                            }
                            else{
                                stock.get(name).remove(obj); // removing experied drugs.
                            }
                            
                        }); 
                        if(stock.get(name).size() == 0){ // checking if there's still any drug list associated to this name 
                            stock.remove(name);// if not the node is removed from the tree.
                        }  
                    }
                    writeResult("\n");

                    break;
                case PRESCRIPTION:
                    while(myReader.hasNextLine()){
                    String med = myReader.nextLine();
                    String[] medication = med.split(" ");
                    if(medication.length >= 3){
                        // calculer la quantite total qui est aussi le nombre des jours de traitement
                        // chercher le medicament dans stock, si trouvé, verifier la data
                        // si medicament trouvé afficher la prescription avec identifiant
                        // Ok si trouvé dans stock
                        // sinon Commande et ajouter dans order
                        Drug drugPrescription = new Drug(medication[0], 0, null);
                        int quantity =  Integer.parseInt(medication[1])* Integer.parseInt(medication[2]);
                        
                        if(this.stock.containsKey(drugPrescription.getName())){
                            PriorityQueue<Drug> queue  = this.stock.get(drugPrescription.getName());

                            

                            
                            
                        };
                    }
                    else{
                        break;
                    }
                }        
                writeResult(APPROV + " " + OK + "\n");

                    
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




private void writeResult(String st){
    try {
        System.out.println(st);
        FileWriter myWriter = new FileWriter(writeFile,true);
        myWriter.write(st + "\n");
        
        
        myWriter.close();

      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }


public void compute(){
    processDataFile();

}

}