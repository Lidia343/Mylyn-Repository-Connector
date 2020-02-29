package mylyn.trello.core.model;

import java.util.List;

/**
 * Класс необходим для хранения информации о списке карточек.
 */
public class CardList
{
	private String		name;
	private List<Card>	cards;
	private String		boardId;

	/**
	 * Конструктор класса CardList.
	 */
	public CardList()
	{
	}

	/**
	 * Метод для установки названия списка карточек.
	 * 
	 * @param a_name
	 *            - название
	 */
	public void setName(String a_name)
	{
		name = a_name;
	}

	/**
	 * Метод для установки списка карточек.
	 * 
	 * @param a_cards
	 *            - список карточек
	 */
	public void setCards(List<Card> a_cards)
	{
		cards = a_cards;
	}

	/**
	 * Метод для установки id доски, которой принадлежит список.
	 * 
	 * @param a_boardId
	 *            - id доски
	 */
	public void setBoardId(String a_boardId)
	{
		boardId = a_boardId;
	}

	/**
	 * Метод для возврата имени списка.
	 * 
	 * @return имя списка
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Метод для возврата списка карточек
	 * 
	 * @return список карточек
	 */
	public List<Card> getCards()
	{
		return cards;
	}

	/**
	 * Метод для возврата id доски, которой принадлежит список.
	 * 
	 * @return id доски
	 */
	public String getBoardId()
	{
		return boardId;
	}
}
