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
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;
import trello.core.connection.ITrelloConnection;
import trello.core.connection.TrelloConnection;
import trello.core.connection.TrelloConnectionImit;
import trello.core.model.Card;
import trello.core.model.CardList;

public class TrelloRepositoryConnector extends AbstractRepositoryConnector
{
	public final static String CONNECTOR_KIND = "trello";
	public final static String REPOSITORY_URL = "https://trello.com";
	public final static String REPOSITORY_LABEL = "Trello.com";
	private final TrelloTaskDataHandler taskDataHandler = new TrelloTaskDataHandler();

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
		return REPOSITORY_LABEL;
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
		TrelloConnection client = new TrelloConnection(ITrelloConnection.DEFAULT_KEY, ITrelloConnection.DEFAULT_TOKEN);
		try
		{
			if (a_isId)
				return client.getCardByUrl(a_idOrUrl).getId();
			else
				return client.getCardById(a_idOrUrl).getUrl();
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
		return taskDataHandler.getTaskData(a_repository, a_taskId, a_monitor);
	}

	@Override
	public IStatus performQuery(@NonNull TaskRepository a_repository, @NonNull IRepositoryQuery a_query, @NonNull TaskDataCollector a_collector, 
			                    @Nullable ISynchronizationSession a_session, IProgressMonitor a_monitor)
	{
		try 
		{////////////////Validate Settings!!!!!!!!!!!!!!
			a_monitor.beginTask("Main task", IProgressMonitor.UNKNOWN);
			TrelloConnectionImit client = new TrelloConnectionImit(null, null);
			for (CardList l : client.getCardLists("5e401943376hk65477b36237"))
			{
				for (Card c : l.getCards())
				{
					TaskData taskData = new TaskData(new TaskAttributeMapper(a_repository), a_repository.getConnectorKind(), a_repository.getRepositoryUrl(), c.getId());
					taskData.setPartial(true);
					
					TaskAttribute attribute = new TaskAttributeMapper(a_repository).createTaskAttachment(taskData);
					attribute.setValue(c.getId() + ""); //$NON-NLS-1$
					a_collector.accept(taskData);
				}
			}
			return Status.OK_STATUS;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Status.CANCEL_STATUS;
		}
		finally
		{
			a_monitor.done();
		}
	}

	@Override
	public void updateRepositoryConfiguration(@NonNull TaskRepository a_taskRepository, IProgressMonitor a_monitor) throws CoreException
	{
	}
}
