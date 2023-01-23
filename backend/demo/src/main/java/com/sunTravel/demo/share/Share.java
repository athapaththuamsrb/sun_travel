package com.sunTravel.demo.share;

import com.sunTravel.demo.dto.SearchDto;

import java.util.Date;

public class Share
{    public static String setFormatOfWords(String message){
        message=message.toLowerCase();
        char[] charArray = message.toCharArray();
        boolean foundSpace = true;
        for(int i = 0; i < charArray.length; i++) {
            // if the array element is a letter
            if(Character.isLetter(charArray[i])||Character.isDigit(charArray[i])) {
                // check space is present before the letter
                if(foundSpace) {
                    // change the letter into uppercase
                    charArray[i] = Character.toUpperCase(charArray[i]);
                    foundSpace = false;
                }
            }
            else {
                // if the new character is not character
                foundSpace = true;
            }
        }
        // convert the char array to the string
        return String.valueOf(charArray);
    }
    public static boolean  isValidLocation( String location )
    {
        if(location.equals("Colombo")||location.equals("Matale")||location.equals("Kegalle")||location.equals("Ratnapura")||location.equals("Moneragala")||location.equals("Badulla")||location.equals("Polonnaruwa")||location.equals("Anuradhapura")||location.equals("Puttalam")||location.equals("Kurunegala")||location.equals("Trincomalee")||location.equals("Ampara")||location.equals("Batticaloa")||location.equals("Mullaitivu")||location.equals("Vavuniya")||location.equals("Mannar")||location.equals("Kilinochchi")||location.equals("Matara")||location.equals("Hambantota")||location.equals("Jaffna")||location.equals("Nuwara Eliya")||location.equals("Galle")||location.equals("Kandy")||location.equals("Kalutara")||location.equals("Gampaha")){
            return true;
        }
        else{return false;}
    }
    public static boolean isStartAndEndDateValid( Date start, Date end )
    {
        if(start.compareTo(end) > 0||start.compareTo(end) == 0) {//start date come after end date
            return false;
        } else if(start.compareTo(end) < 0) {//start date come before end date
            return true;
        } else {
            return false;
        }
    }
}
