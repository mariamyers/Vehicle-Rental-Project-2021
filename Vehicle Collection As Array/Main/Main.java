
package Main;

// import project packages
import vehicles.*;
import vehicles.Exceptions.*;

import utilities.*;
import utilities.Exceptions.*;

// import from Java API packages
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 *
 * @author dierbach
 */
public class Main {
    
    // symbolic constants
    public static final int CAR = 1;
    public static final int SUV = 2;
    public static final int TRUCK = 3;
    
    // create empty collection of vehicles
    private static Vehicles vehs = new Vehicles();
   
    // init
    private static boolean quit = false;
    private static Scanner input = new Scanner(System.in);
    
    // main
    public static void main(String[] args) {
       
       // set hardcoded set of vehicles
       populateVehicles(vehs);
       
       // command loop (for selection/execution of use cases)
       int selection;
       while(!quit){
           displayMenu();
           selection = getSelection();
           execute(selection);
       }
    }
    
    // -- Command Loop Supporting Methods  ---------------------------------
    
    private static void displayMenu(){
    // displays list of available use cases
        
        System.out.println(); // skip line
        System.out.println("1 - Display All Vehicles");
        System.out.println("2 - Display Available Vehicles");
        System.out.println("3 - Reserve a Vehicle");
        System.out.println("4 - Cancel a Reservation");
        System.out.println("5 - Display Reservation");
        System.out.println("6 - Add a Vehicle");
        System.out.println("7 - Remove a Vehicle");
        System.out.println("8 - Quit");
        System.out.println();
    }
    
    private static int getSelection(){
    // reprompts user until valid entry entered
    // returns value between 1-8, inclusive
     
        int selection = 0;  // init
        
        boolean valid_input = false;
       
        while(!valid_input){
            try{

                System.out.print("Enter: ");
                selection = input.nextInt();

                if(selection >= 1 && selection <= 8)
                    valid_input = true;
                else
                    System.out.println("* INVALID SELECTION - PLEASE REENTER *");
            }    
            // catch if non-digit entered (thrown by method nextInt)
            catch(InputMismatchException e){
                System.out.println("* INVALID CHAR ENTERED - PLEASE REENTER *");
                System.out.println(); // skip line
                input.next(); // scan past unread non-digit char
            }
        }
        return selection;       
    }
    
    private static void execute(int selection){
    // executes the corresponding method implementing each use case
 
        System.out.println();  // skip line
        switch(selection){
            case 1: displayAllVehicles(); break;
            case 2: displayAvailVehicles(); break;
            case 3: makeReservation(); break;
            case 4: cancelReservation(); break;
            case 5: displayReservation(); break;
            case 6: addVehicle(); break;
            case 7: removeVehicle(); break;
            case 8: quit = true;
        }
    }
    
    // -- Use Case Methods  ------------------------------------------------
    
    private static void displayAllVehicles(){
    // displays all vehicles (reserved and unreserved)
    
        // display heading
        System.out.println("---- ALL VEHICLES (reserved and unreserved)");
        System.out.println();  // skip line
        
        // display vehicles
        vehs.reset();
        while(vehs.hasNext())
            System.out.println((vehs.getNext().toString()));
    }
    
    private static void displayAvailVehicles(){
    // displays all unreserved vehicles
        
        // display heading
        System.out.println("---- ALL AVAILABLE (unreserved) VEHICLES");
        System.out.println();  // skip line
        
        // display vehicles
        vehs.reset();
        Vehicle veh;
        
        while(vehs.hasNext()){
            veh = vehs.getNext();
            if(!veh.isReserved())
                System.out.println((veh.toString()));
        }
    }
        
    private static void makeReservation(){
    // request reservation information from user
    // assigns reservation vehicle reserved

        String entered_VIN;
        String entered_creditcard = "";   // init
        String entered_rentalperiod = ""; // init
    
        char YN_char;
        boolean daily_insur_selected = false; // init
        Vehicle veh = null;
        
        // display heading
        System.out.println("Reservation Entry ...");
        
        try{
            // get VIN of vehicle to reserve
            entered_VIN = getValidExistingVINFromUser();
            
            // get vehicle for given VIN
            veh = vehs.getVehicle(entered_VIN);
               
            // get desired rental period
            // (e.g., D2 - two days, W3-three weeks, M1-one month)
            System.out.println(); // skip line
            entered_rentalperiod = getValidRentalPeriod();
     
            // request if insurance desired
            YN_char = Utilities.getYesNoResponse("Daily Insurance Desired?", input);
            daily_insur_selected = (YN_char == 'Y'); // set boolean value
            
             // display details of vehicle found
            System.out.print("Making reservation for ");
            System.out.println(veh.toString());
       
            // input and validate credit card
            entered_creditcard = getValidCreditCard();
            
            // create reservation
            veh.reserve(new Reservation(entered_creditcard, entered_rentalperiod,
                        daily_insur_selected));
            
            System.out.println("* RESERVATION CONFIRMED *");
        }  
        catch(FailedVINEntryAttemptsException e){
            System.out.println("Failed attempt at VIN entry.");
            System.out.println("Returning to main menu ...");
        }
        catch(FailedRentalPeriodEntryException e){
            System.out.println();  // skip line
            System.out.println("Failed attempt at rental period entry.");
            System.out.println("Returning to main menu ...");
        }
        catch(FailedCreditCardEntryAttemptsException e){ 
            System.out.println("Failed attempt at credit card entry.");
            System.out.println();  // skip line
            System.out.println("Returning to main menu ...");
        }
    }
    
