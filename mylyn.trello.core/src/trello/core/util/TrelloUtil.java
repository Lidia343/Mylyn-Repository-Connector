package trello.core.util;

import java.util.Date;

public class TrelloUtil
{
	public static long toTrelloTime (Date date)
	{
		return date.getTime() / 1000l;
	}
	
	public static Date parseDate (long seconds) 
	{
		return new Date(seconds * 1000l);
	}

	public static Date parseDate (String time) 
	{
		if (time != null) 
		{
			try 
			{
				return TrelloUtil.parseDate(Long.valueOf(time));
			} catch (NumberFormatException e) {}
		}
		return null;
	}
	
	public static String toStandartTimePart (int a_part)
	{
		String part = Integer.toString(a_part);
		if (part.length() == 1)
			part = "0" + a_part;
		return part;
	}
	
	public static String toTrelloHours (int a_hours)
	{
		a_hours -= 3;
		if (a_hours < 0)
			a_hours = a_hours + 24;
		return toStandartTimePart(a_hours);
	}
	
	@SuppressWarnings("deprecation")
	public static Date toMylynDate (String a_trelloDate)
	{
		int year = Integer.parseInt(a_trelloDate.substring(0, 4));
		int month = Integer.parseInt(a_trelloDate.substring(5, 7)) - 1;
		int day = Integer.parseInt(a_trelloDate.substring(8, 10));
		int hours = Integer.parseInt(a_trelloDate.substring(11, 13)) + 3;
		int min = Integer.parseInt(a_trelloDate.substring(14, 16));
		int sec = Integer.parseInt(a_trelloDate.substring(17, 19));
		return new Date (year, month, day, hours, min, sec);
	}
	
	public static boolean contains (String a_text, String[] a_array)
	{
		boolean contain = false;
		
		for (String s : a_array)
		{
			if (a_text.equals(s))
			{
				contain = true;
				break;
			}
		}
		return contain;
	}
}
