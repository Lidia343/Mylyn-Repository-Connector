package trello.core.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.ITask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import trello.core.model.Board;
import trello.core.model.BoardList;
import trello.core.model.Card;
import trello.core.model.CardList;
import trello.core.model.Member;

/**
 * Класс предназначен для установки соединения с сайтом trello.com и получения
 * от него пользовательских данных (информации о пользователе, его досках,
 * списках и карточках).
 */
@SuppressWarnings("restriction")
public class TrelloConnection implements ITrelloConnection
{
	private String m_key;
	private String m_token;
	private final String m_mainUrlPart = "https://api.trello.com/1/";
	private Gson m_gson = (new GsonBuilder().create());
	private final String encoding = "UTF-8";

	public TrelloConnection(String a_key, String a_token)
	{
		m_key = a_key;
		m_token = a_token;
	}
	
	@Override
	public Member getMemberData() throws IOException
	{
		String line = connectByUrlAndGetResponse(ITrelloConnection.GET_METHOD, m_mainUrlPart + "members/me?fields=fullName,username,email&key=" + m_key + "&token=" + m_token);
		Member user = null;
		if(line != null)
			user = m_gson.fromJson(line, Member.class);
		return user;
	}

	@Override
	public BoardList getBoardList() throws IOException
	{
		String line = connectByUrlAndGetResponse(ITrelloConnection.GET_METHOD, m_mainUrlPart + "members/me?fields=none&boards=all&board_fields=name,url,closed&key=" + m_key + "&token=" + m_token);
		BoardList boardList = null;
		if(line != null)
			boardList = m_gson.fromJson(line, BoardList.class);
		return boardList;
	}
	
	@Override
	public List<CardList> getCardLists(String a_boardId) throws IOException
	{
		String line = connectByUrlAndGetResponse(ITrelloConnection.GET_METHOD, m_mainUrlPart + "boards/" + a_boardId + "/lists?cards=open&card_fields=name,desc,url,closed,due,dueComplete,dateLastActivity,idChecklists,idMembers&fields=name,closed&key=" 
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
		URL url = new URL(a_url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
	public Card getCardById(String a_cardId) throws IOException
	{
		String line = connectByUrlAndGetResponse(ITrelloConnection.GET_METHOD, m_mainUrlPart + "cards/" + a_cardId + "?fields=name,desc,url,closed,due,dueComplete,dateLastActivity,idChecklists,idMembers&key=" + m_key + "&token=" + m_token);
		Card card = null;
		if(line != null)
			card = m_gson.fromJson(line, Card.class);
		return card;
	}
	
	@Override
	public Card getCardByUrl(String a_cardUrl) throws IOException
	{
		for (Card c : getAllCards())
		{
			if (c.getUrl().equals(a_cardUrl))
				return c;
		}
		return null;
	}
	
	@Override
	public String changeCard(String a_cardId, String a_attributeName, String a_attributeValue) throws IOException
	{
		connectByUrlAndGetResponse(ITrelloConnection.PUT_METHOD, m_mainUrlPart + "cards/" + a_cardId + 
	    "?" + a_attributeName + "=" + URLEncoder.encode(a_attributeValue, encoding) + "&key=" + m_key + "&token=" + m_token);
		Card card = getCardById(a_cardId);
		return card.getUrl();
	}

	@Override
	public void deleteCard(String a_cardId) throws IOException
	{
		connectByUrlAndGetResponse(ITrelloConnection.DELETE_METHOD, m_mainUrlPart + "cards/" + a_cardId + "?key=" + m_key + "&token=" + m_token);
	}

	@Override
	public Set<String> getChangedCards(Set<ITask> a_set) throws IOException
	{
		Set<String> result = new HashSet<>();
		for (Card card : getAllCards())
		{
			for (ITask task : a_set)
			{
				if (task.getTaskId().equals(card.getId()))
				{
					if (!areSameAttributes(task, card)) result.add(task.getTaskId());
					break;
				}
			}
		}
		return result;
	}
	
	private boolean areSameAttributes(ITask a_task, Card a_card)
	{
		boolean areSame = true;
		//if (a_task.getTaskKey().equals(a_card.getName())) areSame = false;
		if (!a_task.getSummary().equals(a_card.getDesc())) areSame = false;
		if (!a_task.getUrl().equals(a_card.getUrl())) areSame = false;
		return areSame;
	}

	@Override
	public List<Card> getAllCards() throws IOException
	{
		List<Card> cards = new ArrayList<>();
		for (Board b : getBoardList().getBoards())
		{
			for (CardList l : getCardLists(b.getId()))
			{
				for (Card c : l.getCards())
				{
					cards.add(c);
				}
			}
		}
		return cards;
	}

	@Override
	public String doAuth() 
	{
		return "";
		/*String res = "Error";
		try
		{
			URL userDataURL = new URL("https://trello.com/login");
			HttpURLConnection connection = (HttpURLConnection) userDataURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			String par = Base64.getEncoder().encodeToString(("Lidia343" + ":" + "Era3vnomnagna0H").getBytes());
			connection.setRequestProperty("Authorization", "Basic " + par);
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
		catch(Exception e)
		{
			
		}
		return res;*/
	}

	@Override
	public Board getBoard (String a_boardId)
	{
		String line;
		Board board = null;
		try
		{
			line = connectByUrlAndGetResponse(ITrelloConnection.GET_METHOD, m_mainUrlPart + "boards/" + a_boardId + "?key=" + m_key + "&token=" + m_token);
			if(line != null)
				board = m_gson.fromJson(line, Board.class);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return board;
	}
	
	@Override
	public List<Member> getMembers(List<String> a_boardIds) throws IOException
	{
		String line;
		List<Member> partialMembers = null;
		List<Member> members = new ArrayList<>();
		Member temp = null;
		
		for (int i = 0; i <  a_boardIds.size(); i++)
		{
			line = connectByUrlAndGetResponse(ITrelloConnection.GET_METHOD, m_mainUrlPart + "boards/" + a_boardIds.get(i) + "/members?key=" + m_key + "&token=" + m_token);
			if(line != null)
				partialMembers = m_gson.fromJson(line, new TypeToken<List<Member>>(){}.getType());
			
			boolean contain = false;
			for (Member m : partialMembers)
			{
				line = connectByUrlAndGetResponse(ITrelloConnection.GET_METHOD, m_mainUrlPart + "members/" + m.getId() + "?key=" + m_key + "&token=" + m_token);
				if (line != null)
					temp = m_gson.fromJson(line, Member.class);
				
				if (temp == null) break;
				for (Member u : members)
				{
					if (temp.getId().equals(u.getId())) contain = true;
				}
				if (!contain) members.add(temp);
			}
		}
		return members;
	}
}
