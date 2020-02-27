package connection;

import java.util.List;
import model.BoardList;
import model.CardList;
import model.TrelloImitDataStorage;
import model.User;

/**
 * Класс предназначен для имитации получения данных с сайта  trello.com.
 */
public class TrelloConnectionImit implements ITrelloConnection
{
	TrelloImitDataStorage dataStorage;
	
	public TrelloConnectionImit()
	{
		dataStorage = new TrelloImitDataStorage();
	}
	
	@Override
	public User connectAndGetUserData(String a_key, String a_token) throws Exception
	{
		return dataStorage.getUser();
	}

	@Override
	public BoardList getBoardList() throws Exception
	{
		return dataStorage.getBoardList();
	}

	@Override
	public List<CardList> getCardLists(String a_boardId) throws Exception
	{
		return dataStorage.getCardLists(a_boardId);
	}
}
