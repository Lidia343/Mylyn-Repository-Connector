package trello.core.model;

public class Action extends TrelloObject
{
	public final static String UPDATE_CARD = "updateCard";
	public static final String CREATE_CARD = "createCard";
	
	private Data data;
	private String type;
	private String date;
	
	public Data getData ()
	{
		return data;
	}
	
	public String getType ()
	{
		return type;
	}
	
	public String getDate ()
	{
		return date;
	}
}
