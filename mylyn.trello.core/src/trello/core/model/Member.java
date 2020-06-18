package trello.core.model;

/**
 * Класс необходим для хранения информации о участнике.
 */
public class Member
{
	private String id;
	private String fullName;
	private String username;
	private String email;
	private String[] idBoards;

	/**
	 * @param a_id - id участника
	 */
	public void setId(String a_id)
	{
		id = a_id;
	}
	
	/**
	 * @param a_fullName - полное имя участника
	 */
	public void setFullName(String a_fullName)
	{
		fullName = a_fullName;
	}

	/**
	 * @param a_username - логин участника
	 */
	public void setUserName(String a_username)
	{
		username = a_username;
	}

	/**
	 * @param a_idBoards - список id досок участника
	 */
	public void setIdBoards (String[] a_idBoards)
	{
		idBoards = a_idBoards;
	}
	
	/**
	 * @param email- email-адрес участника
	 */
	public void setEmail(String a_email)
	{
		email = a_email;
	}
	
	/**
	 * @return id участника
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * @return полное имя участника
	 */
	public String getFullName()
	{
		return fullName;
	}

	/**
	 * @return логин участника
	 */
	public String getUsername()
	{ 
		return username;
	}

	/**
	 * @return адрес электронной почты участника
	 */
	public String getEmail()
	{
		if (email == null) email = "";
		return email;
	}
	
	/**
	 * @return список id досок участника
	 */
	public String[] getaIdBoards()
	{ 
		return idBoards;
	}
}
