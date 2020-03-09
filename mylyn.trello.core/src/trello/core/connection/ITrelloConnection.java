package trello.core.connection;

import java.io.IOException;
import java.util.List;

import trello.core.model.BoardList;
import trello.core.model.Card;
import trello.core.model.CardList;
import trello.core.model.User;

/**
 * Интерфейс, содержащий методы для установки соединения с сайтом trello.com и
 * получения от него пользовательских данных (информации о пользователе, его
 * досках, списках и карточках).
 */
public interface ITrelloConnection
{
	public static final String DEFAULT_KEY = "1c8962f69eea55f6346aacabf4b9d90e";
	public static final String DEFAULT_TOKEN = "f51cede3da475d589f56dc510ce4292bd4bcae622d1cca92e75821309cef697f";
	
	/**
	 * Метод для установки соединения и получения данных о пользователе.
	 * 
	 * @param a_key
	 *            - ключ пользователя
	 * @param a_token
	 *            - токен пользователя
	 * @return объект класса User, содержащий информацию о пользователе
	 * @throws IOException
	 */
	User getUserData() throws Exception;

	/**
	 * Метод для получения списка досок.
	 * 
	 * @return объект класса BoardList, содержащий список досок
	 * @throws IOException
	 */
	BoardList getBoardList() throws Exception;

	/**
	 * Метод для получения списка списков карточек одной доски.
	 * 
	 * @param a_boardId
	 *            - id доски
	 * @return список List списков CardList карточек Card (объект класса
	 *         CardList содержит название списка карточек и сам список List
	 *         карточек)
	 * @throws IOException
	 */
	List<CardList> getCardLists(String a_boardId) throws Exception;
	
	/**
	 * @param a_cardId - id карточки
	 * @return объект класса Card, соответствующий карточке с указанным id
	 * @throws Exception 
	 */
	Card getCard(String a_cardId) throws Exception;
}
