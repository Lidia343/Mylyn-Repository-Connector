package model;

/**
 * Класс необходим для хранения информации о доске.
 */
public class Board
{
	private String id;
	private String name;
	private String url;
	
	/**
	 * Конструктор класса Board.
	 */
	public Board()
	{
	}
	
	/**
	 * Метод для возврата id доски.
	 * @return id доски
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * Метод для возврата имени доски.
	 * @return имя доски
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Метод для возврата URL доски.
	 * @return URL доски
	 */
	public String getUrl()
	{
		return url;
	}
}
