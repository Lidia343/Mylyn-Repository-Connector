package trello.core.model;

import org.eclipse.mylyn.internal.tasks.core.AbstractTask;

import trello.core.TrelloRepositoryConnector;

@SuppressWarnings("restriction")
public class Task extends AbstractTask
{
	public Task(String a_repositoryUrl, String a_taskId, String a_summary)
	{
		super(a_repositoryUrl, a_taskId, a_summary);
	}

	@Override
	public boolean isLocal()
	{
		return false;
	}

	@Override
	public String getConnectorKind()
	{
		return TrelloRepositoryConnector.CONNECTOR_KIND;
	}
}
