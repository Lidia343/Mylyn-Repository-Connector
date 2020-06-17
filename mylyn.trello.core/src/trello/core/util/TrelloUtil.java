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
	
	public static String redactTimePart (int a_part)
	{
		String part = Integer.toString(a_part);
		if (part.length() == 1)
			part = "0" + a_part;
		return part;
	}
	
	public static String redactHours (int a_hours)
	{
		a_hours -= 3;
		if (a_hours < 0)
			a_hours = a_hours + 24;
		return Integer.toString(a_hours);
	}
}
