package trello.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "trello.core.messages";

	static 
	{
		reloadMessages();
	}

	public static void reloadMessages() 
	{
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
	public static String TrelloRepositoryConnector_Getting_changed_tasks;
}
