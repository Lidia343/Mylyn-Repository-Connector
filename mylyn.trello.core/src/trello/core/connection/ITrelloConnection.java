package trello.core.connection;

import java.io.IOException;
import java.util.List;

import trello.core.model.BoardList;
import trello.core.model.CardList;
import trello.core.model.User;

/**
 * Интерфейс, содержащий методы для установки соединения с сайтом trello.com и
 * получения от него пользовательских данных (информации о пользователе, его
 * досках, списках и карточках).
 */
public interface ITrelloConnection
{
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
	User connectAndGetUserData(String a_key, String a_token) throws Exception;

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
}
