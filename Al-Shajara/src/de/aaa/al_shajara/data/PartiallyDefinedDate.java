package de.aaa.al_shajara.data;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class PartiallyDefinedDate implements Comparable {

	/** Not the actual century, but rather the prefix of the year. So for 2012 -> 20 */
	private Integer century;
	/** Values from 0 to 9. So for 2012 -> 1 */
	private Integer decadeInCentury;
	/** Values from 0 to 9. */
	private Integer yearInDecade;
	/** Values from 1 to 12 */
	private Integer monthInYear;
	/** Values from 1 to 31 */
	private Integer dayInMonth;
	/** Values from 0 to 23 */
	private Integer hour;
	/** Values from 0 to 59 */
	private Integer minute;
	/** Values from 0 to 59 */
	private Integer second;
	/** Values from 0 to 999 */
	private Integer millies;

	private static int[] monthLengths = new int[]{31,28,31,30,31,30,31,31,30,31,30,31};
	
	/**
	 * 
	 * @param month values from 1 to 12
	 * @return the length of the month in days (Gregorian calendar), taking into account leap years for february
	 */
	private static int getMonthLength(int month, int year){
		if (month<1 || month>12)
			return -1;
		if (month==2){
			GregorianCalendar cal = new GregorianCalendar();
			boolean isLeap = cal.isLeapYear(year);
			if (isLeap)
				return 29;
			return 28;
		}
		return monthLengths[month-1];
	}
	
	/**
	 * Converts a Calendar object to a PartiallyDefinedDate with all fields defined
	 * @param date
	 */
	public PartiallyDefinedDate (Calendar date){
		this.century = date.get(Calendar.YEAR)/100;
		this.decadeInCentury = (date.get(Calendar.YEAR)-this.century*100)/10;
		this.yearInDecade = (date.get(Calendar.YEAR)-this.century*100-this.decadeInCentury*10);
		this.monthInYear = date.get(Calendar.MONTH) + 1;
		this.dayInMonth = date.get(Calendar.DAY_OF_MONTH);
		this.hour = date.get(Calendar.HOUR_OF_DAY);
		this.minute = date.get(Calendar.MINUTE);
		this.second = date.get(Calendar.SECOND);
		this.millies = date.get(Calendar.MILLISECOND);
	}
	
	private PartiallyDefinedDate(){
		
	}
	
	public PartiallyDefinedDate(Integer year, Integer monthInYear, Integer dayInMonth){
		this(year,monthInYear,dayInMonth,null,null,null,null);
	}

	public PartiallyDefinedDate(Integer year, Integer monthInYear, Integer dayInMonth, Integer hour, Integer minute, Integer second, Integer millies){
		if (year!=null){
			this.century = year/100;
			this.decadeInCentury = (year-(100*century))/10;
			this.yearInDecade = year-(100*century)-(10*decadeInCentury);
		}
		else{
			this.century=null;
			this.decadeInCentury=null;
			this.yearInDecade=null;
		}
		this.monthInYear = monthInYear;
		this.dayInMonth = dayInMonth;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.millies = millies;
	}

	public String toString(Locale locale){
		if (locale.getLanguage().equals("de")){
			return germanString();
		}
		//Default:
		return germanString();
	}

	private String germanString() {
		String res = "";
		if (century!=null)res+=century;
		else res+="xx";
		if (decadeInCentury!=null)res+=decadeInCentury;
		else res+="x";
		if (yearInDecade!=null)res+=yearInDecade;
		else res+="x";
		if (monthInYear!=null)res=monthInYear + "." + res;
		else if (dayInMonth!=null){
			res = "xx." + res;//only show placeholders for month when day is defined
		}
		if (dayInMonth!=null){
			res = dayInMonth + "." + res;
		}
		else if (monthInYear!=null)res ="xx." + res;//only show placeholders for day if month is defined
		if (hour==null && minute==null && second==null && millies==null)
			return res;
		else {
			res += " ";
			if (hour!=null)res += hour;
			else res += "xx";
			res+=":";
			if (minute!=null)res+=minute;
			else res+="xx";
			if (second!=null){
				res+=":" + second;
				if (millies!=null)
					res+="," + millies;
			}
		}
		return res;
	}

	public String xmlString() {
		String res = "";
		if (century!=null)res+=century;
		else res+="xx";
		if (decadeInCentury!=null)res+=decadeInCentury;
		else res+="x";
		if (yearInDecade!=null)res+=yearInDecade;
		else res+="x";
		if (monthInYear!=null)res+="-" + numberString(monthInYear);
		else if (dayInMonth!=null){
			res += "-xx";//only show placeholders for month when day is defined
		}
		if (dayInMonth!=null){
			res += "-" + numberString(dayInMonth);
		}
		else if (monthInYear!=null)res +="-xx";//only show placeholders for day if month is defined
		if (hour==null && minute==null && second==null && millies==null)
			return res;
		else {
			res += " ";
			if (hour!=null)res += numberString(hour);
			else res += "xx";
			res+=":";
			if (minute!=null)res+=numberString(minute);
			else res+="xx";
			if (second!=null){
				res+=":" + numberString(second);
				if (millies!=null)
					res+="," + millies;
			}
		}
		return res;
	}
	
	/**
	 * Appends a leading zero to numbers 1-9
	 * @param number
	 * @return
	 */
	private String numberString(Integer number){
		if (number<10 && number>0)
			return "0" + String.valueOf(number);
		return String.valueOf(number);
	}

	public static PartiallyDefinedDate parse(String dateString) throws ParseException {
		if (dateString==null || dateString.length()==0)
			return null;
		if (dateString.length()<4)
			throw new ParseException("Date String is too short:" + dateString, 0);
		PartiallyDefinedDate date = new PartiallyDefinedDate();
		date.century = parseInt(dateString.substring(0, 2));
		date.decadeInCentury = parseInt(dateString.substring(2, 3));
		date.yearInDecade = parseInt(dateString.substring(3, 4));
		if (dateString.length()==4){
			return date;
		}
		if (dateString.charAt(4)!='-')
			throw createParseException('-',dateString.charAt(4),4);
		if (dateString.length()<7){
			throw new ParseException("Date String ended unexpectedly:" + dateString, dateString.length()-1);
		}
		date.monthInYear = parseInt(dateString.substring(5, 7));
		if (dateString.length()==7)
			return date;
		if (dateString.charAt(7)!='-')
			throw createParseException('-',dateString.charAt(7),7);
		if (dateString.length()<10){
			throw new ParseException("Date String ended unexpectedly:" + dateString, dateString.length()-1);
		}
		date.dayInMonth = parseInt(dateString.substring(8, 10));
		if (dateString.length()==10)
			return date;
		if (dateString.charAt(10)!=' ')
			throw createParseException(' ', dateString.charAt(10), 10);
		if (dateString.length()<16)
			throw new ParseException("Date String ended unexpectedly:" + dateString, dateString.length()-1);
		date.hour = parseInt(dateString.substring(11, 13));
		if (dateString.charAt(13)!=':')
			throw createParseException(':', dateString.charAt(13), 13);
		date.minute = parseInt(dateString.substring(14, 16));
		return date;
	}
	
	private static ParseException createParseException(char expected, char found, int pos){
		return new ParseException("Excpected '" + expected + "' but found " + found + " at position " + pos, pos);
	}

	private static Integer parseInt(String nr){
		try {
			return Integer.parseInt(nr);
		}
		catch(NumberFormatException e){
			return null;
		}
	}
	
	@Override
	public String toString(){
		return toString(Locale.GERMAN);
	}

	@Override
	public int compareTo(Object other) {
		PartiallyDefinedDate o = (PartiallyDefinedDate)other;
		if (o==null){
			return -1;
		}
		int compare;
		compare = compareInts(this.century, o.century);
		if (compare!=0)
			return compare;
		compare = compareInts(this.decadeInCentury, o.decadeInCentury);
		if (compare!=0)
			return compare;
		compare = compareInts(this.yearInDecade, o.yearInDecade);
		if (compare!=0)
			return compare;
		compare = compareInts(this.monthInYear, o.monthInYear);
		if (compare!=0)
			return compare;
		compare = compareInts(this.dayInMonth, o.dayInMonth);
		if (compare!=0)
			return compare;
		compare = compareInts(this.hour, o.hour);
		if (compare!=0)
			return compare;
		compare = compareInts(this.minute, o.minute);
		if (compare!=0)
			return compare;
		compare = compareInts(this.second, o.second);
		if (compare!=0)
			return compare;
		compare = compareInts(this.millies, o.millies);
		if (compare!=0)
			return compare;
		return 0;
	}
	
	private int compareInts(Integer thisValue, Integer otherValue){
		if (thisValue==null && otherValue==null)
			return 0;
		if (thisValue!=null && otherValue==null)
			return 1;
		if (thisValue==null && otherValue!=null)
			return -1;
		if (thisValue<otherValue)
			return -1;
		if (thisValue==otherValue)
			return 0;
		return 1;
	}

	Integer getCentury() {
		return century;
	}

	Integer getDecadeInCentury() {
		return decadeInCentury;
	}

	Integer getYearInDecade() {
		return yearInDecade;
	}
	
	public Integer getYear(){
		if (century!=null && decadeInCentury!=null && yearInDecade!=null){
			return 100*century + 10*decadeInCentury + yearInDecade;
		}
		return null;
	}

	public Integer getMonthInYear() {
		return monthInYear;
	}

	public Integer getDayInMonth() {
		return dayInMonth;
	}

	public Integer getHour() {
		return hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public Integer getSecond() {
		return second;
	}

	public Integer getMillies() {
		return millies;
	}

	/**
	 * Calculates the difference between the two dates as far as they are defined
	 * c
	 * @param date
	 * @return
	 */
	public PartiallyDefinedDate getDifference(PartiallyDefinedDate date) {
		PartiallyDefinedDate difference = substract(date);
		difference.resolveNegativeValues(this.monthInYear);
		return difference;
	}

	private PartiallyDefinedDate substract(PartiallyDefinedDate date) {
		PartiallyDefinedDate difference = new PartiallyDefinedDate();
		if (this.century==null || date.century==null)return difference;
		difference.century = date.century - this.century;
		if (this.decadeInCentury==null || date.decadeInCentury==null)return difference;
		difference.decadeInCentury= date.decadeInCentury- this.decadeInCentury;
		if (this.yearInDecade==null || date.yearInDecade==null)return difference;
		difference.yearInDecade= date.yearInDecade- this.yearInDecade;
		if (this.monthInYear==null || date.monthInYear==null)return difference;
		difference.monthInYear= date.monthInYear- this.monthInYear;
		if (this.dayInMonth==null || date.dayInMonth==null)return difference;
		difference.dayInMonth= date.dayInMonth- this.dayInMonth;
		if (this.hour==null || date.hour==null)return difference;
		difference.hour= date.hour- this.hour;
		if (this.minute==null || date.minute==null)return difference;
		difference.minute= date.minute- this.minute;
		if (this.second==null || date.second==null)return difference;
		difference.second= date.second- this.second;
		if (this.millies==null || date.millies==null)return difference;
		difference.millies= date.millies- this.millies;
		return difference;
	}

	private void resolveNegativeValues(Integer originalMonth) {
		if (minute!=null && minute<0){
			minute+=60;
			hour--;
		}
		if (hour!=null && hour<0){
			hour+=24;
			dayInMonth--;
		}
		if (dayInMonth!=null && dayInMonth<0 && originalMonth!=null){
			dayInMonth+=getMonthLength(originalMonth, getYear());//length of previous month
			monthInYear--;
		}
		if (monthInYear!=null && monthInYear<0){
			monthInYear+=12;
			yearInDecade--;
		}
		if (yearInDecade!=null && yearInDecade<0){
			yearInDecade+=10;
			decadeInCentury--;
		}
		if (decadeInCentury!=null && decadeInCentury<0){
			decadeInCentury+=10;
			century--;
		}
	}

	public String timeSpanString() {
		StringBuilder res = new StringBuilder();
		int year = getYear();
		if (year>0)
			res.append(year).append(" years");
		if (monthInYear!=null && monthInYear>0)
			res.append(" ").append(monthInYear).append(" months");
		if (dayInMonth!=null && dayInMonth>0)
			res.append(" ").append(dayInMonth).append(" days");
		if (hour!=null && hour>0)
			res.append(" ").append(hour).append(" hours");
		if (minute!=null && minute>0)
			res.append(" ").append(minute).append(" minutes");
		return res.toString();
	}
}
