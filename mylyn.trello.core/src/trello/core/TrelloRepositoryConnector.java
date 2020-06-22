package trello.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
//import org.eclipse.mylyn.tasks.core.ITaskComment;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
//import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;
import trello.core.connection.ITrelloConnection;
import trello.core.connection.TrelloConnection;
import trello.core.model.Card;
import trello.core.model.Task;
import trello.core.util.TrelloUtil;

public class TrelloRepositoryConnector extends AbstractRepositoryConnector
{
	public final static String CONNECTOR_KIND = "trello";
	public final static String REPOSITORY_URL = "https://trello.com/1/";
	public final static String REPOSITORY_LABEL = "Trello.com";
	
	public final static String LIST_ID_NUMBERS_QUERY_KEY = "listIdNumbers";
	public final static String LIST_ID_QUERY_KEY = "listId";
	public final static String DUE_QUERY_KEY = "due";
	public final static String CLOSED_QUERY_KEY = "closed";
	public final static String COMPLETED_QUERY_KEY = "completed";
	public final static String CHECKLISTS_QUERY_KEY = "checklists";
	
	public final static String NON_CLOSED_CARDS = "Only non-archived cards";
	public final static String CLOSED_AND_NON_CLOSED_CARDS = "Archived and non-archived cards";
	public final static String CLOSED_CARDS = "Only archived cards";
	public final static String COMPLETED_AND_NON_COMPLETED_CARDS = "Completed and non-completed cards";
	public final static String NON_COMPLETED_CARDS = "Only non-completed cards";
	public final static String COMPLETED_CARDS = "Only completed cards";
	
	public final static String ALL = "(All)";
	public final static String NO = "(No)";
	
	private final TrelloTaskDataHandler m_taskDataHandler = new TrelloTaskDataHandler();
	
	private int counter1 = 1;
	private int counter2 = 1;
	
	//private final String taskKeyUpdateDate = "UpdateDate";

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
		ITrelloConnection client = new TrelloConnection(ITrelloConnection.DEFAULT_KEY, ITrelloConnection.DEFAULT_TOKEN);
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
		return false;
		/*else 
		{
			Date repositoryDate = mapper.getModificationDate();
			Date localDate = new Date(Long.parseLong(a_task.getAttribute(taskKeyUpdateDate)));
			if (repositoryDate != null && repositoryDate.equals(localDate)) 
			{
				return false;
			}
			return true;
		}*/
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
	
