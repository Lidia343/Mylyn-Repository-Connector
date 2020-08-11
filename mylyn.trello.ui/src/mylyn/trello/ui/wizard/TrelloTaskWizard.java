package mylyn.trello.ui.wizard;

import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard;

public class TrelloTaskWizard extends NewTaskWizard
{
	private ITaskMapping m_taskSelection;
	
	public TrelloTaskWizard(TaskRepository a_taskRepository, ITaskMapping a_taskSelection)
	{
		super(a_taskRepository, a_taskSelection);
		m_taskSelection = new TrelloTaskMapping();
		a_taskSelection = m_taskSelection;
		setNeedsProgressMonitor(false);
	}

	@Override
	public ITaskMapping getTaskSelection ()
	{
		return m_taskSelection;
	}
}
