public class Tp2{
    
    public static void main(String[] args) {
        Process process = new Process();

        int count = 1;
        String fileName = "tests/exemple";
        String fileName2 = "tests/sortie";
        while(count <= 8){
            System.out.println(fileName + count +".txt");
            System.out.println(fileName2 + count +".txt");
            process.processDataFile(fileName + count +".txt",fileName2+ count +".txt");
            count++;
        }
        
    }

}