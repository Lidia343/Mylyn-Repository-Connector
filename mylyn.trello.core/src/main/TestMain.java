package main;

import java.util.List;

import connection.TrelloConnectionImit;
import model.*;

public class TestMain
{
	public static void main(String[] args)
	{
		TrelloConnectionImit imit = new TrelloConnectionImit();
		try
		{
			/*BoardList list = imit.getBoardList();
			for (Board b : list.getBoards())
			{
				System.out.println("Доска");
				System.out.println(b.getId());
				System.out.println(b.getName());
				System.out.println(b.getUrl());
				for (CardList l : b.getCardLists())
				{
					System.out.println("Список");
					System.out.println(l.getBoardId());
					System.out.println(l.getName());
					System.out.println("Карточки");
					for (Card c : l.getCards())
					{
						System.out.println(c.getId());
						System.out.println(c.getName());
						System.out.println(c.getUrl());
						System.out.println(c.getDesc());
					}
				}
				System.out.println();
			}*/
			/*List<CardList> cardLists = imit.getCardLists("5e4019433654b30377b36237");
			for (CardList l : cardLists)
			{
				System.out.println(l.getBoardId());
				System.out.println(l.getName());
				System.out.println("Карточки:");
				for (Card c : l.getCards())
				{
					System.out.println(c.getId());
					System.out.println(c.getName());
					System.out.println(c.getUrl());
				}
				System.out.println();
			}*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
