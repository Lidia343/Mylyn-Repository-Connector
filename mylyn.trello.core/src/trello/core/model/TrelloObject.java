package trello.core.model;

/**
 * Класс предоставляет общую для объектов репозитория Trello информацию.
 */
public class TrelloObject
{
	protected String id;
	protected String name;
	
	public String getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
}