	public IStatus performQuery(TaskRepository a_repository, IRepositoryQuery a_query, TaskDataCollector a_resultCollector,
			ISynchronizationSession a_session, IProgressMonitor a_monitor) 
	{
		try
		{
			TrelloConnection client = new TrelloConnection(ITrelloConnection.DEFAULT_KEY, ITrelloConnection.DEFAULT_TOKEN);
			int listIdNumbers = Integer.parseInt(a_query.getAttribute(LIST_ID_NUMBERS_QUERY_KEY));
			List<String> listIds = new ArrayList<>();
			for (int i = 0; i < listIdNumbers; i++)
			{
				listIds.add(a_query.getAttribute(LIST_ID_QUERY_KEY + Integer.toString(i)));
			}
			
			List<Card> cards = new ArrayList<>();
			for (String id : listIds)
			{
				List<Card> temp = client.getCards(id, a_query.getAttribute(CLOSED_QUERY_KEY));
				for (Card c : temp)
				{
					String queryDue = a_query.getAttribute(DUE_QUERY_KEY);
					String cardDue = c.getDue();
					if (cardDue != null)
					{
						int end = cardDue.indexOf(".");
						cardDue = cardDue.substring(0, end);
					}
					if (queryDue.equals(NO))
					{
						if (cardDue != null) continue;
					} 
					else
					{
						if (!queryDue.equals(ALL))
						{
							if (cardDue == null) continue;
							if (queryDue.endsWith("T")) cardDue = cardDue.substring(0, cardDue.indexOf('T') + 1);
							if (!cardDue.equals(queryDue)) continue;
						}
					}
					
					String queryComplete = a_query.getAttribute(COMPLETED_QUERY_KEY);
					boolean cardComplete = Boolean.parseBoolean(c.getDueComplete());
					if (queryComplete.equals(COMPLETED_CARDS))
					{
						if (cardComplete == false) continue;

					}
					if (queryComplete.equals(NON_COMPLETED_CARDS))
					{
						if (cardComplete == true) continue;
					}
					
					if (Boolean.parseBoolean(a_query.getAttribute(CHECKLISTS_QUERY_KEY)) == false)
					{
						c.setIdChecklists(null);
					}

					cards.add(c); 
				}
			}
			
			for (Card card : cards)
			{
				TaskData taskData = new TaskData(m_taskDataHandler.getAttributeMapper(a_repository), CONNECTOR_KIND, a_repository.getRepositoryUrl(), card.getId());
				taskData.getRoot().createAttribute(TaskAttribute.SUMMARY).setValue(card.getName());
				taskData.getRoot().createAttribute(TaskAttribute.TASK_URL).setValue(card.getUrl());
				
				String trelloDue = card.getDue();
				if  (trelloDue != null)
				{
					TaskAttribute dueAttr = taskData.getRoot().createAttribute(TaskAttribute.DATE_DUE);
					taskData.getAttributeMapper().setDateValue(dueAttr, TrelloUtil.toMylynDate(trelloDue));
				}
				
				a_session.putTaskData(new Task(a_repository.getUrl(), card.getId(), card.getName()), taskData);
				a_resultCollector.accept(taskData);
			}
			
			/*for (ITask t : a_session.getTasks())
			{
				System.out.println(t.getDueDate());
			}*/
			return Status.OK_STATUS;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Status.CANCEL_STATUS;
		}
	}
	
	@Override
	public void preSynchronization(ISynchronizationSession a_session, IProgressMonitor a_monitor) throws CoreException 
	{
		try 
		{
			System.out.println("!!!!!!!!PreSynchronization!!!!!!!");
			a_monitor = Policy.monitorFor(a_monitor);
			a_monitor.beginTask("PreSync", IProgressMonitor.UNKNOWN);
			
			if (!a_session.isFullSynchronization()) 
			{
				//System.out.println("NoFullSynchronization");
				return;
			}
			
			if (a_session.getTasks().isEmpty()) 
			{
				//System.out.println("EmptyTasks");
				return;
			}
			
			TaskRepository repository = a_session.getTaskRepository();

			/*if (repository.getSynchronizationTimeStamp() == null || repository.getSynchronizationTimeStamp().length() == 0) 
			{
				System.out.println("NoTime");
				for (ITask task : a_session.getTasks()) 
				{
					a_session.markStale(task);
				}
				return;
			}*/
			
			Date since = new Date(0);
			try 
			{
				since = new Date(Integer.parseInt(repository.getSynchronizationTimeStamp()));//since = TrelloUtil.parseDate(Integer.parseInt(repository.getSynchronizationTimeStamp()));
			} catch (NumberFormatException e) {}
			
			try 
			{
				TrelloConnection client = new TrelloConnection(ITrelloConnection.DEFAULT_KEY, ITrelloConnection.DEFAULT_TOKEN);
				Set<String> ids = client.getChangedCards(a_session.getTasks());
			
				//System.out.println("Size:  " + ids.size());
				
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
			
			
		} finally 
		{
			a_monitor.done();
		}
	}
	
	@Override
	public void postSynchronization(ISynchronizationSession a_event, IProgressMonitor a_monitor) throws CoreException 
	{
		try
		{
			System.out.println("!!!!!!!!PostSynchronization!!!!!!!");
			a_monitor.beginTask("", 1);
			if (a_event.isFullSynchronization() && a_event.getStatus() == null) 
			{
				Date date = getSynchronizationTimestamp(a_event);
				if (date != null) 
				{
					a_event.getTaskRepository().setSynchronizationTimeStamp(TrelloUtil.toTrelloTime(date) + "");
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
		Date mostRecentTimeStamp = TrelloUtil.parseDate(a_event.getTaskRepository().getSynchronizationTimeStamp());
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
