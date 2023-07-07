import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
private TreeMap< String, Integer> order;

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
    this.order = new TreeMap<>();


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

                        Set <String> names = order.keySet();

                        for ( String name : names) {
                            
                            writeResult(name + " " + order.get(name) );
                            
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
                    
                    // ArrayList<String> meds = new ArrayList<String>();
                    // for(int i=0; i<medication.length; i++){
                    //     if(!medication[i].equals(" ")){
                    //         meds.add(medication[i]);
                    //     }
                    // }
                    // meds.forEach(obj -> {
                        
                    // });
                    // System.out.println(meds);

                    
                    
                    // System.out.println(meds);
                    // System.out.println(meds.size());

                    if(medication.length >= 3){

                        // fullName = kybd.nextLine(); med
                    // Remove leading and trailing whitespaces
                    String fullName = med.trim();

                    // Bounds
                    int firstSpace = fullName.indexOf(" ");
                    int lastSpace = fullName.lastIndexOf(" ");

                    // Extract names
                    String medicationName = fullName.substring(0, firstSpace);
                    String dose = fullName.substring(firstSpace + 1, lastSpace);
                    String repetition = fullName.substring(lastSpace + 1);

                    // Trim everything
                    medicationName = medicationName.trim(); // Not needed
                    dose = dose.trim();
                    repetition = repetition.trim();

                    System.out.println("medicationtest " + medicationName);

                    System.out.println("dose " + dose);

                    System.out.println( "repetition " + repetition);

                        
                        Drug drugPrescription = new Drug(medicationName, 0, null);


                        int traitmentDose = Integer.parseInt(dose);

                        int traitmentRepetition = Integer.parseInt(repetition);

                        int quantity =  traitmentDose * traitmentRepetition;
                        
                        if(this.stock.containsKey(drugPrescription.getName())){
                            PriorityQueue<Drug> queue  = this.stock.get(drugPrescription.getName());
                            Boolean flag = false;

                            for (Drug drug : queue){
                                int numODays = actualDate.getNumODays(drug.getExpirationDate());
                                if (numODays >= quantity){ // for expiration Date
                                    if( drug.getQuantity() >= drugPrescription.getQuantity()){
                                        drug.setQuantity(drug.getQuantity() - quantity);
                                        writeResult(drugPrescription.getName() + " " + traitmentDose + " " + repetition + " " +   OK);
                                        flag = true;
                                        if (drug.getQuantity() == 0){
                                            queue.remove(drug);
                                        }
                                        break;
                                    }
                                }
                            }
                            if(!flag){
                                writeResult(drugPrescription.getName() + " " + traitmentDose + " " + traitmentRepetition + " " +   COMMANDE);
                                
                                if(order.containsKey(drugPrescription.getName())){
                                    
                                    order.put(drugPrescription.getName(), order.get(drugPrescription.getName()) + quantity); 
                                }
                                else{
                                    order.put(drugPrescription.getName(), quantity);
                                }
                            }
                            
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