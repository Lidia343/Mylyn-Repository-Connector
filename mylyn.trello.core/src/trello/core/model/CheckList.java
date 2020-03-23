package trello.core.model;

import java.util.List;

/**
 * Класс необходим для хранения информации о чек-листе карточки.
 */
public class CheckList extends TrelloObject
{
	private List<CheckItem> checkItems;
	
	/**
	 * @param a_checkItems - список элементов чек-листа
	 */
	public void setCheckItems(List<CheckItem> a_checkItems)
	{
		checkItems = a_checkItems;
	}
	
	/**
	 * @return список элементов чек-листа
	 */
	public List<CheckItem> getCheckItems()
	{
		return checkItems;
	}
}
