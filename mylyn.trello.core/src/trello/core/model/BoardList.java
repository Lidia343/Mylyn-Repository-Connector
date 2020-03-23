package trello.core.model;

import java.util.List;

/**
 * Класс необходим для хранения информации о списке досок.
 */
public class BoardList
{
	private List<Board> boards;

	/**
	 * @param a_boards - список досок
	 */
	public void setBoards(List<Board> a_boards)
	{
		boards = a_boards;
	}

	/**
	 * @return список досок
	 */
	public List<Board> getBoards()
	{
		return boards;
	}
}
