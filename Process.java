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

    // clear the file writeFile
    try {
        FileWriter myWriter = new FileWriter(writeFile);
        // myWriter.write("");
        myWriter.close();

      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }

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

                        String [] medication = med.split("\\s+");

                        if(medication.length >= 2){
                            // System.out.println("name" + medication[0] + " quantity" + Integer.parseInt(medication[1]) + " date"  + medication[2] );
                            String drugName = medication[0];
                            Drug drug = new Drug(drugName, Integer.parseInt(medication[1]), new Date(medication[2]));
                            
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
                    writeResult(APPROV + " " + OK);
                    
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
                        writeResult("");
                        // clear order
                        this.order.clear();
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
                                
                                if(drug.getExpirationDate().compareTo(actualDate) >= 0){ // cetait 1 
                                    break;
                                }
                                else{
                                    queue.poll(); // removing experied drugs
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
                    
                    writeResult("");

                    break;
                case PRESCRIPTION:
                    writeResult(PRESCRIPTION +" " +prinsciptionNum++);
                    while(myReader.hasNextLine()){
                        
                        String med = myReader.nextLine();
                        
                        String [] medication = med.split("\\s+");

                        if(medication.length >= 3){

                            // String medicationName = medication[0];
                            String dose = medication[1];
                            String repetition = medication[2];

                            int traitmentDose = Integer.parseInt(dose);
                            int traitmentRepetition = Integer.parseInt(repetition);

                            
                            Drug drugPrescription = new Drug(medication[0], traitmentDose * traitmentRepetition, null);

                            Date  finalDate = actualDate.computeDate(drugPrescription.getQuantity());
                            
                            
                            if(this.stock.containsKey(drugPrescription.getName())){
                                // PriorityQueue<Drug> queue  = this.stock.get(drugPrescription.getName());
                                Boolean flag = false;


                                // pas necessaire de parcourrir toute la liste
                                // for (Drug drug : queue){
                                //     if (drug.getExpirationDate().compareTo(finalDate) >= 0){ // c'etait 1, bug dans exemple5 : med49 for expiration Date
                                //         if( drug.getQuantity() >= drugPrescription.getQuantity()){
                                //             drug.setQuantity(drug.getQuantity() - drugPrescription.getQuantity());
                                //             writeResult(drugPrescription.getName() + " " + traitmentDose + " " + repetition + " " +   OK);
                                //             flag = true;
                                //             if (drug.getQuantity() == 0){
                                //                 queue.remove(drug);
                                //             }
                                //             break;
                                //         }
                                //     }
                                // }
                                // Idee 1 :faire une copie de la liste, si med trouvé, ajuster la quantité dans stock
                                // Idee 2 : enlever les elements de la file, jusqua trouver le med, ensuite les ajouter dans la liste
                                // PriorityQueue<Drug> queue  = new PriorityQueue<Drug>(this.stock.get(drugPrescription.getName()));
                                PriorityQueue<Drug> queue = this.stock.get(drugPrescription.getName());
                                PriorityQueue<Drug> queueAux = new PriorityQueue<Drug>();
                                while (queue.size() != 0){

                                    Drug drug = queue.poll();
                                    queueAux.add(drug);

                                    if (drug.getExpirationDate().compareTo(finalDate) >= 0){ // c'etait 1, bug dans exemple5 : med49 for expiration Date
                                        if( drug.getQuantity() >= drugPrescription.getQuantity()){
                                            drug.setQuantity(drug.getQuantity() - drugPrescription.getQuantity());
                                            // this.stock.get(drugPrescription.getName()).
                                            writeResult(drugPrescription.getName() + " " + traitmentDose + " " + repetition + " " +   OK);
                                            flag = true;
                                            if (drug.getQuantity() == 0){
                                                queueAux.remove(drug);
                                            }
                                            break;
                                        }
                                    }
                                }

                                while (queueAux.size() != 0){
                                    Drug drug = queueAux.poll();
                                    queue.add(drug);
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
                    writeResult("");   
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