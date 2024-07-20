
package vehicles;

/**
 *
 * @author dierbach
 */
public class Car extends Vehicle {
    
    // additional attributes
    private String num_seats;
    
    // constructor
    public Car(String descript, int mpg, String num_seats, String VIN){
        super(descript, mpg, VIN);
        this.num_seats = num_seats;
    }
    
    // toString method
    public String toString(){
        String spacer = "  ";
        return "CAR: " + descript + spacer + "Seating Capacity: " + num_seats + spacer +
               "MPG: " + mpg + spacer + "VIN: " + VIN;
    }
}

