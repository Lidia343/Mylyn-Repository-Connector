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
	private String closed;
	private String due;
	private String dueComplete;
	private String dateLastActivity;
	private String[] idChecklists;
	private String[] idMembers;

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
	 * @param a_closed - состояние закрыта/открыта карточки
	 */
	public void setClosed(String a_closed)
	{
		closed = a_closed;
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
	 * @param a_idMembers - массив id участников карточки
	 */
	public void setIdMembers(String[] a_idMembers)
	{
		idMembers = a_idMembers;
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
	 * @return состояние закрыта/открыта карточки
	 */
	public String getClosed()
	{
		return closed;
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

	/**
	 * @return массив id участников карточки
	 */
	public String[] getIdMembers()
	{
		return idMembers;
	}
}
