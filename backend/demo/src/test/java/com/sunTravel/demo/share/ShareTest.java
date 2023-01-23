package com.sunTravel.demo.share;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ShareTest
{

    @Test
    void setFormatOfWords()//set the capital as first letter of the word in sentence
    {
        assertEquals("Taj Samudra Colombo",Share.setFormatOfWords( "taj samudra colombo" )  );
    }

    @Test
    void isValidLocation()
    {
        assertEquals(false,Share.isValidLocation( "colombo" ));
        assertEquals(true,Share.isValidLocation( "Colombo" ));
    }

    @Test
    void isStartAndEndDateValid()
    {
        Date date1=new Date("2023/01/18");
        Date date2=new Date("2023/01/20");
        assertEquals(true,Share.isStartAndEndDateValid( date1,date2 ));//start date is before end date
        assertEquals(false,Share.isStartAndEndDateValid( date2,date1 ));//start date is after end date
        assertEquals(false,Share.isStartAndEndDateValid( date1,date1 ));//start date and end date both are equal

    }
}