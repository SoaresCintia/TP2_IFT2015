import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
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

    // clear the file writeFile for testing
    try {
        FileWriter myWriter = new FileWriter(writeFile);
        myWriter.close();
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}

// public Process(){
//     this.stock = new TreeMap<>();
//     this.order = new TreeMap<>();
// }

public  void processDataFile(){
try {
    File myObj = new File(this.readFile);
    Scanner myReader = new Scanner(myObj);
        
    while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        String[] line = data.split(" ");
        String command = line[0];
            
        switch (command) {
            case APPROV:  // O(n)

                // O(n)
                while(myReader.hasNextLine()){ 

                    String med = myReader.nextLine();
                    String [] medication = med.split("\\s+");


                    if(medication.length >= 2){
                        String drugName = medication[0];
                        Drug drug = new Drug(drugName, 
                                            Integer.parseInt(medication[1]), 
                                            new Date(medication[2]));
                            
                        // O (log n)
                        if(this.stock.containsKey(drugName)) // O(log n)
                        { 
                            this.stock.get(drugName).add(drug); // O(log n + log t ) = O (log n)
                        } else {
                            PriorityQueue<Drug> queue = new PriorityQueue<>();
                            queue.add(drug); // O(1), car la liste est vide
                            this.stock.put(drugName,queue); // O(log n)
                        }
                    }else{
                        break;
                    }
                }        
                writeResult(APPROV + " " + OK);
                    
                break;

            case DATE: 
                String date = line[1];
                actualDate = new Date(date);
                
                // O(k)
                if(this.order.size() > 0){
                    writeResult(actualDate.toString() + " " + COMMANDE + " :");

                    Set <String> names = order.keySet(); // O(k)

                    // O(k)
                    for ( String name : names) // O(k)
                    {
                        writeResult(name + " " + order.get(name)); // O(1)        
                    }

                    writeResult("");

                    // O(k)
                    this.order.clear();
                }else{
                    writeResult(actualDate.toString() + " " + OK + "\n");
                }

                break;
            case STOCK:
                    
                writeResult(STOCK + " " + actualDate.toString());
                Set<String> names = stock.keySet();

                for (String name  : names) {
                        
                    PriorityQueue<Drug> queue = stock.get(name);

                    // find the first element in queue with good expiration date
                    while(queue.size() != 0 ){
                            // if the first date is good, the others are good too
                            Drug drug = queue.peek();
                                
                            if(drug.getExpirationDate().compareTo(actualDate) >= 1){ // Exemple8, medication11 
                                break;
                            }
                            else{
                                queue.poll(); // removing experied drugs
                            }
                        }
                        queue.forEach(obj ->  writeResult(obj.toString()));
                }
                writeResult("");
                break;
            
            case PRESCRIPTION: // O( m*n*log n + m*log k))

                writeResult(PRESCRIPTION + " " + prinsciptionNum++); // O(1)

                while(myReader.hasNextLine()){ // O(m * (n log n + log k))
                        
                    String med = myReader.nextLine();
                    String [] medication = med.split("\\s+");

                    // O (n log n + log k)
                    if(medication.length >= 3){ 

                        String dose = medication[1];
                        String repetition = medication[2];

                        int traitmentDose = Integer.parseInt(dose);
                        int traitmentRepetition = Integer.parseInt(repetition);

                        Drug drugPrescription = new Drug(
                                        medication[0], 
                                        traitmentDose * traitmentRepetition, 
                                        null);

                        Date  finalDate = actualDate.computeDate(drugPrescription.getQuantity());
                            
                        // O( log t + p*log p + log k ) = O (n log n + log k)
                        if(this.stock.containsKey(drugPrescription.getName()))//O(log t)
                        { 
                            Boolean flag = false;
                            // Remove items from the queue until find the correct drug, then add them to the list again
                            PriorityQueue<Drug> queue = this.stock.get(drugPrescription.getName()); //O(1)
                            Stack<Drug> stack = new Stack<>(); //O(1)
                                
                            while (queue.size() != 0){ // O(p) 

                                Drug drug = queue.poll(); // O(1)
                                stack.push(drug); // O(1)

                                // O(1)
                                if (drug.getExpirationDate().compareTo(finalDate) >= 0){ // O(1)

                                    if( drug.getQuantity() >= drugPrescription.getQuantity()){

                                        drug.setQuantity(drug.getQuantity() - drugPrescription.getQuantity());
                                        writeResult(drugPrescription.getName() + " " + traitmentDose + " " + repetition + " " +   OK);
                                        flag = true;

                                        if (drug.getQuantity() == 0){
                                            stack.pop(); // O(1), on va faire cela au maximum 1 fois
                                        }
                                        break;
                                    }
                                }
                            }

                            while (stack.size() != 0){  //O(p log p)
                                Drug drug = stack.pop(); //O(1)
                                queue.add(drug); //O(log p)
                            }

                            if(!flag){ // O(log k)
                               addToOrder(drugPrescription,traitmentDose,traitmentRepetition,drugPrescription.getQuantity());
                            }
                                
                        }
                        else{ // O(log k)
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
        FileWriter myWriter = new FileWriter(writeFile,true);
        myWriter.write(st + "\n");
        myWriter.close();

    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}

//O(log k)
private void addToOrder(Drug d, int dose, int rep,int quantity){
     writeResult(d.getName() + " " + dose + " " + rep + " " +   COMMANDE); // O(1)
    
    //O(log k)
    if(order.containsKey(d.getName())) //O(log k)
    {                         
        order.put(d.getName(), order.get(d.getName()) + quantity); //O(log k)
    }
    else{
        order.put(d.getName(), quantity); //O(log k)
    }
}

public void compute(){
    processDataFile();
}

}