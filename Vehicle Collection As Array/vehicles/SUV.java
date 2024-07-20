
package vehicles;

/**
 *
 * @author dierbach
 */
public class SUV extends Vehicle {
    
    // additional attributes
    private String num_seats;
    private String cargo_capacity;
    
    // constructor
    public SUV(String descript, int mpg, String num_seats, String cargo_capacity,
               String VIN){
        super(descript, mpg, VIN);
        this.num_seats = num_seats;
        this.cargo_capacity = cargo_capacity;
    }
    
    // toString method
    public String toString(){
        String spacer = "  ";
        return "SUV: " + descript + spacer + "Seating Capacity: " + num_seats + spacer +
               "Cargo Capacity: " + cargo_capacity + " cu. ft." + spacer +
               "MPG: " + mpg + spacer + "VIN: " + VIN;
    }
}


