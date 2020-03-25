package trello.core.model;

import java.util.List;

/**
 * Класс необходим для хранения информации о списке карточек.
 */
public class CardList
{
	private String id;
	private String name;
	private List<Card> cards;
	private String boardId;
	private String closed;

	/**
	 * @param a_id - id списка карточек
	 */
	public void setId(String a_id)
	{
		id = a_id;
	}
	
	/**
	 * @param a_name - название списка карточек
	 */
	public void setName(String a_name)
	{
		name = a_name;
	}

	/**
	 * @param a_cards - список карточек
	 */
	public void setCards(List<Card> a_cards)
	{
		cards = a_cards;
	}

	/**
	 * @param a_boardId - id доски
	 */
	public void setBoardId(String a_boardId)
	{
		boardId = a_boardId;
	}

	/**
	 * @param a_closed - состояние закрыт/открыт списка карточек
	 */
	public void setClosed(String a_closed)
	{
		closed = a_closed;
	}
	
	/**
	 * @return id списка карточек
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * @return имя списка карточек
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return список карточек
	 */
	public List<Card> getCards()
	{
		return cards;
	}

	/** 
	 * @return id доски, которой принадлежит список
	 */
	public String getBoardId()
	{
		return boardId;
	}
	
	/**
	 * @return состояние закрыт/открыт списка карточек
	 */
	public String getClosed()
	{
		return closed;
	}
}
