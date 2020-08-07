package mylyn.trello.ui;

import java.io.IOException;
import java.net.URL;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.mylyn.internal.tasks.ui.wizards.NewWebTaskPage;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentModel;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskRepositoryPage;
import org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard;
import org.eclipse.mylyn.tasks.ui.wizards.NewWebTaskWizard;
import org.eclipse.mylyn.tasks.ui.wizards.RepositoryQueryWizard;
import org.eclipse.mylyn.tasks.ui.wizards.TaskAttachmentPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

import mylyn.trello.ui.wizard.TrelloTaskWizardPage;
import mylyn.trello.ui.wizard.TrelloQueryPage;
import mylyn.trello.ui.wizard.TrelloRepositorySettingsPage;
import trello.core.TrelloRepositoryConnector;
import trello.core.client.TrelloClient;
import trello.core.model.Card;

public class TrelloRepositoryConnectorUi extends AbstractRepositoryConnectorUi
{
	@Override
	public @NonNull String getConnectorKind()
	{
		return TrelloRepositoryConnector.CONNECTOR_KIND;
	}

	@SuppressWarnings("restriction")
	@Override
	public @NonNull IWizard getNewTaskWizard(@NonNull TaskRepository a_taskRepository, @Nullable ITaskMapping a_selection)
	{
		/*Card card = null;
		try
		{
			card = new TrelloClient().createCard();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NewWebTaskWizard wizard = new NewWebTaskWizard(a_taskRepository, card.getUrl(), a_selection);
		wizard.addPage(new NewWebTaskPage(a_selection));
		return wizard;*/
		//NewTaskWizard wizard = new NewTaskWizard(a_taskRepository, a_selection);
		NewTaskWizard wizard = new NewTaskWizard(a_taskRepository, a_selection);
		wizard.addPage(new TrelloTaskWizardPage());
		return wizard;
	}


	
	@Override
	public @NonNull IWizard getQueryWizard(@NonNull TaskRepository a_taskRepository, @Nullable IRepositoryQuery a_queryToEdit)
	{
		RepositoryQueryWizard wizard = new RepositoryQueryWizard(a_taskRepository);
		wizard.addPage(new TrelloQueryPage(a_taskRepository, a_queryToEdit));
		return wizard;
	}

	@Override
	public @NonNull ITaskRepositoryPage getSettingsPage(@Nullable TaskRepository a_taskRepository)
	{
		return new TrelloRepositorySettingsPage(a_taskRepository);
	}

	@Override
	public boolean hasSearchPage()
	{
		return false;
	}
	
	@Override
	public IWizardPage getTaskAttachmentPage(TaskAttachmentModel model)
	{
		TaskAttachmentPage page = new TaskAttachmentPage(model);
		page.setNeedsReplaceExisting(false);//true
		return page;
	}
}
