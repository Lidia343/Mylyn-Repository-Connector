package trello.core.model;

/**
 * Класс необходим для хранения информации о карточке.
 */
public class Card extends TrelloObject
{
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String DESC = "desc";
	public static final String URL = "url";
	public static final String CLOSED = "closed";
	public static final String DUE = "due";
	public static final String DUE_COMPLETE = "dueComplete";
	public static final String DATE_LAST_ACTIVITY = "dateLastActivity";
	public static final String ID_CHECKLISTS = "idChecklists";
	public static final String ID_MEMBERS = "idMembers";
	
	private String desc;
	private String url;
	private String due;
	private String dueComplete;
	private String dateLastActivity;////////////////////////
	private String[] idChecklists;

	/**
	 * Конструктор класса Card.
	 */
	public Card()
	{
		name = "";
	}

	/** 
	 * @param a_name - описание карточки
	 */
	public void setDesc(String a_desc)
	{
		desc = a_desc;
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
