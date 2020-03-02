package trello.core.handle;

/**
 * Интерфейс обработчика результата попытки установки соединения с trello.com
 * (при отсутствии подключения).
 */
public interface INoConnectionResultHandler
{
	/**
	 * Метод для создания окна с сообщением пользователю.
	 * @param a_message - сообщение
	 */
	void createMessage(String a_message);
}
