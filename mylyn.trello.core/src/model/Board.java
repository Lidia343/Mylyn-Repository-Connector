package model;

import java.util.List;

/**
 * Класс необходим для хранения информации о доске.
 */
public class Board
{
	private String id;
	private String name;
	private String url;
	private List<CardList> cardLists;
	
	/**
	 * Конструктор класса Board.
	 */
	public Board()
	{
	}
	
	/**
	 * @param a_id - id доски
	 */
	public void setId(String a_id)
	{
		id = a_id;
	}
	
	/**
	 * @param a_name - название доски
	 */
	public void setName(String a_name)
	{
		name = a_name;
	}
	
	/**
	 * @param a_name - url доски
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
	 * @return id доски
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * @return имя доски
	 */
	public String getName()
	{
		return name;
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
}
