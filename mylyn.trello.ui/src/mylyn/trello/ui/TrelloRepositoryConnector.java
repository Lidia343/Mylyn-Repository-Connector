package mylyn.trello.ui;
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

public class TrelloRepositoryConnector extends AbstractRepositoryConnector
{
	public final static String CONNECTOR_KIND = "trello";
	
	@Override
	public boolean canCreateNewTask(@NonNull TaskRepository a_taskRepository)
	{
		return false;
	}

	@Override
	public boolean canCreateTaskFromKey(@NonNull TaskRepository a_taskRepository)
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
		return "Trello Repository";
	}

	@Override
	public @Nullable String getRepositoryUrlFromTaskUrl(@NonNull String a_taskFullUrl)
	{
		return "api.trello.com";
	}

	
	public TaskData getTaskData(@NonNull TaskRepository a_taskRepository, @NonNull String a_taskId, IProgressMonitor a_monitor) throws CoreException
	{
		TaskData taskData = new TaskData(new TaskAttributeMapper(a_taskRepository), a_taskRepository.getConnectorKind(), a_taskRepository.getRepositoryUrl(), a_taskId); 
		return taskData; 
	}

	@Override
	public @Nullable String getTaskIdFromTaskUrl(@NonNull String a_taskFullUrl)
	{
		
		return null; 
	}

	@Override
	public @Nullable String getTaskUrl(@NonNull String a_repositoryUrl, @NonNull String a_taskId)
	{
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
	public IStatus performQuery(@NonNull TaskRepository a_taskRepository, @NonNull IRepositoryQuery a_query, 
								@NonNull TaskDataCollector a_collector, @Nullable ISynchronizationSession a_session, IProgressMonitor a_monitor)
	{
		return Status.OK_STATUS; 
	}

	@Override
	public void updateRepositoryConfiguration(@NonNull TaskRepository a_taskRepository, IProgressMonitor a_progressMonitor) throws CoreException
	{
		
	}
}
