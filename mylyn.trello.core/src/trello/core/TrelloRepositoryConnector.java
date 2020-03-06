package trello.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;
import trello.core.connection.TrelloConnectionImit;
import trello.core.handle.INoConnectionResultHandler;
import trello.core.handle.NoConnectionResultHandler;
import trello.core.model.Board;
import trello.core.model.BoardList;
import trello.core.model.Card;
import trello.core.model.CardList;

public class TrelloRepositoryConnector extends AbstractRepositoryConnector
{
	public final static String CONNECTOR_KIND = "trello";
	public final static String REPOSITORY_URL = "https://trello.com";

	@Override
	public boolean canCreateNewTask(@NonNull TaskRepository a_repository)
	{
		return false;
	}

	@Override
	public boolean canCreateTaskFromKey(@NonNull TaskRepository a_repository)
	{
		return false;
	}

	@Override
	public @NonNull String getConnectorKind()
	{
		return CONNECTOR_KIND;
	}

	@Override
	public @NonNull String getLabel()
	{
		return "Trello";
	}

	@Override
	public @Nullable String getRepositoryUrlFromTaskUrl(@NonNull String a_taskUrl)
	{
		String repositoryUrlEnd = ".com/";
		int index = a_taskUrl.indexOf(repositoryUrlEnd);
		return a_taskUrl.substring(0, index + repositoryUrlEnd.length());
	}

	@Override
	public @Nullable String getTaskIdFromTaskUrl(@NonNull String a_taskUrl)
	{
		return getTaskIdOrUrl(true, a_taskUrl);
	}

	@Override
	public @Nullable String getTaskUrl(@NonNull String a_repositoryUrl, @NonNull String a_taskIdOrKey)
	{
		return getTaskIdOrUrl(false, a_taskIdOrKey);
	}

	private String getTaskIdOrUrl (boolean a_isId, String a_idOrUrl)
	{
		INoConnectionResultHandler handler = new NoConnectionResultHandler();
		TrelloConnectionImit connection = new TrelloConnectionImit("1c8962f69eea55f6346aacabf4b9d90e", "f51cede3da475d589f56dc510ce4292bd4bcae622d1cca92e75821309cef697f");//Тестовая реализация
		try
		{
			BoardList boardList = connection.getBoardList();
			for (Board b : boardList.getBoards())
			{
				for (CardList l : b.getCardLists())
				{
					for (Card c : l.getCards())
					{
						if (a_isId)
						{
							if (c.getUrl().equals(a_idOrUrl))
							{
								return c.getId();
							}
						}
						else
						if (c.getId().equals(a_idOrUrl))
						{
							return c.getUrl();
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			handler.createMessage(e.getMessage());
		}
		return null;
	}
	
	@Override
	public boolean hasTaskChanged(@NonNull TaskRepository a_taskRepository, @NonNull ITask a_task, @NonNull TaskData a_taskData)
	{
		return false;
	}

	@Override
	public void updateTaskFromTaskData(@NonNull TaskRepository a_taskRepository, @NonNull ITask a_task, @NonNull TaskData a_taskData)
	{
	}

	@Override
	public @NonNull TaskData getTaskData(@NonNull TaskRepository a_repository, @NonNull String a_taskId, 
			                             IProgressMonitor a_monitor) throws CoreException
	{
		TaskData taskData = new TaskData(new TaskAttributeMapper(a_repository), a_repository.getConnectorKind(), a_repository.getRepositoryUrl(), a_taskId); 
		return taskData; 
	}

	@Override
	public IStatus performQuery(@NonNull TaskRepository a_repository, @NonNull IRepositoryQuery a_query, @NonNull TaskDataCollector a_collector, 
			                    @Nullable ISynchronizationSession a_session, IProgressMonitor a_monitor)
	{
		return Status.OK_STATUS; 
	}

	@Override
	public void updateRepositoryConfiguration(@NonNull TaskRepository a_taskRepository, IProgressMonitor a_monitor) throws CoreException
	{
	}
}
