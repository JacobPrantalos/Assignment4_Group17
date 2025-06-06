package assignment4;

// This file is used to ensure all correct information is being passed both to the terminal and "people.txt" file
public class Main {
    public static void main(String[] args) {

        // Reset "people.txt" file/create "people.txt" file for testing
        Person.resetTextFile("people.txt");

        /*
         * These should all return valid
         */
        // Person 1
        Person johnSmith = new Person("56s_d%&fAB", "John", "Smith", 
            "1|Downing Street|Bendigo|Victoria|Australia", "02-06-1999");
        johnSmith.addPerson();

        // Person 2
        Person bobJane = new Person("57s_d%&fAB", "Bob", "Jane", 
            "1|T-Mart|Melbourne|Victoria|Australia", "01-05-1984");
        bobJane.addPerson();

        // Person 3
        Person aliceNewman = new Person("58s_d%&fAB", "Alice", "Newman", 
            "18|Ocean Avenue|Geelong|Victoria|Australia", "11-09-2001");
        aliceNewman.addPerson();

        /*
         * These should all return invalid
         */
        // Person 4
        Person georgeBush = new Person("5*s_d%&fAB", "George", "Bush", 
            "7|Vegas Strip|Las Vegas|Victoria|Australia", "04-01-1987");
        georgeBush.addPerson();

        // Person 5
        Person albertPerkins = new Person("52s_d%&fAB", "Albert", "Perkins", 
            "10|Agen Lane|Morrabin|Victoria|Canada", "04-09-1901");
        albertPerkins.addPerson();

        // Person 6
        Person daveLincoln = new Person("59s_d%&fAB", "Dave", "Lincoln", 
            "197|Royale Drive|Melbourne|Victoria|Australia", "04-01-2056");
        daveLincoln.addPerson();

        johnSmith.addDemeritPoints(6, "22-03-2024");
        johnSmith.addDemeritPoints(6, "22-04-2024");
        johnSmith.addDemeritPoints(1, "22-05-2024");

        // johnSmith.updatePersonalDetails(null, null, null, null, "14-05-2005");
        // String updatedAddress = "2|Hanslow Way|Melbourne|Victoria|Australia";
        // johnSmith.updatePersonalDetails(null, null, null, updatedAddress, null);
        //johnSmith.updatePersonalDetails("66s_d%&fAB", null, null, null, null);
        //johnSmith.updatePersonalDetails(null, "Harry", "Daniels", null, null);
    }
}