package model;

/** The InHouse class extends the class Part allowing access to the Part class methods and variables. */
public class InHouse extends Part{

    private int machineId;

    // Constructor
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     @param machineId int
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**
     @return machineId
     */
    public int getMachineId(){
        return machineId;
    }

}
