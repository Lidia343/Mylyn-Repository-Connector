package trello.core.model;

import trello.core.TrelloAttribute;

/**
 * Класс необходим для хранения информации о карточке.
 */
public class Card extends TrelloObject
{
	private String closed;
	private String desc;
	private String idList;
	private String url;
	private String due;
	private String dueComplete;
	private String dateLastActivity;
	private String[] idChecklists;

	/**
	 * Конструктор класса Card.
	 */
	public Card()
	{
		name = "";
	}

	public void setClosed (String a_closed)
	{
		closed = a_closed;
	}
	
	public String getClosed ()
	{
		return closed;
	}
	
	/** 
	 * @param a_name - описание карточки
	 */
	public void setDesc(String a_desc)
	{
		desc = a_desc;
	}

	public void setIdList (String a_idList)
	{
		idList = a_idList;
	}
	
	public String getIdList ()
	{
		return idList;
	}
	
	/**
	 * @param a_url - url карточки
	 */
	public void setUrl(String a_url)
	{
		url = a_url;
	}

	/**
	 * @param a_due - запланированная дата выполнения карточки
	 */
	public void setDue(String a_due)
	{
		due = a_due;
	}
	
	/** 
	 * @param a_dueComplete - состояние выполнена/нет карточки
	 */
	public void setDueComlete(String a_dueComplete)
	{
		dueComplete = a_dueComplete;
	}
	
	/**
	 * @param a_dateLastActivity - дата последнего действия с карточкой
	 */
	public void setDateLastActivity(String a_dateLastActivity)
	{
		dateLastActivity = a_dateLastActivity;
	}
	
	/**
	 * @param a_idChecklists - массив id чек-листов карточки
	 */
	public void setIdChecklists(String[] a_idChecklists)
	{
		idChecklists = a_idChecklists;
	}
	
	/**
	 * @return описание карточки
	 */
	public String getDesc()
	{
		return desc;
	}

	/**
	 * @return URL карточки
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @return запланированную дату завершения карточки
	 */
	public String getDue()
	{
		return due;
	}

	/**
	 * @return состояние выполнена/нет карточки
	 */
	public String getDueComplete()
	{
		return dueComplete;
	}
	
	/**
	 * @return дату последнего действия с карточкой
	 */
	public String getDateLastActivity()
	{
		return dateLastActivity;
	}

	/**
	 * @return массив id чек-листов карточки
	 */
	public String[] getIdChecklists()
	{
		return idChecklists;
	}
	
	public String getValue (String a_key)
	{
		if (a_key.equals(TrelloAttribute.CLOSED)) return closed;
		if (a_key.equals(TrelloAttribute.ID_LIST)) return idList;
		if (a_key.equals(TrelloAttribute.NAME)) return name;
		if (a_key.equals(TrelloAttribute.DESCRIPTION)) return desc;
		if (a_key.equals(TrelloAttribute.DUE_COMPLETE)) return dueComplete;
		if (a_key.equals(TrelloAttribute.DUE)) return due;
		if (a_key.equals(TrelloAttribute.DATE_LAST_ACTIVITY)) return dateLastActivity;
		return null;
	}
	
	@Override
	public String toString ()
	{
		String ids = "";
		if (idChecklists != null)
		for (String s : idChecklists)
		{
			ids += (s + "\n");
		}
		return "Id: " + id + "\nName: " + name + "\nDesc: " + desc + "\nUrl: " + url + "\nClosed: " +"\nDue: "
				+ due + "\nDueComplete: " + dueComplete + "\nDateLastActivity: " + dateLastActivity
				+ "\nIdChecklists:\n" + ids;
	}
}
