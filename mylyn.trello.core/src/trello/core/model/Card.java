package trello.core.model;

/**
 * Класс необходим для хранения информации о карточке.
 */
public class Card
{
	private String	id;
	private String	name;
	private String	desc;
	private String	url;

	/**
	 * Конструктор класса Card.
	 */
	public Card()
	{
		name = "";
	}

	/**
	 * Метод для установки id карточки.
	 * 
	 * @param a_id
	 *            - id
	 */
	public void setId(String a_id)
	{
		id = a_id;
	}

	/**
	 * Метод для установки названия карточки.
	 * 
	 * @param a_name
	 *            - название
	 */
	public void setName(String a_name)
	{
		name = a_name;
	}

	/**
	 * Метод для установки описания карточки.
	 * 
	 * @param a_name
	 *            - описание
	 */
	public void setDesc(String a_desc)
	{
		desc = a_desc;
	}

	/**
	 * Метод для установки url карточки.
	 * 
	 * @param a_name
	 *            - url
	 */
	public void setUrl(String a_url)
	{
		url = a_url;
	}

	public String getId()
	{
		return id;
	}

	/**
	 * Метод для возврата имени карточки.
	 * 
	 * @return имя карточки
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Метод для возврата описания карточки.
	 * 
	 * @return описание карточки
	 */
	public String getDesc()
	{
		return desc;
	}

	/**
	 * Метод для возврата URL карточки.
	 * 
	 * @return URL карточки
	 */
	public String getUrl()
	{
		return url;
	}
}
