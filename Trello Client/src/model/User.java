package model;

/**
 * Класс необходим для хранения информации о пользователе.
 */
public class User
{
	private String fullName;
	private String username;
	private String email;
	
	/**
	 * Конструктор класса User.
	 */
	public User()
	{
	}
	
	/**
	 * Метод для возврата полного имени пользователя.
	 * @return полное имя пользователя
	 */
	public String getFullName()
	{
		return fullName;
	}
	
	/**
	 * Метод для возврата логина пользователя.
	 * @return логин пользователя
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * Метод для возврата адреса электронной почты пользователя.
	 * @return адрес электронной почты пользователя
	 */
	public String getEmail()
	{
		return email;
	}
}
