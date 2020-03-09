package trello.core.connection;

import java.util.List;

import trello.core.model.BoardList;
import trello.core.model.Card;
import trello.core.model.CardList;
import trello.core.model.TrelloImitDataStorage;
import trello.core.model.User;

/**
 * Класс предназначен для имитации получения данных с сайта trello.com.
 */
public class TrelloConnectionImit implements ITrelloConnection
{
	TrelloImitDataStorage dataStorage;

	public TrelloConnectionImit(String a_key, String a_token)
	{
		dataStorage = new TrelloImitDataStorage();
	}

	@Override
	public User getUserData() throws Exception
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
	
	@Override
	public Card getCard(String a_cardId)
	{
		return dataStorage.getCardLists(TrelloImitDataStorage.boardIds[0]).get(0).getCards().get(0);
	}
}
