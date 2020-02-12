package graphics;

import org.eclipse.jface.viewers.IStructuredContentProvider;

import model.BoardList;

/**
 * Класс необходим для предоставления данных о списке досок компоненту TableViewer.
 */
public class BoardsContentProvider implements IStructuredContentProvider 
{
	@Override
	public Object[] getElements(Object a_inputElement) 
	{
		if (a_inputElement instanceof BoardList)
		{
			try 
			{
				return ((BoardList)a_inputElement).getBoards().toArray();
			} catch (Exception a_e) 
			{
				System.out.println(a_e.getMessage());
			}
		}
		return new Object[0];
	}
}
