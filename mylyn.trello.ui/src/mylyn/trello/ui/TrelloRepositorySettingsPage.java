package mylyn.trello.ui;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage;
import org.eclipse.swt.widgets.Composite;

import trello.core.TrelloRepositoryConnector;

public class TrelloRepositorySettingsPage extends AbstractRepositorySettingsPage
{
	public TrelloRepositorySettingsPage(String a_title, String a_description,
		                                TaskRepository a_taskRepository, AbstractRepositoryConnector a_connector)
	{
		super(a_title, a_description, a_taskRepository, a_connector);
		
		
	}
	
	@Override
	protected void createAdditionalControls(@NonNull Composite a_composite)
	{
		if (serverUrlCombo.getItemCount() == 0)
		{
			serverUrlCombo.add(TrelloRepositoryConnector.REPOSITORY_URL);
		}
	}

	@Override
	public String getConnectorKind()
	{
		return TrelloRepositoryConnector.CONNECTOR_KIND;
	}
}
