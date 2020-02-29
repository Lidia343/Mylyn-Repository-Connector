package mylyn.trello.core.connection;

import java.util.List;
import mylyn.trello.core.model.BoardList;
import mylyn.trello.core.model.CardList;
import mylyn.trello.core.model.TrelloImitDataStorage;
import mylyn.trello.core.model.User;

/**
 * Класс предназначен для имитации получения данных с сайта trello.com.
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
