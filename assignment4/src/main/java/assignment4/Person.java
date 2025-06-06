package assignment4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Person {
    /*
     * Field declarations for "Person" class
     */
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<Date, Integer> demeritPoints;
    private int activeDemeritPoints;
    private boolean isSuspended;
    private final String specialChars = "!@#$%^&*()-_=+\\\\|{};:/?.><~";
    private final String person_file = "people.txt";

    /*
     * Constructos
     */
    // Default constructor, called when a "Person" object is made with no inputted parameters
    public Person() {
        this.personID = null;
        this.firstName = null;
        this.lastName = null;
        this.address = null;
        this.birthdate = null;
        this.demeritPoints = null;
        this.isSuspended = false;
        this.activeDemeritPoints = 0;
    }

    // Parameterized constructor, this allows the fields of an object to have their values assigned at creation
    // It avoids having to use the getter/setter functions when assigning values to an objects fields
    public Person(String personID, String firstName, String lastName, String address, String birthdate) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthdate = birthdate;
        this.demeritPoints = null;
        this.isSuspended = false;
        this.activeDemeritPoints = 0;
    }

    /*
     * Functions used to write/edit to the "people.txt" file
     */
    // Wipes an existing file, or creates a new one, through passing the name of the file to PrintWriter
    // PrintWriter does this by overwriting the file with empty content, as seen with this line of code --> pw.write("")
    public static void resetTextFile(String filename) {
        try {
            File file = new File(filename);
            PrintWriter pw = new PrintWriter(file);
            pw.write("");
            pw.close();
        }
        catch (IOException e) {
            System.out.println("Error wiping file...\n" + e.getMessage());
        }
    }
    
    // Appends this person's basic info to the txt file through means of the FileWriter
    private void saveToTextFile(String filename) {
        try {
            FileWriter fw = new FileWriter(filename, true);
            fw.write(personID + "," + firstName + "," + lastName + "," + address + "," + birthdate + "\n");
            fw.write("Demerit points (" + personID + "): " + Integer.toString(activeDemeritPoints) + "\n");
            fw.write("Suspension status (" + personID + "): " + Boolean.toString(isSuspended) + "\n");
            
            fw.write("\n");
            fw.close();
        }
        catch (IOException e) {
            System.out.println("Error writing to file...\n" + e.getMessage());
        }
    }

    // Overloaded "saveToTextFile()" function to accepts "mode" and "status" as arguments
    // This oveloaded version is used in relation to "addDemeritPoints()" function
    private void saveToTextFile(String filename, String mode, String status) {
        if (mode == "demerit") {
            try {
                FileWriter fw = new FileWriter(filename, true);
                fw.write("\n"+ personID + "," + firstName + " " + lastName + ", " + "Demerit Points:\n");

                for (HashMap.Entry<Date, Integer> entry : demeritPoints.entrySet()) {
                    String dateStr = dateToString(entry.getKey());
                    fw.write(dateStr + "," + entry.getValue() + "\n");
                }
                    
                fw.write(status + "\n");
                fw.close();
            }
            catch (IOException e) {
                System.out.println("Error writing to file...\n" + e.getMessage());
            }
        }
    }

    // Replaces the first line containing "target" with "replacement" in the txt file
    // It does this by:
        // Using an ArrayList to store all lines from the file in memory, allowing modification of any line before writing back
        // A BufferedReader then reads each line from the file, and BufferedWriter writes the updated list of lines back to the file
    public void replaceLineInFile(String filename, String target, String replacement) {
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            
            // Read each line and add to ArrayList, replacing the target line as needed
            while ((line = reader.readLine()) != null) {
                if (line.contains(target)) {
                    lines.add(replacement);
                } else {
                    lines.add(line);
                }
            }
            reader.close();

            // Write all, possibly modified, lines back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error replacing line in file: " + e.getMessage());
        }
    }

    // Checks if this person's ID already exists in the txt file by reading each line with the BufferedReader
    // Only checks the field "personID" since the ID must be unique for each person
    private boolean personAlreadyInTextFile(String filename) {
        try {
            BufferedReader fr = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = fr.readLine()) != null) {
                String[] lineList = line.split(",");

                if (personID.equals(lineList[0])) {
                    return true;
                }
            }
            fr.close();
        }
        catch (IOException e) {
            System.out.println("Error reading file...\n" + e.getMessage());
        }
        return false;
    }

    /*
     * "addPerson()", this section holds any relevant code pertaining to this function
     */
    // The main "addPerson()" function
    // This function calls all relevant helper functions to confirm the person's detials are correct according to the specification
    // It then writes the person's details to the "person.txt" file, while also displaying the details to the person in the terminal
    // If the person's details are not successfully added, the reason will be displayed in the terminal
    public boolean addPerson() {
        System.out.println("\n===\tADDING " + firstName + " " + lastName + "\t===\n");
        
        if (validatePersonID(personID) && validateAddress(address) && validateBirthdate(birthdate)) {
            if (personAlreadyInTextFile(person_file)) {
                System.out.println("Person Not Added Successful\t:(");
                System.out.println("This person is already in \"people.txt\" (duplicate personID warning).");
                return false;
            } 
            else {
                saveToTextFile(person_file);
                System.out.println("Person Added Successful!\t:)");
                return true;
            }
        }
        System.out.println("Person Not Added Successful\t:(");
        return false;  
    }

    // Checks whether the person's "personID" alligns with the requirements outlined in the assignment specification
    // If it doesn't, it prints a message displaying to the person the exact rule that was violated
    public boolean validatePersonID(String inputID) {
        // PersonID has an incorrect length, thus return false
        if (inputID.length() != 10) {
            return false;
        }
        // PersonID does not have a valid set of starting numbers, thus return false
        if (!startsWithCorrectNumbers(inputID)) {
            return false;
        }
        // PersonID has the incorrect amount of special characters, thus return false
        if (!hasSpecialCharacters(inputID)) {
            return false;
        }
        // PersonID is missing capital letters at the end, thus return false
        if (!hasCapitalLettersAtEnd(inputID)) {
            return false;
        }
        
        // Otherwise, PersonID meets all required conditions and thus can return true
        return true;
    }

    // Checks whether the personID starts with two valid numbers
    // Both numbers have to be between 2 and 9 (inclusive)
    private boolean startsWithCorrectNumbers(String inputString) {
        int firstNumber = Character.getNumericValue(inputString.charAt(0));
        int secondNumber = Character.getNumericValue(inputString.charAt(1));

        return (firstNumber >= 2 && firstNumber <= 9) && (secondNumber >= 2 && secondNumber <= 9);
    }
    
    // Checks whether there are at least 2 special characters between index 3 and 8 (inclusive)
    private boolean hasSpecialCharacters(String inputString) {
        int count = 0;
        for (int i = 2; i < 8; i++) {
            if (isSpecialChar(inputString.charAt(i))) {
                count++;
            }
        }

        if (count >= 2) {
            return true;
        }
        return false;
    }
    
    // Checks whether the input character is in the special characters list
    private boolean isSpecialChar(char c) {
        for (int i = 0; i < specialChars.length(); i++) {
            if (c == specialChars.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    // Checks whether the last two characters are capital letters
    private boolean hasCapitalLettersAtEnd(String inputString) {
        int length = inputString.length();

        return (Character.isUpperCase(inputString.charAt(length - 1)) && Character.isUpperCase(inputString.charAt(length - 2)));
    }    

    // Checks whether the address is in the correct format
    public boolean validateAddress(String inputAddress) {
        List<String> addressList = Arrays.asList(inputAddress.split("\\|"));

        // Address is in the incorrect format, thus return false
        if (addressList.size() != 5) {
            return false;
        }

        // Address meets all required conditions, return true
        if (addressList.get(3).equals("Victoria") && addressList.get(4).equals("Australia") 
            && addressList.size() == 5 && isNumeric(addressList.get(0))) {
            return true;
        // Otherwise, return false
        } else {
            return false;
        }
    }

    // Checks whether an input string is a numeric value
    private boolean isNumeric(String inputNumber) {
        try {
            Integer.parseInt(inputNumber);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // Checks whether the person's birthday is in the correct format (dd-mm-yyyy)
    // It also confirms that the inputted values are reasonable, such as inputting a birth year past 2025
    public boolean validateBirthdate(String inputBirthDate) {
        String[] birthdateList = inputBirthDate.split("-");
        String currDate = getCurrentDateString();
        int currYear = Integer.parseInt(currDate.substring(currDate.length() - 4));
        
        // Birthdate has the wrong format, thus return false
        if (inputBirthDate.length() != 10 || birthdateList.length != 3) {
            return false;
        }
        // Birthdate day is out of acceptable bounds, thus return false
        if (Integer.parseInt(birthdateList[0]) <= 0 || Integer.parseInt(birthdateList[0]) > 31) {
            return false;  
        }
        // Birthdate month is out of acceptable bounds, thus return false
        if (Integer.parseInt(birthdateList[1]) <= 0 || Integer.parseInt(birthdateList[1]) > 12) {
            return false;
        }
        // Birthdate year is greater than the current year, thus return false
        if ((birthdateList[2]).length() != 4 || Integer.parseInt(birthdateList[2]) > currYear) {
            return false;
        }
        
        System.out.println("Birthday is valid\t:)");
        return true;
    }

    /*
     * "updatePersonalDetails()", this section holds any relevant code pertaining to this function
     */
    public boolean updatePersonalDetails(String newPersonID, String newFirstName, String newLastName, 
                                         String newAddress, String newBirthdate) {
        // Checks if a person wants to update their "birthDate" details
        if (newBirthdate != null) {
            // If they do want this detail updated, then no other details can be changed
            System.out.println("As birthdate details is desired to be updated, then no other details can be changed");    
            
            // Check if their "newBirthdate" is valid according to the conditions defined in the "addPerson()" function
            // If so, update their details in the "person.txt" file, and thus make "updatePersonalDetails()" return true      
            boolean validBirthDate = validateBirthdate(newBirthdate);
            if (validBirthDate) {
                String oldDetails = personID + "," + firstName + "," + lastName + "," + address + "," + birthdate;
                String updatedBirthdate = personID + "," + firstName + "," + lastName + "," + address + "," + newBirthdate;
                
                replaceLineInFile(person_file, oldDetails, updatedBirthdate);
                birthdate = newBirthdate;
                return true;
            // Otherwise, "updatePersonalDetails()" will return false and the details in "person.txt" file will remain unchanged
            } else {
                return false; 
            }
        }

        // These two variables help check whether a person desires to update their "address" details
        boolean updateAddressNull = false; 
        boolean canUpdateAddress = false;
        // If a person does desire to have their "address" details update, then they must meet all relevant conditions
        // Assuming they meet these conditions:
            // Set "canUpdateAddress" to equal true
            // Otherwise leave it as false
        if (newAddress != null) {
            boolean validAddress = validateAddress(newAddress);
            int age = calculateCurrentAge();

            if (validAddress && age > 18) {
                canUpdateAddress = true;
            }
        // If the person doesn't want their "address" details changed, then:
            // Set "updateAddressNull" to equal true
            // Set "canUpdateAddress" to equal true
        } else {
            updateAddressNull = true;
            canUpdateAddress = true;
        }
        
        // These two variables help check whether a person desires to update their "personID" details
        boolean updatePersonIDNull = false;
        boolean canUpdatePersonID = false;
        // If a person does desire to have their "personID" details update, then they must meet all relevant conditions
        // Assuming they meet these conditions:
            // Set "canUpdatePersonID" to equal true
            // Otherwise leave it as false
        if (newPersonID != null) {
            boolean validPersonID = validatePersonID(newPersonID);
            boolean idCanBeChanged = canIdBeChanged();

            if (validPersonID && idCanBeChanged) {
                canUpdatePersonID = true;
            }
        // If the person doesn't want their "personID" details changed, then:
            // Set "updateAddressNull" to equal true
            // Set "canUpdateAddress" to equal true
        } else {
            updatePersonIDNull = true;
            canUpdatePersonID = true;
        }

        // If the person has met the conditions to update their "address" or "personID" details or both, do the following
        if (canUpdateAddress && canUpdatePersonID) {
            // If the person can update their "address" details, and it's not null, then update their details
            if (canUpdateAddress && !updateAddressNull) {
                String oldDetails = personID + "," + firstName + "," + lastName + "," + address + "," + birthdate;
                String updatedAddress = personID + "," + firstName + "," + lastName + "," + newAddress + "," + birthdate;

                replaceLineInFile(person_file, oldDetails, updatedAddress);
                address = newAddress;                
            }
            // If the person can update their "personID" details, and it's not null, then update their details
            if (canUpdatePersonID && !updatePersonIDNull) {
                String oldDetails = personID + "," + firstName + "," + lastName + "," + address + "," + birthdate;
                String updatedID = newPersonID + "," + firstName + "," + lastName + "," + address + "," + birthdate;

                replaceLineInFile(person_file, oldDetails, updatedID);
                personID = newPersonID;                
            }
        // Otherwise, make "updatePersonalDetails()" return false
        } else {
            return false;
        }

        // If this line is reached, then that means all other conditions have been met and thus the person's first name can be updated
        // Unless, they do not desire to have their "firstName" details updated
        if (newFirstName != null) {
            String oldDetails = personID + "," + firstName + "," + lastName + "," + address + "," + birthdate;
            String updatedFirstName = personID + "," + newFirstName + "," + lastName + "," + address + "," + birthdate;

            replaceLineInFile(person_file, oldDetails, updatedFirstName);
            firstName = newFirstName;
        }

        // If this line is reached, then that means all other conditions have been met and thus the person's last name can be updated
        // Unless, they do not desire to have their "lastName" details updated
        if (newLastName != null) {
            String oldDetails = personID + "," + firstName + "," + lastName + "," + address + "," + birthdate;
            String updatedLastName = personID + "," + firstName + "," + newLastName + "," + address + "," + birthdate;

            replaceLineInFile(person_file, oldDetails, updatedLastName);
            lastName = newLastName;
        }

        return true;
    }

    //
    private int calculateCurrentAge() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        LocalDate currentDate = LocalDate.parse(getCurrentDateString(), formatter);
        LocalDate dateOfBirth = LocalDate.parse(birthdate, formatter);

        int age = Period.between(dateOfBirth, currentDate).getYears();
        return age;
    }

    //
    private boolean canIdBeChanged() {
        int firstDigit = Integer.parseInt(personID.substring(0, 1));

        if (firstDigit % 2 != 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * "addDemeritPoints()", this section holds any relevant code pertaining to this function
     */
    // The main "addDemeritPoints()" function 
    // This function calls all relevant helper functions to add demerit points, and in-turn see whether their licence is suspended
    // It then writes to the terminal whether the demerit points were successuflly added, and if the person's licence is suspended
    // In-turn, the "people.txt" file is updated to reflect this information
    public String addDemeritPoints(int inPoints, String inDateStr) {   
        Date currDate = stringToDate(inDateStr);
        String currDateStr = dateToString(currDate);

        String oldDemerit = "Demerit points (" + personID + "): " + Integer.toString(activeDemeritPoints);
        String oldStatus = "Suspension status (" + personID + "): " + Boolean.toString(isSuspended);

        if (this.demeritPoints == null) {
            this.demeritPoints = new HashMap<>();
        }

        // Condition 1: The offense date should be in the format "dd-mm-yyyy"
        if (!isValidOffenseDate(inDateStr)) {
            System.out.println("Input offense date is not in dd-mm-yyyy\tFailed");
            saveToTextFile(person_file, "demerit", "Failed");
            return "Failed";
        }

        // Condition 2: The demerit points must be a whole number between 1-6 (inclusive)
        if (inPoints < 1 || inPoints > 6) {
            System.out.println("Input demerit points must be between 1 to 6\tFailed");
            saveToTextFile(person_file, "demerit", "Failed");
            return "Failed";
        }
        else {
            System.out.println("Input demerit points is valid");
        }
        this.demeritPoints.put(currDate, inPoints);

        // Condition 3:
            // If the person is 21 or older, they have a limit of 12 demerit points within two years without licence suspension
            // Else, if person younger than 21, they have a limit of 6 demerit points within two years without licence suspension
        if (calculateAgeAtViolation(currDateStr) >= 21 && demeritWithinTwoYears() > 12) {
            String newDemerit = "Demerit points (" + personID + "): " + Integer.toString(activeDemeritPoints);
            replaceLineInFile(person_file, oldDemerit, newDemerit);

            isSuspended = true;
            String newStatus = "Suspension status (" + personID + "): " + Boolean.toString(isSuspended);
            replaceLineInFile(person_file, oldStatus, newStatus);

            return "Success";
        }
        else if (calculateAgeAtViolation(currDateStr) < 21 && demeritWithinTwoYears() > 6) {
            String newDemerit = "Demerit points (" + personID + "): " + Integer.toString(activeDemeritPoints);
            replaceLineInFile(person_file, oldDemerit, newDemerit);            

            isSuspended = true;
            String newStatus = "Suspension status (" + personID + "): " + Boolean.toString(isSuspended);
            replaceLineInFile(person_file, oldStatus, newStatus);

            return "Success";
        }
        else {
            String newDemerit = "Demerit points (" + personID + "): " + Integer.toString(activeDemeritPoints);
            replaceLineInFile(person_file, oldDemerit, newDemerit);

            return "Failed";
        }
    }

    // Checks if the inputted offense date is valid by verifying it is not set in the future
    // Uses DateTimeFormatter and LocalDate to parse and validate the offense date string
    public boolean isValidOffenseDate(String inDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDate parsedDate = LocalDate.parse(inDateStr, formatter);

            if (parsedDate.isAfter(LocalDate.now())) {
                System.out.println("This date is in the future");
                return false;
            } else {
               return true; 
            }
        }
        catch (DateTimeParseException e) {
            return false;
        }
    }

    // This finds the person's age at the date of violation
    // It does by converting the birthdate and date of violation strings using DateTimeFormatter and LocalDate
    // It then finds the difference in years between the birthdate and inputted date using Period
    private int calculateAgeAtViolation(String dateOfViolation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        LocalDate violationDate = LocalDate.parse(dateOfViolation, formatter);
        LocalDate dateOfBirth = LocalDate.parse(this.birthdate, formatter);

        int age = Period.between(dateOfBirth, violationDate).getYears();
        return age;
    }

    // Calculates and return sum of demerit point within last 2 years based on the most recent offence date
    // This is done by iterating through the "demeritPoints" HashMap, converting Date to LocalDate for comparison
    public int demeritWithinTwoYears() {
        int totalPoints = 0;

        if (demeritPoints == null) {
            return totalPoints;
        }

        LocalDate curr = stringToLocalDate(getCurrentDateString());
        LocalDate twoYearsAgo = curr.minus(Period.ofYears(2));
        
        for (HashMap.Entry<Date, Integer> entry : demeritPoints.entrySet()) {
            String dateStr = dateToString(entry.getKey());
            LocalDate date = stringToLocalDate(dateStr);
            
            if (!date.isBefore(twoYearsAgo)) {
                totalPoints += entry.getValue();
            }
        }
        activeDemeritPoints = totalPoints;

        return totalPoints;
    }

    // Converts a String representation of a date to a Date object
    // This is done by using DateTimeFormatter and LocalDate to convert a date String to a Date object
    private Date stringToDate(String inDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate formattedDate = LocalDate.parse(inDateStr, formatter);
        
        return Date.from(formattedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Converts a Date object to a String representation of that date
    // This is done by using SimpleDateFormat to format a Date object as a String in "dd-MM-yyyy" format
    private String dateToString(Date inDate) {
        SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");
        
        return SDF.format(inDate);
    }

    // Converts String representation of a date to a LocalDate object
    // This is done by using DateTimeFormatter and LocalDate to parse a date string into a LocalDate object
    private LocalDate stringToLocalDate(String inDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        return LocalDate.parse(inDateStr, formatter);
    }

    // Returns the current date as a string in the format "dd-MM-yyyy"
    // Uses DateTimeFormatter and LocalDate to get and format today's date
    public String getCurrentDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        return LocalDate.now().format(formatter);
    }
}
