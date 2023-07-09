import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


public class Process {

private String readFile;
private String writeFile;

private TreeMap < String, PriorityQueue < Drug > > stock;
private TreeMap< String, Integer> order;

public final String APPROV = "APPROV";
public final String DATE = "DATE";
public final String STOCK = "STOCK";
public final String PRESCRIPTION = "PRESCRIPTION";
public final String OK = "OK";
public final String COMMANDE = "COMMANDE";
private int prinsciptionNum = 1;

private Date actualDate;

public Process (String [] args){
    this.readFile = args[0];
    this.writeFile = args[1];

    this.stock = new TreeMap<>();
    this.order = new TreeMap<>();


}

public Process(){
    this.stock = new TreeMap<>();
    this.order = new TreeMap<>();

}

public  void processDataFile(){
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

                        // String str = "one_space   multiple_spaces    tab";
                        String [] medication = med.split("\\s+");

                        // arr[0] = one_space
                        // arr[1] = multiple_spaces
                        // arr[2] = tab


                        // String[] medication = med.split(" ");
                        
                        // if(medication.length == 1){ 
                        //     medication = medication[0].split("\t");
                        // }
                        
                        // System.out.println("taille med " + medication.length);
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
                case DATE: 
                    // System.out.print("in date");
                    String date = line[1];
                    actualDate = new Date(date);
                    
                    if(this.order.size() > 0){
                        writeResult(actualDate.toString() + " " + COMMANDE + " :");

                        Set <String> names = order.keySet();

                        for ( String name : names) {
                            
                            writeResult(name + " " + order.get(name));
                            
                        }
                        writeResult("\n");
                    }else{
                        // System.out.print("in else statement");
                        writeResult(actualDate.toString() + " " + OK + "\n");

                    }

                    break;
                case STOCK:
                    
                    writeResult(STOCK + " " + actualDate.toString());
                    Set<String> names = stock.keySet();

                    for (String name  : names) {
                        
                        PriorityQueue<Drug> queue = stock.get(name);
                            // find the first ele in queue that has the good expiration date
                        while(queue.size() != 0 ){
                                // if the first date is good, the others are good also
                                Drug drug = queue.peek();
                                if(drug.getExpirationDate().compareTo(actualDate) >= 0){ // c'etait 1
                                    break;
                                }
                                else{
                                    stock.get(name).remove(drug); // removing experied drugs.// or poll
                                    queue.poll();
                                }
                            }

                            queue.forEach(obj ->  writeResult(obj.toString()));

                        // ConcurrentModificationException in Exemple 3
                        // Maybe it is not necessary because we print queue 
                        // if(stock.get(name).size() == 0){ // checking if there's still any drug list associated to this name 
                        //     stock.remove(name);// if not the node is removed from the tree.
                        //     // break;
                            
                        // }  
                    }
                    
                    
                

                    writeResult("\n");

                    break;
                case PRESCRIPTION:
                    writeResult(PRESCRIPTION +" " +prinsciptionNum++);
                    while(myReader.hasNextLine()){
                        
                        String med = myReader.nextLine();
                        
                        // String[] medication = med.split(" ");

                        String [] medication = med.split("\\s+");

                    

                        if(medication.length >= 3){

                            String medicationName = medication[0];
                            String dose = medication[1];
                            String repetition = medication[2];

                            int traitmentDose = Integer.parseInt(dose);
                            int traitmentRepetition = Integer.parseInt(repetition);

                            
                            Drug drugPrescription = new Drug(medicationName, traitmentDose * traitmentRepetition, null);

                            Date  finalDate = actualDate.computeDate(drugPrescription.getQuantity());
                            
                            
                            if(this.stock.containsKey(drugPrescription.getName())){
                                PriorityQueue<Drug> queue  = this.stock.get(drugPrescription.getName());
                                Boolean flag = false;

                                for (Drug drug : queue){
                                    // int numODays = actualDate.getNumODays(drug.getExpirationDate());
                                    if (drug.getExpirationDate().compareTo(finalDate) >= 1){ // for expiration Date
                                        if( drug.getQuantity() >= drugPrescription.getQuantity()){
                                            drug.setQuantity(drug.getQuantity() - drugPrescription.getQuantity());
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
                                   addToOrder(drugPrescription,traitmentDose,traitmentRepetition,drugPrescription.getQuantity());
                                }
                                
                            }
                            else{
                                
                                addToOrder(drugPrescription,traitmentDose,traitmentRepetition,drugPrescription.getQuantity());
                                
                                
                            }
                        }
                        else{
                            
                            break;
                        }
                    }     
                    writeResult("\n");   
                    break;
                
                default:
                    break;
        }
     }
        myReader.close();
    } 
    catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}




  




private void writeResult(String st){
    try {
        // System.out.println(st);
        FileWriter myWriter = new FileWriter(writeFile,true);
        myWriter.write(st + "\n");
        
        
        myWriter.close();

      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}

private void addToOrder(Drug d, int dose, int rep,int quantity){
     writeResult(d.getName() + " " + dose + " " + rep + " " +   COMMANDE);
    if(order.containsKey(d.getName())){
                                        
        order.put(d.getName(), order.get(d.getName()) + quantity); 
    }
    else{
        order.put(d.getName(), quantity);
    }
    
}




public void compute(){
    processDataFile();

}

}