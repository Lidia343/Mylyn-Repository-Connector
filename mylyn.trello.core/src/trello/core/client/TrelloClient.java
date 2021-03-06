package trello.core.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.ITask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import trello.core.TrelloRepositoryConnector;
import trello.core.model.Action;
import trello.core.model.Board;
import trello.core.model.BoardList;
import trello.core.model.Card;
import trello.core.model.CardList;
import trello.core.model.Checklist;
import trello.core.model.Member;

/**
 * Класс предназначен для установки соединения с сайтом trello.com и получения
 * от него пользовательских данных (информации о пользователе, его досках,
 * списках и карточках).
 */
@SuppressWarnings("restriction")
public class TrelloClient implements ITrelloClient
{
	private String m_key;
	private String m_token;
	private final String m_mainUrlPart = TrelloRepositoryConnector.REPOSITORY_URL;
	private Gson m_gson = (new GsonBuilder().create());
	private final String encoding = "UTF-8";
	
	private final String m_allFilter = "filter=all&";
	private final String m_closedFilter = "filter=closed&";
	private final String m_openFilter = "filter=open&";
	

	public TrelloClient()
	{
		m_key = ITrelloClient.DEFAULT_KEY;
		m_token = ITrelloClient.DEFAULT_TOKEN;
	}
	
	@Override
	public Member getMemberData() throws IOException
	{
		String line = connectByUrlAndGetResponse(ITrelloClient.GET_METHOD, m_mainUrlPart + "members/me?fields=fullName,username,email&key=" + m_key + "&token=" + m_token);
		Member user = null;
		if(line != null)
			user = m_gson.fromJson(line, Member.class);
		return user;
	}
	
	public List<Card> getCards (String a_listId, String a_closed) throws IOException
	{
		String filter = m_openFilter;
		
		if (a_closed.equals(TrelloRepositoryConnector.CLOSED_CARDS)) filter = m_closedFilter;
		if (a_closed.equals(TrelloRepositoryConnector.CLOSED_AND_NON_CLOSED_CARDS)) filter = m_allFilter;
		
		String line = connectByUrlAndGetResponse(ITrelloClient.GET_METHOD, m_mainUrlPart + "lists/" + a_listId + "/cards?" + filter + "key=" + m_key + "&token=" + m_token);
		List<Card> cards = null;
		if(line != null)
			cards = m_gson.fromJson(line, new TypeToken<List<Card>>(){}.getType());
		return cards;
	}

	@Override
	public BoardList getBoardList(boolean a_seeAlsoClosedBoards) throws IOException
	{
		String filter = "open";
		if (a_seeAlsoClosedBoards) filter = "all";
		
		String line = connectByUrlAndGetResponse(ITrelloClient.GET_METHOD, m_mainUrlPart + "members/me?fields=none&boards=" + filter + "&key=" + m_key + "&token=" + m_token);
		BoardList boardList = null;
		if(line != null)
			boardList = m_gson.fromJson(line, BoardList.class);
		return boardList;
	}
	
	@Override
	public List<CardList> getCardLists(String a_boardId, boolean a_seeAlsoClosedLists) throws IOException
	{
		String filter = m_openFilter;
		if (a_seeAlsoClosedLists) filter = m_allFilter;
		String line = connectByUrlAndGetResponse(ITrelloClient.GET_METHOD, m_mainUrlPart + "boards/" + a_boardId + "/lists?" + filter + "key=" + m_key + "&token=" + m_token);
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
		String line = connectByUrlAndGetResponse(ITrelloClient.GET_METHOD, m_mainUrlPart + "cards/" + a_cardId + "?key=" + m_key + "&token=" + m_token);
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
	public Card createCard (String a_idList, Map<String, String> a_attributes) throws IOException
	{
		StringBuilder parameterBuilder = new StringBuilder();
		for (Entry<String, String> entry : a_attributes.entrySet())
		{
			parameterBuilder.append(entry.getKey() + "=" + entry.getValue());
		}
		
		String line = connectByUrlAndGetResponse(ITrelloClient.POST_METHOD, m_mainUrlPart + "cards?" + 
												 parameterBuilder.toString() + "&key=" + m_key + "&token=" + m_token);
		Card card = null;
		if (line != null)
		{
			card = m_gson.fromJson(line, Card.class);
		}
		return card;
	}
	
	@Override
	public String changeCard(String a_cardId, String a_paramName, String a_paramValue) throws IOException
	{
		connectByUrlAndGetResponse(ITrelloClient.PUT_METHOD, m_mainUrlPart + "cards/" + a_cardId + 
	    "?" + a_paramName + "=" + URLEncoder.encode(a_paramValue, encoding) + "&key=" + m_key + "&token=" + m_token);
		Card card = getCardById(a_cardId);
		return card.getUrl();
	}

	@Override
	public void deleteCard(String a_cardId) throws IOException
	{
		connectByUrlAndGetResponse(ITrelloClient.DELETE_METHOD, m_mainUrlPart + "cards/" + a_cardId + "?key=" + m_key + "&token=" + m_token);
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
		for (Board b : getBoardList(true).getBoards())
		{
			for (CardList l : getCardLists(b.getId(), true))
			{
				for (Card c : getCards(l.getId(), TrelloRepositoryConnector.CLOSED_AND_NON_CLOSED_CARDS))
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
			line = connectByUrlAndGetResponse(ITrelloClient.GET_METHOD, m_mainUrlPart + "boards/" + a_boardId + "?key=" + m_key + "&token=" + m_token);
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
			line = connectByUrlAndGetResponse(ITrelloClient.GET_METHOD, m_mainUrlPart + "boards/" + a_boardIds.get(i) + "/members?key=" + m_key + "&token=" + m_token);
			if(line != null)
				partialMembers = m_gson.fromJson(line, new TypeToken<List<Member>>(){}.getType());
			
			boolean contain = false;
			for (Member m : partialMembers)
			{
				line = connectByUrlAndGetResponse(ITrelloClient.GET_METHOD, m_mainUrlPart + "members/" + m.getId() + "?key=" + m_key + "&token=" + m_token);
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
	
	@Override
	public List<Action> getActions (String a_listId) throws IOException
	{
		String line;
		List<Action> actions = null;
		line = connectByUrlAndGetResponse(ITrelloClient.GET_METHOD, m_mainUrlPart + "lists/" + a_listId + "/actions?key=" + m_key + "&token=" + m_token);
		if (line != null)
				actions = m_gson.fromJson(line, new TypeToken<List<Action>>(){}.getType());
		return actions;
	}
	
	@Override
	public List<Checklist> getChecklists (String a_cardId) throws IOException
	{
		String line;
		List<Checklist> checklists = null;
		line = connectByUrlAndGetResponse(ITrelloClient.GET_METHOD, m_mainUrlPart + "cards/" + a_cardId + "/checklists?key=" + m_key + "&token=" + m_token);
		if (line != null)
			checklists = m_gson.fromJson(line, new TypeToken<List<Checklist>>(){}.getType());
		return checklists;
	}
}