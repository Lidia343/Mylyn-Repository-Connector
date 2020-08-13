package trello.core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

import trello.core.model.Card.Key;
import trello.core.Messages;

public enum TrelloAttribute
{
	CLOSED(Key.CLOSED, Messages.TrelloAttribute_Closed, null, TaskAttribute.TYPE_BOOLEAN,
		   Flag.ATTRIBUTE),
	
	DATE_COMPLETION(Key.DATE_COMPLETION, Messages.TrelloAttribute_Date_Completion,
					TaskAttribute.DATE_COMPLETION, TaskAttribute.TYPE_DATE, Flag.READ_ONLY),
	
	DATE_CREATION(Key.DATE_CREATION, Messages.TrelloAttribute_Date_Creation,
				  TaskAttribute.DATE_CREATION, TaskAttribute.TYPE_DATE, Flag.READ_ONLY),
	
	DATE_LAST_ACTIVITY(Key.DATE_LAST_ACTIVITY, Messages.TrelloAttribute_Date_Last_Activity,
					   TaskAttribute.DATE_MODIFICATION, TaskAttribute.TYPE_DATE, Flag.READ_ONLY),
	
	DESCRIPTION(Key.DESCRIPTION, Messages.TrelloAttribute_Description, TaskAttribute.DESCRIPTION,
				TaskAttribute.TYPE_LONG_RICH_TEXT),
	
	DUE(Key.DUE, Messages.TrelloAttribute_Due, TaskAttribute.DATE_DUE, TaskAttribute.TYPE_DATE),
	
	DUE_COMPLETE(Key.DUE_COMPLETE, Messages.TrelloAttribute_Due_Complete, null,
				 TaskAttribute.TYPE_BOOLEAN, Flag.ATTRIBUTE),
	
	ID(Key.ID, Messages.TrelloAttribute_ID, TaskAttribute.TASK_KEY, TaskAttribute.TYPE_SHORT_TEXT,
	   Flag.PEOPLE),
	
	ID_LIST(Key.ID_LIST, Messages.TrelloAttribute_ID_List, null, TaskAttribute.TYPE_SHORT_TEXT,
			Flag.ATTRIBUTE),
	
	NAME(Key.NAME, Messages.TrelloAttribute_Name, TaskAttribute.SUMMARY, TaskAttribute.TYPE_SHORT_RICH_TEXT),
	
	OWNER(Key.OWNER, Messages.TrelloAttribute_Assigned_to, TaskAttribute.USER_ASSIGNED,
		  TaskAttribute.TYPE_PERSON, Flag.PEOPLE),
	
	TYPE(Key.TYPE, Messages.TrelloAttribute_Type, TaskAttribute.TASK_KIND, TaskAttribute.TYPE_SINGLE_SELECT,
		 Flag.ATTRIBUTE),
	
	URL(Key.URL, Messages.TrelloAttribute_URL, TaskAttribute.TASK_URL, TaskAttribute.TYPE_SHORT_TEXT);
	
	public enum Flag
	{
		READ_ONLY, ATTRIBUTE, PEOPLE;
	}
	
	static Map<String, TrelloAttribute> attributeByTrelloKey = new HashMap<>();
	static Map<String, TrelloAttribute> trelloKeyByTaskKey = new HashMap<>();
	
	private String m_trelloKey;
	private String m_name;
	private String m_taskKey;
	private String m_type;
	private EnumSet<Flag> m_flags;
	
	public static TrelloAttribute getByTrelloKey (String a_trelloKey)
	{
		for (TrelloAttribute attribute : values())
		{
			if (a_trelloKey.equals(attribute.getTrelloKey()))
			{
				return attribute;
			}
		}
		return null;
	}
	
	public static TrelloAttribute getByTaskKey (String a_taskKey)
	{
		for (TrelloAttribute attribute : values())
		{
			if (a_taskKey.equals(attribute.getTaskKey()))
			{
				return attribute;
			}
		}
		return null;
	}
	
	TrelloAttribute (Key a_trelloKey, String a_name, String a_taskKey, String a_type,
				     Flag a_firstFlag, Flag... a_moreFlags)
	{
		m_trelloKey = a_trelloKey.getKey();
		m_name = a_name;
		m_taskKey = a_taskKey;
		m_type = a_type;
		if (a_firstFlag == null)
		{
			m_flags = EnumSet.noneOf(Flag.class);
		}
		else
		{
			m_flags = EnumSet.of(a_firstFlag, a_moreFlags);
		}
	}
	
	TrelloAttribute (Key a_trelloKey, String a_name, String a_taskKey, String a_type)
	{
		this(a_trelloKey, a_name, a_taskKey, a_type, null);
	}

	public String getTrelloKey ()
	{
		return m_trelloKey;
	}
	
	public String getTaskKey ()
	{
		return m_taskKey;
	}
	
	public String getKind ()
	{
		if (m_flags.contains(Flag.ATTRIBUTE))
		{
			return TaskAttribute.KIND_DEFAULT;
		}
		else
		if (m_flags.contains(Flag.PEOPLE))
		{
			return TaskAttribute.KIND_PEOPLE;
		}
		return null;
	}
	
	public String getType ()
	{
		return m_type;
	}
	
	public boolean isReadOnly ()
	{
		return m_flags.contains(Flag.READ_ONLY);
	}
	
	@Override
	public String toString ()
	{
		return m_name;
	}
}
