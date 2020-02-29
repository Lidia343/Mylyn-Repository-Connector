package mylyn.trello.ui;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage;
import org.eclipse.swt.widgets.Composite;

public class TrelloRepositorySettingsPage extends AbstractRepositorySettingsPage
{
	public TrelloRepositorySettingsPage(String a_title, String a_description,
		   TaskRepository a_taskRepository, AbstractRepositoryConnector a_connector,
		   AbstractRepositoryConnectorUi a_connectorUi)
	{
		super(a_title, a_description, a_taskRepository, a_connector, a_connectorUi);
	}

	@Override
	protected void createAdditionalControls(@NonNull Composite a_arg0)
	{
	}

	@Override
	public String getConnectorKind()
	{
		return TrelloRepositoryConnector.CONNECTOR_KIND;
	}
}