    private static void cancelReservation(){
    // requests credit card number of reservation to cancel
 
        String entered_creditcard = "";
        char YN_char;
        Vehicle veh;
        
        // display heading
        System.out.println("Cancel Reservation Entry ...");
        
        try{
            // get credit card number of reservation from user
            entered_creditcard = getValidCreditCard();

            // get reservation
            veh = getReservedVehicleByCreditCard(entered_creditcard);
            
            // display reservation info
            System.out.println();
            System.out.println("RESERVATION INFORMATION");
            System.out.println(veh.toString());
            System.out.println(veh.getReservation().toString());
            
            // confirm cancellation
            YN_char = Utilities.getYesNoResponse("Please confirm", input);
            
            if(YN_char == 'N')
                // abort cancellation if not confirmed
                System.out.println("* CANCELLATION ABORTED *");
            else {
                // cancel confirmed reservation
                veh.cancelReservation();
                System.out.println("* RESERVATION SUCESSFULLY CANCELLED *");
            }
        }
        catch(FailedCreditCardEntryAttemptsException e){
            System.out.println("Failed attempt at credit card entry.");
            System.out.println();  // skip line
            System.out.println("Returning to main menu ...");
        }
        catch(ReservationNotFoundException e){
            System.out.print("* Reservation Not Found for ");
            System.out.println("Credit Card Number " + entered_creditcard + " *");
        }
    }
    
    private static void displayReservation(){
    // display the details of a given reservation

        String entered_creditcard = ""; // init
        Vehicle veh;
        
        // display heading
        System.out.println("Display Reservation Entry ...");
        
        try{
            // get credit card number of reservation from user
            entered_creditcard = getValidCreditCard();
            
            // get reservation
            veh = getReservedVehicleByCreditCard(entered_creditcard);
            
            // display reservation info
            System.out.println();  // skip line
            System.out.println("RESERVATION INFORMATION");
            System.out.println(veh.toString());
            System.out.println(veh.getReservation().toString());
        }
        catch(FailedCreditCardEntryAttemptsException e){
            System.out.println("Failed attempt at credit card entry.");
            System.out.println();  // skip line
            System.out.println("Returning to main menu ...");
        }    
        catch(ReservationNotFoundException e){
            System.out.print("* RESERVATION NOT FOUND FOR ");
            System.out.println("Credit Card Number " + entered_creditcard + " *");
        }
    }
    
    private static void addVehicle(){
    // requests vehicle information from user and adds to Vehicles collection

        String veh_descript;
        String VIN;
        int mpg;
        
        char YN_char;
        int vehicle_type;
        String value1 = ""; // init
        String value2 = ""; // init
        Vehicle new_veh = null;
        
        // display heading
        System.out.println("New Vehicle Entry ... \n");
        
        try{
            // get vehicle type
            vehicle_type = getVehicleType();
            
            // get vehicle description
            System.out.print("Enter vehicle make and model: ");
            veh_descript = input.nextLine();
            //input.nextLine();
            
            // get mpg
            System.out.print("Enter mpg: ");
            mpg = input.nextInt();
            
            // get vehicle type specific info
            switch(vehicle_type){
                case CAR: System.out.print("Enter seating capacity: ");
                          value1 = input.next();
                          break;
                case SUV: System.out.print("Enter seating capacity: ");
                          value1 = input.next();
                          System.out.print("Enter cargo capacity: ");
                          value2 = input.next();
                          break;
                case TRUCK: System.out.print("Enter load capacity: ");
                            value1 = input.next();
                            break;
            }
            
            // get VIN
            VIN = getValidVINFromUser();
            
            // add new vehicle
            switch(vehicle_type){
                case CAR: new_veh = new Car(veh_descript, mpg, value1, VIN);
                          break;
                case SUV: new_veh = new SUV(veh_descript, mpg, value1, value2, VIN);
                          break;
                case TRUCK: new_veh = new Truck(veh_descript, mpg, value1, VIN);
                            break;
            }
        }
        catch(FailedVINEntryAttemptsException e){
            System.out.println("Failed attempts at VIN entry ...");
            System.out.println("Returning to main menu ...");
        }
        catch(FailedCreditCardEntryAttemptsException e){  
            System.out.println("Failed attempts at credit card entry ...");
        }

        // add new vehicle to collection
        vehs.add(new_veh);
        
        // display confirmation info
        System.out.println(); // skip line
        System.out.println("* NEW VEHICLE SUCESSFULLY ADDED *");
        System.out.println(new_veh.toString());
    }
    
