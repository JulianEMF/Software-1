package model;

/** The Outsourced class extends the class Part allowing access to the Part class methods and variables. */
public class Outsourced extends Part{

    private static String companyName;

    // Constructor
    public Outsourced (int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     @param companyName String
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     @return companyName
     */
    public static String getCompanyName(){
        return companyName;
    }
}
