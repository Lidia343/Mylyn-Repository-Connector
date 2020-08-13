package trello.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Карточка Trello.
 */
public class Card extends TrelloObject
{
	/**
	 * Ключ, соответствующий одному из свойств карточки.
	 */
	public enum Key
	{
		CLOSED("closed"), DATE_COMPLETION("date"),
		DATE_CREATION("date"), DATE_LAST_ACTIVITY("dateLastActivity"),
		DESCRIPTION("desc"), DUE("due"), DUE_COMPLETE("dueComplete"),
		ID("id"), ID_LIST("idList"), NAME("name"), OWNER("owner"),
		TYPE("type"), URL("url");
		
		public static Key fromKey (String a_name)
		{
			for (Key key : Key.values())
			{
				if (key.getKey().equals(a_name))
				{
					return key;
				}
			}
			return null;
		}
		
		private String m_key;
		
		Key (String a_key)
		{
			m_key = a_key;
		}
		
		@Override
		public String toString ()
		{
			return m_key;
		}
		
		public String getKey ()
		{
			return m_key;
		}
	}
	
	/**Отображение, хранящее значения свойств карточки по ключу.*/
	private final Map<Key, String> m_valueByKey = new HashMap<>();
	
	private String closed;
	private String dateLastActivity;
	private String desc;
	private String due;
	private String dueComplete;
	private String[] idChecklists;
	private String idList;
	private String url;
	
	public String getValue (Key a_key)
	{
		return m_valueByKey.get(a_key);
	}
	
	public Map<String, String> getValues ()
	{
		Map<String, String> result = new HashMap<>();
		for (Key key : m_valueByKey.keySet())
		{
			result.put(key.getKey(), m_valueByKey.get(key));
		}
		return result;
	}
	
	public void putValue (Key a_key, String a_value)
	{
		m_valueByKey.put(a_key, a_value);
	}
	
	public void setIdChecklists (String[] a_idChecklists)
	{
		idChecklists = a_idChecklists;
	}
	
	public String getClosed ()
	{
		return closed;
	}
	
	public String getDateLastActivity()
	{
		return dateLastActivity;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public String getDue()
	{
		return due;
	}
	
	public String getDueComplete()
	{
		return dueComplete;
	}
	
	public String[] getIdChecklists()
	{
		return idChecklists;
	}
	
	public String getIdList ()
	{
		return idList;
	}
	
	public String getUrl()
	{
		return url;
	}
}