    private static void removeVehicle(){
    // removes specified vehicle from the collection
 
        String VIN = "";
        char YN_char;
        
        // display heading
        System.out.println("Remove Vehicle Entry ...");
        
        try{
            // get credit card number of reservation from user
            VIN = getValidExistingVINFromUser();
            
            // display details of vehicle to remove
            System.out.println(vehs.getVehicle(VIN).toString());
            
            // confirm cancellation
            YN_char = Utilities.getYesNoResponse("Please confirm", input);
            
            if(YN_char == 'N')
                // abort if not confirmed
                System.out.println("* VEHICLE REMOVABLE ABORTED *");
            else {
                // remove vehicle
                vehs.remove(VIN);
                System.out.println("* VEHICLE SUCESSFULLY REMOVED *");
            }
        }
        catch(FailedVINEntryAttemptsException e){
            System.out.println("* Valid VIN not successfully entered *");
            System.out.println(); // skip line
            System.out.println("Returning to Main Menu ...");
        }
    }
    
    // Supporting Methods  --------------------------------------------------
        
    private static void populateVehicles(Vehicles vehs){
    // adds to vehicles collection a specific mix of Car, SUV and Truck objects
        
        vehs.add(new Car("Chevrolet Camaro", 30, "2", "HK4GM4564GD"));
        vehs.add(new Car("Ford Fusion", 34, "4", "AB4EG5689GM"));
        vehs.add(new SUV("Honda Odyssey", 28, "7", "6", "VM9RE2635TD"));
        vehs.add(new Truck("Ten-Foot", 12, "2810", "EJ5KU2435BC"));
    }
    
    private static int getVehicleType(){
    // prompts user for vehicle type (1-Car, 2-SUV, 3-Truck)
    // returns symbolic value CAR, SUV or TRUCK
        
        int selection = 1;  // init
        boolean valid_input = false;
        
        while(!valid_input){
            System.out.print("Enter (1-Car, 2-SUV, 3-Truck): ");
            selection = input.nextInt();
            input.nextLine(); // eat up end of line char
            
            if(selection< 1 || selection > 3)
                System.out.println("* INVALID ENTRY - PLEASE REENTER *");
            else
                valid_input = true;
        }
        
        // return appropriate defined symbolic constant
        if(selection == 1)
            return CAR;
        else
            if(selection == 2)
                return SUV;
            else
                return TRUCK;
    }
    
    private static boolean validVINChars(String vin){
    // returns true if vin contains only A..Z and digits 0..9.
    // returns false otherwise
        
        boolean valid_vin = true;
        char chr;
        int i = 0;
        
        while(i < vin.length() && valid_vin){
            chr = vin.charAt(i);
            
            // check for any non-digits and non-uppercase chars
            if(!Character.isUpperCase(chr) && !Character.isDigit(chr))
                valid_vin = false;
            i = i + 1;
        }
        return valid_vin;
    }
    
    private static boolean validateRentalPeriod(String rental_period) 
            throws InvalidRentalPeriodFormatException {
    // returns true if rental_period formatted as <periodtype><length>
    // where <periodtype> = 'D', 'W', or 'M', <length> an integer value
    // otherwise, return false. Throws InvalidRentalPeriodFormatException
    // if rental_period found improperly formatted
        
        String digits_part;
        boolean error_found = false;

        // verifies that rental period of proper form
        char first_char = rental_period.charAt(0);
        
        if(Character.toUpperCase(first_char) != 'D' &&
           Character.toUpperCase(first_char) != 'W' &&
           Character.toUpperCase(first_char) != 'M')
             error_found = true;
        else
        {
            digits_part = rental_period.substring(1, rental_period.length());
            if(Utilities.containsNonDigit(digits_part))
                error_found = true;
        }
        
        // rental period found invalid
        if(error_found)
            throw new InvalidRentalPeriodFormatException();

        // rental period verified OK
        return true;
    }
    
