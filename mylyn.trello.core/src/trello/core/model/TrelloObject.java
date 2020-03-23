package trello.core.model;

/**
 * Класс представляет общую для объектов репозитория Trello информацию.
 */
public class TrelloObject
{
	protected String id;
	protected String name;
	
	/**
	 * @param a_id - id объекта
	 */
	public void setId(String a_id)
	{
		id = a_id;
	}
	
	/**
	 * @param a_name - имя объекта
	 */
	public void setName(String a_name)
	{
		name = a_name;
	}
	
	/**
	 * @return id объекта
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * @return имя объекта
	 */
	public String getName()
	{
		return name;
	}
}
