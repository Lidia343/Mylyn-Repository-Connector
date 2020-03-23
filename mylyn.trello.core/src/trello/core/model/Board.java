package trello.core.model;

import java.util.List;

/**
 * Класс необходим для хранения информации о доске.
 */
public class Board extends TrelloObject
{
	private String url;
	private List<CardList> cardLists;
	private String closed;

	/**
	 * @param a_url - url доски
	 */
	public void setUrl(String a_url)
	{
		url = a_url;
	}

	/**
	 * @param a_cardLists - списки карточек доски
	 */
	public void setCardLists(List<CardList> a_cardLists)
	{
		cardLists = a_cardLists;
	}

	/**
	 * @param a_closed - состояние закрыта/открыта доски
	 */
	public void setClosed(String a_closed)
	{
		closed = a_closed;
	}
	
	/**
	 * @return URL доски
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @return списки карточек доски
	 */
	public List<CardList> getCardLists()
	{
		return cardLists;
	}
	
	/**
	 * @return состояние закрыта/открыта доски
	 */
	public String getClosed()
	{
		return closed;
	}
}
