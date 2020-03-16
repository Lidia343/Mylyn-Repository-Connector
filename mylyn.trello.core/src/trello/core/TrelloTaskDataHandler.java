package trello.core;

import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.mylyn.commons.net.Policy;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import trello.core.connection.ITrelloConnection;
import trello.core.connection.TrelloConnection;
import trello.core.model.Card;

public class TrelloTaskDataHandler extends AbstractTaskDataHandler
{
	private static final String TASK_DATA_VERSION = "2"; 
	
	public TaskData getTaskData(TaskRepository a_repository, String a_taskId, IProgressMonitor a_monitor) throws CoreException
	{
		a_monitor = Policy.monitorFor(a_monitor);
		try
		{
			a_monitor.beginTask("Task Download", IProgressMonitor.UNKNOWN);
			return downloadTaskData(a_repository, a_taskId, a_monitor);
		}
		finally
		{
			a_monitor.done();
		}
	}
	
	private TaskData downloadTaskData(TaskRepository a_repository, String a_taskId, IProgressMonitor a_monitor) throws CoreException
	{
		ITrelloConnection client = new TrelloConnection(ITrelloConnection.DEFAULT_KEY, ITrelloConnection.DEFAULT_TOKEN);
		Card card = null;
		try
		{
			card = client.getCardById(a_taskId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return createTaskDataFromCard(a_repository, card);
	}

	private TaskData createTaskDataFromCard(TaskRepository a_repository, @NonNull Card a_card)
	{
		TaskData taskData = new TaskData(new TaskAttributeMapper(a_repository), TrelloRepositoryConnector.CONNECTOR_KIND, a_repository.getRepositoryUrl(), a_card.getId()); 
		taskData.setVersion(TASK_DATA_VERSION);
		taskData.setPartial(true);
		return taskData;
	}
	
	@Override
	public RepositoryResponse postTaskData(@NonNull TaskRepository a_repository, @NonNull TaskData a_taskData, @Nullable Set<TaskAttribute> a_oldAttributes,
										   @Nullable IProgressMonitor a_monitor) throws CoreException
	{
		return null;
	}

	@Override
	public boolean initializeTaskData(@NonNull TaskRepository a_repository, @NonNull TaskData a_data, @Nullable ITaskMapping a_initializationData,
									  @Nullable IProgressMonitor a_monitor) throws CoreException
	{
		return false;
	}

	@Override
	public TaskAttributeMapper getAttributeMapper(@NonNull TaskRepository a_repository)
	{
		return new TaskAttributeMapper(a_repository);
	}
}
