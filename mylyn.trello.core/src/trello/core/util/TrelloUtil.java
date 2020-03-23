package trello.core.util;

import java.util.Date;

public class TrelloUtil
{
	public static long toTrelloTime(Date date)
	{
		return date.getTime() / 1000l;
	}
	
	public static Date parseDate(long seconds) 
	{
		return new Date(seconds * 1000l);
	}

	public static Date parseDate(String time) 
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
}
