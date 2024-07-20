//Maria Myers
package vehicles;

import vehicles.Exceptions.VINNotFoundException;
import vehicles.Exceptions.InsufficientSpaceException;


public class Vehicles {
    private Vehicle[] vehs;
    private int current;
    
    // constructor
    public Vehicles(){
        vehs = new Vehicle[25];
        initToNulls(vehs);
    }
    
    // general methods
    
    public Vehicle getVehicle(String vin) throws VINNotFoundException{
        int index = 0;
        boolean found = false;
        
        while(index < vehs.length && !found){
            if(vehs[index] != null && vehs[index].getVIN().equals(vin))
                found = true;
            else
                index = index + 1;
        } 
        
        // VIN not found if reach end of array
        if(!found)
            throw new VINNotFoundException();
        
        // return vehicle found
        return vehs[index];
    }
    
    public void add(Vehicle veh) throws InsufficientSpaceException{
        vehs[findFirstNull(vehs)] = veh;
    }
    
    public void remove(String VIN) throws VINNotFoundException {
        int index = 0;
        int removed_loc;
        boolean vehicle_found = false;
        
        while(index < vehs.length && vehs[index] != null && !vehicle_found){
            if (vehs[index].getVIN().equals(VIN))
                vehicle_found = true;
            else
                index = index + 1;
        }
        
        // VIN not found if reach end of string
        if(!vehicle_found)
            throw new VINNotFoundException();
        else
            removed_loc = index;
        
        // remove all following vehicles up one location
        for(int i = removed_loc; i < vehs.length - 1; i++)
            vehs[i] = vehs[i+1];
    }
    
    //iterator methods
    
    public void reset(){
        current = 0;
    }
    
    public boolean hasNext(){
    // check if another vehicle to return with changing iterator var current
        
        int index = current;
        
        while(index < vehs.length && vehs[index] == null)
            index = index + 1;
        
        // vehicle not found if reach end of string
        if(index < vehs.length)
            return true;
        else
            return false;
    }   
        
    public Vehicle getNext(){
        return vehs[current++];
    }
   
    // supporting methods
    
    private void initToNulls(Vehicle[] vehs){
        for(int i = 0; i < vehs.length; i++){
            vehs[i] = null;
        }
    }
    
    private int findFirstNull(Vehicle[] vehs){
        int i = 0;
        while(i < vehs.length && vehs[i] != null)
            i = i + 1;
        
        if(i == vehs.length)
            throw new InsufficientSpaceException();
        
        return i;
    }
}
