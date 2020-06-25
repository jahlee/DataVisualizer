/**
 * Enum of the months of the year
 * This year (2020) is a leap year, so February has 29 days instead of 28
 */
public enum Months
{
    Jan(31), Feb(29), Mar(31), Apr(30), May(31), Jun(30), Jul(31), Aug(31), Sep(30), Oct(31), Nov(30), Dec(31);

    private int day;

    Months(int i)
    {
        this.day = i;
    }

    // returns number of days in the month
    public int getDay() {
        return day;
    }
}