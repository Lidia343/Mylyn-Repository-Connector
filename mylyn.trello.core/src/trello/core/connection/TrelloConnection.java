package trello.core.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import trello.core.model.BoardList;
import trello.core.model.Card;
import trello.core.model.CardList;
import trello.core.model.User;

/**
 * Класс предназначен для установки соединения с сайтом trello.com и получения
 * от него пользовательских данных (информации о пользователе, его досках,
 * списках и карточках).
 */
public class TrelloConnection implements ITrelloConnection
{
	private String m_key;
	private String m_token;
	private String m_mainUrlPart = "https://api.trello.com/1/";
	private Gson m_gson = (new GsonBuilder().create());

	public TrelloConnection(String a_key, String a_token)
	{
		m_key = a_key;
		m_token = a_token;
	}
	
	@Override
	public User getUserData() throws IOException
	{
		String line = connectByUrlAndGetResponse("GET", m_mainUrlPart + "members/me?fields=fullName,username,email&key=" + m_key + "&token=" + m_token);
		User user = null;
		if(line != null)
			user = m_gson.fromJson(line, User.class);
		return user;
	}

	@Override
	public BoardList getBoardList() throws IOException
	{
		String line = connectByUrlAndGetResponse("GET", m_mainUrlPart + "members/me?fields=none&boards=all&board_fields=name,url&key=" + m_key + "&token=" + m_token);
		BoardList boardList = null;
		if(line != null)
			boardList = m_gson.fromJson(line, BoardList.class);
		return boardList;
	}
	
	@Override
	public List<CardList> getCardLists(String a_boardId) throws IOException
	{
		String line = connectByUrlAndGetResponse("GET", m_mainUrlPart + "boards/" + a_boardId + "/lists?cards=open&card_fields=name,desc,url&fields=name&key=" 
				                                 + m_key + "&token=" + m_token);
		List<CardList> cardLists = null;
		if(line != null)
			cardLists = m_gson.fromJson(line, new TypeToken<List<CardList>>(){}.getType());
		return cardLists;
	}

	/**
	 * Метод для отправки запроса с помощью URL.
	 * @param a_requestMethod - тип запроса
	 * @param a_url - URL запроса
	 * @return строку, содержащую ответ в формате JSON
	 * @throws IOException
	 */
	private String connectByUrlAndGetResponse(String a_requestMethod, String a_url) throws IOException
	{
		URL userDataURL = new URL(a_url);
		HttpURLConnection connection = (HttpURLConnection) userDataURL.openConnection();
		connection.setRequestMethod(a_requestMethod);
		connection.connect();
		int responseCode = connection.getResponseCode();
		if(responseCode >= 400 && responseCode < 500)
		{
			throw new IOException("Ошибка клиента");
		}
		if(responseCode >= 500)
		{
			throw new IOException("Ошибка сервера");
		}
		try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream())))
		{
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = bufferedReader.readLine()) != null)
			{
				builder.append(line + "\n");
			}
			connection.disconnect();
			return builder.toString();
		}
	}
	
	@Override
	public Card getCard(String a_cardId) throws IOException
	{
		String line = connectByUrlAndGetResponse("GET", m_mainUrlPart + "cards/" + a_cardId + "?fields=name,desc,url&key=" + m_key + "&token=" + m_token);
		Card card = null;
		if(line != null)
			card = m_gson.fromJson(line, Card.class);
		return card;
	}
}
