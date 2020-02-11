package model;

import java.util.List;

/**
 * Класс необходим для хранения информации о списке карточек.
 */
public class CardList
{
	private String name;
	private List<Card> cards;
	
	/**
	 * Конструктор класса CardList.
	 */
	public CardList()
	{
	}
	
	
	/**
	 * Метод для возврата имени списка.
	 * @return имя списка
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Метод для возврата списка карточек
	 * @return список карточек
	 */
	public List<Card> getCards()
	{
		return cards;
	}
}
