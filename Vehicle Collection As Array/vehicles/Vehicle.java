
package vehicles;

import vehicles.Exceptions.UnreservedVehicleException;

/**
 *
 * @author dierbach
 */
public abstract class Vehicle {
    
    // attributes
    protected String descript;
    protected int mpg;
    protected String VIN;
    private Reservation resv;
    
    // constructor
    public Vehicle(String descript, int mpg, String VIN){
        this.descript = descript;
        this.mpg = mpg;
        this.VIN = VIN;
        
        // init as "no reservation"
        resv = null;
    }
    
    // abstract toString method
    public abstract String toString();
    
    // general method
    public String getVIN(){
        return VIN;
    }
    
    public boolean isReserved(){
        return resv != null;
    }
    
    public void reserve(Reservation resv){
        this.resv = resv;
    }
    
    public Reservation getReservation(){
        return resv;
    }
    
    public void cancelReservation() throws UnreservedVehicleException{
        if(resv == null)
            throw new UnreservedVehicleException();
        
        resv = null;
    }
}
