package trello.core;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

public abstract class TrelloAttribute
{
	//Добавить зависимости (чек-листы) - потом это будет условием в клиенте
	public static final String ID_LIST = "idList";
	public static final String CLOSED = "closed";
	public static final String DUE_COMPLETE = "dueComplete";
	
	public static final String NAME = "name";
	public static final String DESCRIPTION = "desc";
	public static final String DUE = "due";
	public static final String DATE_LAST_ACTIVITY = "dateLastActivity";
	public static final String DATE_CREATION = "date";
	public static final String DATE_COMPLETION = "date";
	
	public static String valueOf (String a_mylynAttributeId)
	{
		if (a_mylynAttributeId.equals(ID_LIST)) return ID_LIST;
		if (a_mylynAttributeId.equals(CLOSED)) return CLOSED;
		if (a_mylynAttributeId.equals(DUE_COMPLETE)) return DUE_COMPLETE;
		
		if (a_mylynAttributeId.equals(TaskAttribute.SUMMARY)) return NAME;
		if (a_mylynAttributeId.equals(TaskAttribute.DESCRIPTION)) return DESCRIPTION;
		if (a_mylynAttributeId.equals(TaskAttribute.DATE_DUE)) return DUE;
		if (a_mylynAttributeId.equals(TaskAttribute.DATE_MODIFICATION)) return DATE_LAST_ACTIVITY;
		if (a_mylynAttributeId.equals(TaskAttribute.DATE_CREATION)) return DATE_CREATION;
		if (a_mylynAttributeId.equals(TaskAttribute.DATE_COMPLETION)) return DATE_COMPLETION;
		
		return null;
	}
}
