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

public class TrelloRepositoryConnector extends AbstractRepositoryConnector
{
	public final static String CONNECTOR_KIND = "trello";

	public TrelloRepositoryConnector()
	{
		
	}

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
		return "api.trello.com";
	}

	@Override
	public @Nullable String getTaskIdFromTaskUrl(@NonNull String a_taskUrl)
	{
		return null;
	}

	@Override
	public @Nullable String getTaskUrl(@NonNull String a_repositoryUrl, @NonNull String a_taskIdOrKey)
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
