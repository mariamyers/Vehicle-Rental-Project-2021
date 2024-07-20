
package vehicles;

/**
 *
 * @author dierbach
 */
public class Truck extends Vehicle {
    
    // additional attributes
    private String load_capacity;
    
    // constructor
    public Truck(String descript, int mpg, String load_capacity, String VIN){
        super(descript, mpg, VIN);
        this.load_capacity = load_capacity;
    }
    
    // toString method
    public String toString(){
        String spacer = "  ";
        return "TRUCK: " + descript + spacer + "Load Capacity: " + load_capacity + " lbs." +
               spacer + "MPG: " + mpg + spacer + "VIN: " + VIN;
    }
}

