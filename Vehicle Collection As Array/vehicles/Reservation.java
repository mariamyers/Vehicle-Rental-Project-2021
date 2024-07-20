
package vehicles;

import vehicles.Exceptions.InvalidRentalPeriodFormatException;

/**
 *
 * @author C. Dierbach
 */
public class Reservation {
    
    // attributes
    private String credit_card;
    private String rental_period;
    private boolean insur_selected;
    
    // constructor
    public Reservation(String credit_card, String rental_period, 
                       boolean insur_selected){
        this.credit_card = credit_card;
        this.rental_period = rental_period;
        this.insur_selected = insur_selected;
    }
    
    // toString method
    public String toString(){
        String spacer = "   ";
        String insur_option_descript;
        
        if(insur_selected)
            insur_option_descript = "Daily Insurance Included";
        else
            insur_option_descript = "Daily Insur Declined";
        
        return "Credit Card: " + credit_card + spacer +
               "Rental Period: " + rentalPeriodDescript(rental_period) + spacer +
               insur_option_descript;   
    }
    
    // general methods
    public String getCreditCard(){
        return credit_card;
    }
    
    public boolean insur_selected(){
        return insur_selected;
    }
    
    
    // -- supporting methods
    private String rentalPeriodDescript(String rental_period) 
            throws InvalidRentalPeriodFormatException{
        
        String timeperiod_descript;
        String lengthtime_descript;
        String spacer = " ";
        
        char time_period = Character.toUpperCase(rental_period.charAt(0));
        switch(time_period){
            case 'D': timeperiod_descript = "days"; break;
            case 'W': timeperiod_descript = "weeks"; break;
            case 'M': timeperiod_descript = "months"; break;
            default: throw new InvalidRentalPeriodFormatException();
        }
        lengthtime_descript = rental_period.substring(1, rental_period.length());
        
        return lengthtime_descript + spacer + timeperiod_descript;
    }
}
