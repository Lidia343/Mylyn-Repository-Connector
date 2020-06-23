package trello.core.connection;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.ITask;

import trello.core.model.Action;
import trello.core.model.Board;
import trello.core.model.BoardList;
import trello.core.model.Card;
import trello.core.model.CardList;
import trello.core.model.Member;

/**
 * Интерфейс, содержащий методы для установки соединения с сайтом trello.com и
 * получения от него пользовательских данных (информации о пользователе, его
 * досках, списках и карточках).
 */
public interface ITrelloConnection
{
	public static final String DEFAULT_KEY = "1c8962f69eea55f6346aacabf4b9d90e";
	public static final String DEFAULT_TOKEN = "f51cede3da475d589f56dc510ce4292bd4bcae622d1cca92e75821309cef697f";
	public static final String POST_METHOD = "POST";
	public static final String PUT_METHOD = "PUT";
	public static final String GET_METHOD = "GET";
	public static final String DELETE_METHOD = "DELETE";
	
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
	Member getMemberData() throws Exception;

	List<Card> getCards (String a_listId, String a_closed) throws Exception;
	
	/**
	 * Метод для получения списка досок.
	 * 
	 * @return объект класса BoardList, содержащий список досок
	 * @throws IOException
	 */
	BoardList getBoardList(boolean a_seeAlsoClosedBoards) throws Exception;

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
	List<CardList> getCardLists(String a_boardId, boolean a_seeAlsoClosedLists) throws Exception;
	
	/**
	 * @param a_cardId - id карточки
	 * @return объект класса Card, соответствующий карточке с указанным id
	 * @throws Exception 
	 */
	Card getCardById(String a_cardId) throws Exception;
	
	/**
	 * @param a_cardUrl - URL карточки
	 * @return объект класса Card, соответствующий карточке с указанным URL
	 * @throws Exception 
	 */
	Card getCardByUrl(String a_cardUrl) throws Exception;
	
	/**
	 * Метод для изменения свойства карточки.
	 * @param a_cardId - id карточки
	 * @param a_attributeName - название свойства
	 * @param a_attributeValue - значение свойства
	 * @return новый URL карточки
	 */
	String changeCard(String a_cardId, String a_attributeName, String a_attributeValue) throws Exception;
	
	
	/**
	 * Метод удаляет карточку из репозитория.
	 * @param a_cardId - id карточки
	 */
	void deleteCard(String a_cardId) throws Exception;
	
	
	/**
	 * @param oldCards - набор id карточек из локального репозитория
	 * @return набор id изменённых карточек из репозитория Trello
	 */
	Set<String> getChangedCards(Set<ITask> oldCards) throws Exception;
	
	
	/**
	 * @return все карточки из репозитория
	 */
	List<Card> getAllCards() throws Exception;
	
	String doAuth();
	
	Board getBoard (String a_boardId);
	
	List<Member> getMembers(List<String> boardIds) throws Exception;
	
	List<Action> getActions(String a_listId) throws Exception;
}
