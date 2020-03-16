package trello.core;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.mylyn.commons.net.Policy;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
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
	private final TrelloTaskDataHandler m_taskDataHandler = new TrelloTaskDataHandler();
	
	private final String taskKeyUpdateDate = "UpdateDate";

	@Override
	public boolean canCreateNewTask(@NonNull TaskRepository a_repository)
	{
		return true;
	}

	@Override
	public boolean canCreateTaskFromKey(@NonNull TaskRepository a_repository)
	{
		return true;
	}
	
	@Override
	public boolean canSynchronizeTask(TaskRepository taskRepository, ITask task) 
	{
		return true;
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
	public TrelloTaskDataHandler getTaskDataHandler() 
	{
		return m_taskDataHandler;
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
		TaskMapper mapper = (TaskMapper)getTaskMapping(a_taskData);
		if (a_taskData.isPartial()) 
		{
			return mapper.hasChanges(a_task);
		} 
		else 
		{
			Date repositoryDate = mapper.getModificationDate();
			Date localDate = new Date(Long.parseLong(a_task.getAttribute(taskKeyUpdateDate)));
			if (repositoryDate != null && repositoryDate.equals(localDate)) 
			{
				return false;
			}
			return true;
		}
	}

	@Override
	public void updateTaskFromTaskData(@NonNull TaskRepository a_taskRepository, @NonNull ITask a_task, @NonNull TaskData a_taskData)
	{
		TaskMapper mapper = (TaskMapper)getTaskMapping(a_taskData);
		mapper.applyTo(a_task);
		String status = mapper.getStatus();
		if (status != null) 
		{
			if (mapper.getStatus().equals("closed")) 
			{
				Date modificationDate = mapper.getModificationDate();
				if (modificationDate == null) 
				{
					modificationDate = new Date(0);
				}
				a_task.setCompletionDate(modificationDate);
			} 
			else 
			{
				a_task.setCompletionDate(null);
			}
		}
		TrelloConnection client = new TrelloConnection(ITrelloConnection.DEFAULT_KEY, ITrelloConnection.DEFAULT_TOKEN);
		try
		{
			a_task.setUrl(client.changeCard(a_task.getTaskId(), Card.NAME, client.getCardById(a_task.getTaskId()).getName()));
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*if (!a_taskData.isPartial()) 
		{
			a_task.setAttribute("SupportsSubtasks", Boolean.toString(taskDataHandler.supportsSubtasks(a_taskData)));
			Date date = a_task.getModificationDate();
			a_task.setAttribute("UpdateDate", (date != null) ? TracUtil.toTracTime(date) + "" : null); //$NON-NLS-1$
		}*/
	}

	@Override
	public @NonNull TaskData getTaskData(@NonNull TaskRepository a_repository, @NonNull String a_taskId, 
			                             IProgressMonitor a_monitor) throws CoreException
	{
		return m_taskDataHandler.getTaskData(a_repository, a_taskId, a_monitor);
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
	public void preSynchronization(ISynchronizationSession a_session, IProgressMonitor a_monitor) throws CoreException 
	{
		a_monitor = Policy.monitorFor(a_monitor);
		try 
		{
			a_monitor.beginTask(Messages.TrelloRepositoryConnector_Getting_changed_tasks, IProgressMonitor.UNKNOWN);

			if (!a_session.isFullSynchronization()) 
			{
				return;
			}

			if (a_session.getTasks().isEmpty()) 
			{
				return;
			}

			TaskRepository repository = a_session.getTaskRepository();

			if (repository.getSynchronizationTimeStamp() == null || repository.getSynchronizationTimeStamp().length() == 0) 
			{
				for (ITask task : a_session.getTasks()) 
				{
					a_session.markStale(task);
				}
				return;
			}

			Date since = new Date(0);
			try 
			{
				since = new Date(Integer.parseInt(repository.getSynchronizationTimeStamp()));
			} 
			catch (NumberFormatException e) 
			{
			}

			try 
			{
				TrelloConnection client = new TrelloConnection(ITrelloConnection.DEFAULT_KEY, ITrelloConnection.DEFAULT_TOKEN);
				Set<String> ids = client.getChangedCards(a_session.getTasks());
			
				System.err.println(" preSynchronization(): since=" + since.getTime() + ",changed=" + ids); //$NON-NLS-1$ //$NON-NLS-2$ 
				
				if (ids.isEmpty()) 
				{
					a_session.setNeedsPerformQueries(false);
					return;
				}

				if (ids.size() == 1) 
				{
					System.err.println(" preSynchronization(): since=" + since.getTime());
				}

				for (ITask task : a_session.getTasks()) 
				{
					String id = task.getTaskId();
					if (ids.contains(id)) 
					{
						a_session.markStale(task);
					}
				}
			} catch (OperationCanceledException e) 
			{
				throw e;
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		} 
		finally 
		{
			a_monitor.done();
		}
	}
	
	@Override
	public void postSynchronization(ISynchronizationSession a_event, IProgressMonitor a_monitor) throws CoreException 
	{
		try 
		{
			a_monitor.beginTask("", 1);
			if (a_event.isFullSynchronization() && a_event.getStatus() == null) 
			{
				Date date = getSynchronizationTimestamp(a_event);
				if (date != null) 
				{
					a_event.getTaskRepository().setSynchronizationTimeStamp(Long.toString(date.getTime())); 
				}
			}
		} 
		finally 
		{
			a_monitor.done();
		}
	}

	private Date getSynchronizationTimestamp(ISynchronizationSession a_event) 
	{
		Date mostRecent = new Date(0);
		Date mostRecentTimeStamp = new Date (Integer.parseInt(a_event.getTaskRepository().getSynchronizationTimeStamp()));
		for (ITask task : a_event.getChangedTasks()) 
		{
			Date taskModifiedDate = task.getModificationDate();
			if (taskModifiedDate != null && taskModifiedDate.after(mostRecent)) 
			{
				mostRecent = taskModifiedDate;
				mostRecentTimeStamp = task.getModificationDate();
			}
		}
		return mostRecentTimeStamp;
	}
	
	@Override
	public void updateRepositoryConfiguration(@NonNull TaskRepository a_taskRepository, IProgressMonitor a_monitor) throws CoreException
	{
		
	}
	
	@Override
	public boolean canDeleteTask(TaskRepository a_repository, ITask a_task) 
	{
		return true;
	}

	@Override
	public IStatus deleteTask(TaskRepository a_repository, ITask a_task, IProgressMonitor a_monitor) throws CoreException 
	{
		TrelloConnection client = new TrelloConnection(ITrelloConnection.DEFAULT_KEY, ITrelloConnection.DEFAULT_TOKEN);
		try 
		{
			client.deleteCard(a_task.getTaskId());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return Status.OK_STATUS;
	}
	
	@Override
	public boolean canGetTaskHistory(TaskRepository a_repository, ITask a_task) 
	{
		return false;
	}
}
