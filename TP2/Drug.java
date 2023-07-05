package TP2;

public class Drug {

    private String name;
    private Date expirationDate;

    public Drug(String name, Date expirationDate) {
        this.name = name;
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

}
