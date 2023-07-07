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
        return this.year + "-" + this.month + "-" + this.day;
    }
}
