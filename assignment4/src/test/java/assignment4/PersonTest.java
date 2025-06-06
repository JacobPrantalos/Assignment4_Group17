package assignment4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PersonTest {
    /*
     * The below test functions are all pertaining to the "Person" function "addPerson()"
     */
    // These tests are for the validation of the "personID" field
    @Test
    public void testPersonID() {
        Person person = new Person(); 
        boolean result;
        
        // Tests a valid ID (Refer to assignment specification) 
        result = person.validatePersonID("56s_d%&fAB");
        assertEquals(true, result);
        
        // Tests an invalid ID (Second character is outside the specified number range of 2-9)
        result = person.validatePersonID("60s_d%&fAB");
        assertEquals(false, result);

        // Tests an invalid ID (The ID is not exactly 10 characters long)
        result = person.validatePersonID("52s_d%&fA");
        assertEquals(false, result);

        // Tests an invalid ID (The last letter is not capitalized)
        result = person.validatePersonID("52s_d%&fAl");
        assertEquals(false, result);
    }

    // These tests are for the validation of the "address" field
    @Test
    public void testAddress() {
        Person person = new Person();
        boolean result;

        // Tests a valid address (Street Number|Street|City|State|Country)
        result = person.validateAddress("1|Downing Street|Bendigo|Victoria|Australia");
        assertEquals(true, result);

        // Tests an invalid address (State|Country != Victoria|Australia)
        result = person.validateAddress("10|Agen Lane|Morrabin|Toronto|Canada");
        assertEquals(false, result);

        // Tests an invalid address (Uses "," as the format delimiter instead of "|")
        result = person.validateAddress("10,Agen Lane,Morrabin,Victoria,Australia");
        assertEquals(false, result);

        // Tests an invalid address (The street number must numeric, not a string)
        result = person.validateAddress("ten|Agen Lane|Morrabin|Victoria|Australia");
        assertEquals(false, result);
    }

    // These tests are for the validation of the "birthdate" field
    @Test
    public void testBirthday() {
        Person person = new Person();
        boolean result;

        // Tests a valid birthdate (dd-mm-yyyy)
        result = person.validateBirthdate("04-09-1901");
        assertEquals(true, result);

        // Tests an invalid birthdate (The year must be 4 digits)
        result = person.validateBirthdate("4-09-19");
        assertEquals(false, result);

        // Tests an invalid birthdate (A birth year of 2056 is an unreasonable value)
        result = person.validateBirthdate("01-01-2056");
        assertEquals(false, result);

        // Tests an invalid birthdate (Uses "/" as the format delimiter instead of "-")
        result = person.validateBirthdate("01/01/2000");
        assertEquals(false, result);
    }
    
    /*
     * The below test functions are all pertaining to the "Person" function "addDemeritPoints()"
     */
    // These tests are designed to validate the date of offense
    @Test
    public void testisValidOffenseDate() {
        Person person = new Person();
        boolean result;
        
        // Tests a valid offense date (dd-mm-yyyy)
        result = person.isValidOffenseDate("12-04-2024");
        assertEquals(true, result);

        // Tests an invalid offense date (The day is not within the accepted range)
        result = person.isValidOffenseDate("34-02-2020");
        assertEquals(false, result);

        // Tests an invalid offense date (An offense year of 2049 is an unreasonable value)
        result = person.isValidOffenseDate("19-08-2049");
        assertEquals(false, result);

        // Tests an invalid offense date (Not in the correct format of dd-mm-yyyy)
        result = person.isValidOffenseDate("04-2004-28");
        assertEquals(false, result);

        // Tests an invalid offense date (Not in the correct format of dd-mm-yyyy)
        result = person.isValidOffenseDate("1-4-2000");
        assertEquals(false, result);
    }

    // This test is for sum of demerit points within the two year boundary
    @Test
    public void testDemeritWithinTwoYears() {
        int result;
        Person person = new Person("56s_d%&fAB", "Daniel", "Zhange", 
                                    "1|Downing Street|Bendigo|Victoria|Australia", "14-05-2005");
        
        // Valid sum
        person.addDemeritPoints(3, "11-06-2024");
        person.addDemeritPoints(4, "09-11-2023");
        result = person.demeritWithinTwoYears();

        assertEquals(7, result);
    }
}
