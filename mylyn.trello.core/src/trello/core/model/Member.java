package trello.core.model;

/**
 * Класс необходим для хранения информации о пользователе.
 */
public class User
{
	private String fullName;
	private String username;
	private String email;

	/**
	 * @param a_fullName - полное имя пользователя
	 */
	public void setFullName(String a_fullName)
	{
		fullName = a_fullName;
	}

	/**
	 * @param a_username - логин пользователя
	 */
	public void setUserName(String a_username)
	{
		username = a_username;
	}

	/**
	 * @param email- email-адрес пользователя
	 */
	public void setEmail(String a_email)
	{
		email = a_email;
	}

	/**
	 * @return полное имя пользователя
	 */
	public String getFullName()
	{
		return fullName;
	}

	/**
	 * @return логин пользователя
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @return адрес электронной почты пользователя
	 */
	public String getEmail()
	{
		return email;
	}
}
