public class Drug implements Comparable<Drug> {

    private String name;
    private Date expirationDate;
    private int quantity;

    public Drug(String name,int quantity,Date expirationDate) {
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int compareTo(Drug d) {
        
            return this.expirationDate.compareTo(d.getExpirationDate());
    

    }
    @Override
    public String toString(){
        return "name: " + this.name + " quantity " + this.quantity + " expDate :" + this.expirationDate.toString();
    }
}