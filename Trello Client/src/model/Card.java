package model;

/**
 * Класс необходим для хранения информации о карточке.
 */
public class Card
{
	private String id;
	private String name;
	private String desc;
	private String url;
	
	/**
	 * Конструктор класса Card.
	 */
	public Card ()
	{
		name = "";
	}
	
	public String getId()
	{
		return id;
	}
	
	/**
	 * Метод для возврата имени карточки.
	 * @return имя карточки
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Метод для возврата описания карточки.
	 * @return описание карточки
	 */
	public String getDesc()
	{
		return desc;
	}
	
	/**
	 * Метод для возврата URL карточки.
	 * @return URL карточки
	 */
	public String getUrl()
	{
		return url;
	}	
}
