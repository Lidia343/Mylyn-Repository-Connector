package mylyn.trello.ui.wizard;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

import trello.core.TrelloRepositoryConnector;

public class TrelloRepositorySettingsPage extends AbstractRepositorySettingsPage
{
	private static final String TITLE = "Repository settings";

	private static final String DESCRIPTION = "Example: " + TrelloRepositoryConnector.REPOSITORY_URL;
	
	public TrelloRepositorySettingsPage(TaskRepository a_taskRepository)
	{
		super(TITLE, DESCRIPTION, a_taskRepository);
	}
	
	private void setRepositoryLabel()
	{
		if (serverUrlCombo.getText().equals(TrelloRepositoryConnector.REPOSITORY_URL))
		{
			repositoryLabelEditor.setStringValue(TrelloRepositoryConnector.REPOSITORY_LABEL);
		}
	}
	
	@Override
	protected void createAdditionalControls(@NonNull Composite a_composite)
	{
		if (serverUrlCombo.getItemCount() == 0)
		{
			serverUrlCombo.add(TrelloRepositoryConnector.REPOSITORY_URL);
		}
		serverUrlCombo.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent a_e)
			{
				if (repositoryLabelEditor.getStringValue().equals(""))
				{
					setRepositoryLabel();
				}
			}
		});
		serverUrlCombo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				setRepositoryLabel();
			}
		});
	}

	@Override
	public String getConnectorKind()
	{
		return TrelloRepositoryConnector.CONNECTOR_KIND;
	}
}
