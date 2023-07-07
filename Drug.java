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
        if(this.name.compareTo(d.getName()) == 0) {
            return this.expirationDate.compareTo(d.getExpirationDate());
        }else{
            return this.name.compareTo(d.getName());
        }

    }

    @Override
    public boolean equals(Object o){
        if(this == o ){
            return true;
        }
        if(o == null){
            return false;

        }
        if(o.getClass() != getClass()){
            return false;
        }
        Drug d = (Drug) o;

        return this.name.equals(d.getName());
    }
    @Override
    public String toString(){
        return this.name + " " + this.quantity + " " + this.expirationDate.toString();
    }

    public int getQuantity() {
        return this.quantity;
    }
}