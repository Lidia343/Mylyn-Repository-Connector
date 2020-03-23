package trello.core.model;

/**
 * Класс необходим для хранения информации об элементе чек-листа карточки.
 */
public class CheckItem extends TrelloObject
{
	private String state;
	
	/**
	 * @param a_state - состояние выполнен/нет элемента чек-листа
	 */
	public void setState(String a_state)
	{
		state = a_state;
	}
	
	/**
	 * @return состояние выполнен/нет элемента чек-листа
	 */
	public String getState()
	{
		return state;
	}
}
