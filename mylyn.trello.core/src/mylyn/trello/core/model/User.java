package mylyn.trello.core.model;

/**
 * Класс необходим для хранения информации о пользователе.
 */
public class User
{
	private String	fullName;
	private String	username;
	private String	email;

	/**
	 * Конструктор класса User.
	 */
	public User()
	{
	}

	/**
	 * Метод для установки имени пользователя.
	 * 
	 * @param a_fullName
	 *            - полное имя
	 */
	public void setFullName(String a_fullName)
	{
		fullName = a_fullName;
	}

	/**
	 * Метод для установки логина пользователя.
	 * 
	 * @param a_username
	 *            - логин
	 */
	public void setUserName(String a_username)
	{
		username = a_username;
	}

	/**
	 * Метод для установки email-адреса пользователя.
	 * 
	 * @param email
	 *            - email-адрес
	 */
	public void setEmail(String a_email)
	{
		email = a_email;
	}

	/**
	 * Метод для возврата полного имени пользователя.
	 * 
	 * @return полное имя пользователя
	 */
	public String getFullName()
	{
		return fullName;
	}

	/**
	 * Метод для возврата логина пользователя.
	 * 
	 * @return логин пользователя
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * Метод для возврата адреса электронной почты пользователя.
	 * 
	 * @return адрес электронной почты пользователя
	 */
	public String getEmail()
	{
		return email;
	}
}
