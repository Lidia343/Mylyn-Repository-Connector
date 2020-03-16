package trello.core.connection;

import java.util.List;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.ITask;

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
	public Card getCardById(String a_cardId)
	{
		return dataStorage.getCardLists(TrelloImitDataStorage.boardIds[0]).get(0).getCards().get(0);
	}
	
	@Override
	public Card getCardByUrl(String a_cardUrl)
	{
		return dataStorage.getCardLists(TrelloImitDataStorage.boardIds[0]).get(0).getCards().get(0);
	}

	@Override
	public String changeCard(String a_cardId, String a_attributeName, String a_attributeValue)
	{
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCard(String a_cardId)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getChangedCards(Set<ITask> a_oldCards)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
