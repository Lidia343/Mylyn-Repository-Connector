package mylyn.trello.core.handle;

/**
 * Интерфейс обработчика результата попытки установки соединения с trello.com
 * (при отсутствии подключения)
 *
 */
public interface NoConnectionResultHandler
{
	/**
	 * Метод для создания окна с сообщением пользователю.
	 * 
	 * @param a_message
	 *            - сообщение
	 */
	void createMessage(String a_message);
}
