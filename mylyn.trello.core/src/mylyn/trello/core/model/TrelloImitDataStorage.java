package mylyn.trello.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс предназначен для установки и возврата данных о досках, списках карточек
 * и карточках, не расположенных на trello.com, но определённых в формате
 * хранимой на данном сайте соответствующей пользовательской информации.
 */
public class TrelloImitDataStorage
{
	private User			user;
	private BoardList		boardList;

	private final String[]	boardIds	= { "5e401943376hk65477b36237",
			"5e4019433654b30377b36237" };
	private final String[]	boardNames	= { "Главная", "Погода" };
	private final String[]	boardUrls	= {
			"https://trello.com/b/UIDPFrBr/%D0%B4%D0%0%D0%BE-%D0%B%82%D1%8C-%D0%B2-trello",
			"https://trello.com/b/IRTihBr/%D0%B5%BE-%D0%B%8%8C-%D0%B2-trello" };

	private final String	cardUrl		= "https://trello.com/c/Kjt3jSA7/1-%D0%BD%BC%1%80-%D0DBC-%D0%B1D1%8B";

	private final int		listCount	= 1;
	private final int		cardCount	= 3;

	/**
	 * Конструктор класса TrelloImitDataStorage.
	 */
	public TrelloImitDataStorage()
	{
		user = new User();
		boardList = new BoardList();
		setData();
	}

	/**
	 * Метод для начальной установки данных.
	 */
	private void setData()
	{
		user.setFullName("Maria");
		user.setUserName("Marta");
		user.setEmail("maria@gmail.com");

		Board board;
		List<Board> boards = new ArrayList<>();

		CardList cardList;
		List<Card> cards;
		Card card;
		List<CardList> tempList;

		for(int i = 0; i < boardIds.length; i++)
		{
			board = new Board();
			board.setId(boardIds[i]);
			board.setName(boardNames[i]);
			board.setUrl(boardUrls[i]);

			tempList = new ArrayList<>();
			for(int l = 0; l < listCount; l++)
			{
				cardList = new CardList();
				cardList.setName("Список " + l);
				cardList.setBoardId(board.getId());
				cards = new ArrayList<>();
				for(int c = 0; c < cardCount; c++)
				{
					card = new Card();
					card.setId(board.getId() + l + c);
					card.setName("Карточка " + c);
					card.setDesc("Описиание " + c);
					card.setUrl(cardUrl + i + l + c);
					cards.add(card);
				}
				cardList.setCards(cards);
				tempList.add(cardList);
			}
			board.setCardLists(tempList);
			boards.add(board);
		}

		boardList.setBoards(boards);
	}

	/**
	 * @return объект класса User, содержащий данные о пользователе.
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * @return список досок.
	 */
	public BoardList getBoardList()
	{
		return boardList;
	}

	/**
	 * @param a_boardId
	 *            - id доски
	 * @return список списков карточек
	 */
	public List<CardList> getCardLists(String a_boardId)
	{
		if(boardList == null || boardList.getBoards() == null || boardList.getBoards().size() == 0)
		{
			return null;
		}

		for(Board b : boardList.getBoards())
		{
			if(b.getId().equals(a_boardId))
			{
				return b.getCardLists();
			}
		}

		return null;
	}
}
