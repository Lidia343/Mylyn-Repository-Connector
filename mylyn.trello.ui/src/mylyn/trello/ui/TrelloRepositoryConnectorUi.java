package mylyn.trello.ui;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskRepositoryPage;
import org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard;
import org.eclipse.mylyn.tasks.ui.wizards.RepositoryQueryWizard;

public class TrelloRepositoryConnectorUi //extends AbstractRepositoryConnectorUi
{
	/*@Override
	public @NonNull String getConnectorKind()
	{
		return TrelloRepositoryConnector.CONNECTOR_KIND;
	}

	@Override
	public @NonNull IWizard getNewTaskWizard(@NonNull TaskRepository a_taskRepository, @Nullable ITaskMapping a_selection)
	{
		return new NewTaskWizard(a_taskRepository, a_selection);
	}

	@Override
	public @NonNull IWizard getQueryWizard(@NonNull TaskRepository a_taskRepository, @Nullable IRepositoryQuery a_queryToEdit)
	{
		RepositoryQueryWizard wizard = new RepositoryQueryWizard(a_taskRepository);
		return wizard;
	}

	@Override
	public @NonNull ITaskRepositoryPage getSettingsPage(@Nullable TaskRepository a_taskRepository)
	{
		return new TrelloRepositorySettingsPage("Settings", "Description", a_taskRepository, new TrelloRepositoryConnector(), this);
	}

	@Override
	public boolean hasSearchPage()
	{
		return false;
	}*/
}