    private static String getValidVINFromUser(){
    // prompts user for valid VIN (i.e., containing only capital letters and digits).
    // keeps prompting user until a valid VIN entered
        
        Vehicle veh;
        String entered_VIN = ""; // init
        
        boolean valid_entry = false;
        int num_trys = 0;
        
        while(!valid_entry){
            System.out.println("Enter VIN: ");
            entered_VIN = input.next();
            
            if(validVINChars(entered_VIN))
                valid_entry = true;
            else {
                System.out.println("* INVALID VIN CHARACTERS FOUND *");
                System.out.println("(Enter Only Capital Letters and Digits)");
                System.out.println(); // skip line
            }
        }  
        return entered_VIN;
    }
    
    private static String getValidExistingVINFromUser() 
                          throws FailedVINEntryAttemptsException{
    // prompts user for valid VIN (i.e., containing only capital letters and digits).
    // throws a FailedVINEntryAttemptsException if failure to enter a proper
    // VIN for an existing vehicle after three attempts
        
        String VIN = "";  // init
        Vehicle veh;
        boolean found = false;
        int num_trys = 0;
        
        while(!found && num_trys < 3){
            try{
                // get properly formatted VIN from user
                VIN = getValidVINFromUser();

                // retrieve vehicle to verify that exists (otherwise throws exception)
                veh = vehs.getVehicle(VIN);
                found = true;
            }
            catch(VINNotFoundException e){
                System.out.println("* VIN NOT FOUND *");
                System.out.println();  // skip line
                num_trys = num_trys + 1;

                if(num_trys == 3)
                    throw new FailedVINEntryAttemptsException();
            }
        }
        return VIN;
    }    
        
    private static String getValidCreditCard() 
             throws FailedCreditCardEntryAttemptsException{
    // prompts user for a properly formatted credit card (i.e., 16 digits), 
    // and throws a FailedCreditCardEntryAttemptsException if failure to enter a
    // properly formatted rental period after three attempts
        
        boolean valid_entry = false;
        String entered_creditcard = "";  // init
        int num_trys = 0;
    
        while(!valid_entry && num_trys < 3){
            try{
                // input and validate rental period
                System.out.print("Enter credit card number: ");
                entered_creditcard = input.next();
                System.out.println();
                
                Utilities.validateCreditCard(entered_creditcard);
                valid_entry = true;
            }
            catch(InvalidNumberCharsException e){
                System.out.println("* MUST ENTER 16 DIGIT CARD NUMBER *");
                num_trys = num_trys + 1;
                
                // return to main menu after three attempts
                if(num_trys == 3)
                    throw new FailedCreditCardEntryAttemptsException();
            }
            catch(NonDigitFoundException e){
                System.out.println("* NON-DIGIT CHAR FOUND *");
                num_trys = num_trys + 1;
                
                // return to main menu after three attempts
                if(num_trys == 3)
                    throw new FailedCreditCardEntryAttemptsException();
            }
        }
        return entered_creditcard;
    }  
    
    private static String getValidRentalPeriod() throws FailedRentalPeriodEntryException{
    // prompts user for a properly formatted rental period, and returns
    // throws a FailedRentalEntryAttemptsException if failure to enter a
    // properly formatted rental period after three attempts
        
        boolean valid_entry = false;
        String entered_rentalperiod = "";  // init
        int num_trys = 0;
    
        while(!valid_entry && num_trys < 3){
            try{
                // input and validate rental period
                System.out.print("Enter rental period ");
                System.out.println("(e.g., D2-two days, W3-three weeks, M1-one month");
                entered_rentalperiod = input.next();
                System.out.println();
                validateRentalPeriod(entered_rentalperiod);
                valid_entry = true;
            }
            catch(InvalidRentalPeriodFormatException e){
                System.out.println("* INVALID RENTAL PERIOD ENTERED *");
                num_trys = num_trys + 1;
                
                // return to main menu after three attempts
                if(num_trys == 3)
                    throw new FailedRentalPeriodEntryException();
            }
        }
        return entered_rentalperiod;
    }  

    private static Vehicle getReservedVehicleByCreditCard(String credit_card) 
            throws ReservationNotFoundException {
    // returns Vehicle object reserved with provided credit card number
    // if no such vehicle found, throws a ReservationNotFoundException
        
        Vehicle veh = null;
        boolean found = false;
        
        vehs.reset();
        while(vehs.hasNext() && !found){
            veh = vehs.getNext();
            if(veh.isReserved())
                if(veh.getReservation().getCreditCard().equals(credit_card))
                    found = true;
        }    
  
        if(!found)
            throw new ReservationNotFoundException();
        
        return veh;
    }
}
        
    