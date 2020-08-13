package trello.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "trello.core.messages";
	
	static
	{
		reloadMessages();
	}
	
	public static void reloadMessages ()
	{
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
	public static String TrelloAttribute_Assigned_to;
	
	public static String TrelloAttribute_Closed;
	
	public static String TrelloAttribute_Date_Completion;
	
	public static String TrelloAttribute_Date_Creation;
	
	public static String TrelloAttribute_Date_Last_Activity;
	
	public static String TrelloAttribute_Description;
	
	public static String TrelloAttribute_Due;
	
	public static String TrelloAttribute_Due_Complete;
	
	public static String TrelloAttribute_ID;
	
	public static String TrelloAttribute_ID_List;
	
	public static String TrelloAttribute_Name;
	
	public static String TrelloAttribute_Type;
	
	public static String TrelloAttribute_URL;
}
