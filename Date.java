// Travail pratique 2 : Gestion des stocks d’une pharmacie
// Larry Fotso Guiffo - 202201552 
// Cíntia Dalila Soares - C2791

import java.time.LocalDate;
import java.time.Period;

public class Date implements Comparable<Date>{

    private int day;
    private int month;
    private int year;

    public Date(String d){
        String[] date = d.split("-");
        this.year = Integer.parseInt(date[0]);
        this.month = Integer.parseInt(date[1]);
        this.day = Integer.parseInt(date[2]);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public Date(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public  int getNumODays(Date date){
        LocalDate start = LocalDate.of(this.year, this.month, this.day);
        LocalDate end = LocalDate.of(date.year,date.month,date.day);
        Period period = Period.between(start, end);
        int days = period.getDays();
        return days;
    }

    @Override
    public int compareTo(Date date) {
        if(this.year == date.getYear()){
            if(this.month == date.getMonth()){
                if(this.day == date.getDay()){
                    return 0;
                }else{
                    if(this.day < date.getDay()){
                        return -1;
                    }else{
                        return 1;
                    }
                }
            }else{
                if(this.month < date.getMonth()){
                    return -1;
                }else{
                    return 1;
                }

            }

        }else{
            if(this.year < date.getYear()){
                return -1;
            }else{
                return 1;
            }
        }
    }
    
    @Override
    public String toString(){
        String m;
        String d;
        if(this.month < 10){
            m = "0" + this.month;
        }else{
            m = ""+this.month;
        }
        if(this.day < 10){
            d = "0" + this.day;
        }else{
            d = ""+ this.day;
        }

        return this.year + "-" + m + "-" + d;
    }


    public Date computeDate(int duration) {
        int daysToAdd = duration;
        int newYear = year;
        int newMonth = month;
        int newDay = day;
        
        while (daysToAdd > 0) {
            int daysInCurrentMonth = getDaysInMonth(newYear, newMonth);
            int remainingDays = daysInCurrentMonth - newDay + 1;
            
            if (daysToAdd >= remainingDays) {
                // Move to the next month
                if (newMonth == 12) {
                    newMonth = 1;
                    newYear++;
                } else {
                    newMonth++;
                }
                
                newDay = 1;
                daysToAdd -= remainingDays;
            } else {
                // Add remaining days to the current month
                newDay += daysToAdd;
                daysToAdd = 0;
            }
        }
        
        return new Date(newYear, newMonth, newDay);
    }
    
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    
    private int getDaysInMonth(int year, int month) {
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        
        if (month == 2 && isLeapYear(year)) {
            return 29;
        }
        
        return daysInMonth[month - 1];
    }
    
}
