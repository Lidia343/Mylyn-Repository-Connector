package mylyn.trello.ui.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import mylyn.trello.ui.calendar.CalendarDialog;
import mylyn.trello.ui.edit.TrelloStringFieldEditor;
import mylyn.trello.ui.handle.IResultHandler;
import trello.core.TrelloTaskDataHandler;

public class TrelloTaskWizardPage implements IWizardPage
{
	private final String m_enterNameErrorMessage = "Enter name";
	
	private String m_title = "New Task";
	private String m_name = "Task";
	private String m_description = "Enter task parameters";
	private String m_errorMessage = m_enterNameErrorMessage;
	
	private TrelloTaskWizard m_wizard;
	
	private Composite m_parent;
	
	private TrelloStringFieldEditor m_nameEditor;
	private TrelloStringFieldEditor m_descEditor;
	private TrelloStringFieldEditor m_dueEditor;
	private Button m_dueSelectionButton;
	private Button m_completedButton;
	
	private CalendarDialog m_calendarDialog;
	
	private String m_trelloDue = null;
	
	private TaskRepository m_taskRepository;
	private TrelloTaskDataHandler m_taskDataHandler = new TrelloTaskDataHandler();
	
	private TrelloTaskMapping m_taskSelection;
	
	//Чек-листы
	
	@Override
	public void createControl(Composite a_parent)
	{
		//m_parent = a_parent;
		m_parent = new Composite (a_parent, SWT.BORDER);
		
		Shell shell = m_parent.getShell();
		
		m_nameEditor = new TrelloStringFieldEditor(m_parent,"Name:", false);
		m_nameEditor.setStringValueChangeListener(SWT.Modify, new Listener ()
		{
			@Override
			public void handleEvent(Event a_event)
			{
				m_errorMessage = null;
				if (m_nameEditor.getStringValue().equals(""))
				{
					m_errorMessage = m_enterNameErrorMessage;
				}
					
				IWizardContainer container = m_wizard.getContainer();
				container.updateMessage();
				container.updateButtons();
			}
		});
		
		m_dueEditor = new TrelloStringFieldEditor(m_parent, "Due:", false);
		m_dueEditor.setEditable(false);
		m_dueEditor.setStringValueChangeListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(Event a_event)
			{
				
			}
		});
		
		GridData data = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		data.horizontalSpan = 2;
		
		m_dueSelectionButton = createButton(SWT.PUSH, "Select due...", data);
		m_dueSelectionButton.addSelectionListener(new SelectionListener ()
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				if (m_calendarDialog == null || m_calendarDialog.isDisposed())
				{
					IResultHandler handler = new IResultHandler ()
					{
						@Override
						public void finish(String a_trelloDue, String a_dueForShow)
						{
							m_trelloDue = a_trelloDue;
							m_dueEditor.setStringValue(a_dueForShow);
						}
					};
					m_calendarDialog = new CalendarDialog(m_parent.getShell(), handler);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
				
			}
		});
		
		m_completedButton = createButton(SWT.CHECK, "Completed", data);
		
		m_descEditor = new TrelloStringFieldEditor(m_parent, "Desc:", true);
		
		shell.setText(m_title);
		shell.pack();
		Point size = shell.getSize();
		shell.setMinimumSize(size.x, size.y + 15);
	}
	
	private Button createButton (int a_style, String a_text, GridData a_data)
	{
		Button button = new Button (m_parent, a_style);	
		button.setLayoutData(a_data);
		button.setText(a_text);
		return button;
	}
	
	@Override
	public void dispose()
	{
		if (m_parent != null)
		{
			m_parent.dispose();
		}
	}

	@Override
	public Control getControl()
	{
		return m_parent;
	}

	@Override
	public String getDescription()
	{
		return m_description;
	}

	@Override
	public String getErrorMessage()
	{
		return m_errorMessage;
	}

	@Override
	public Image getImage()
	{
		return null;
	}

	@Override
	public String getMessage()
	{
		return m_description;
	}

	@Override
	public String getTitle()
	{
		return m_title;
	}

	@Override
	public void performHelp()
	{
		
	}

	@Override
	public void setDescription(String a_description)
	{
		m_description = a_description;
	}

	@Override
	public void setImageDescriptor(ImageDescriptor a_imageDescriptor)
	{
		
	}

	@Override
	public void setTitle(String a_title)
	{
		m_title = a_title;
	}

	@Override
	public void setVisible(boolean a_visible)
	{
		m_parent.setVisible(a_visible);
	}

	@Override
	public boolean canFlipToNextPage()
	{
		return false;
	}

	@Override
	public String getName()
	{
		return m_name;
	}

	@Override
	public IWizardPage getNextPage()
	{
		return null;
	}

	@Override
	public IWizardPage getPreviousPage()
	{
		return null;
	}

	@Override
	public IWizard getWizard()
	{
		return m_wizard;
	}

	@Override
	public boolean isPageComplete()
	{
		if (m_errorMessage == null)
		{
			m_taskSelection.setSummary(m_nameEditor.getStringValue());
			TaskData taskData = new TaskData(m_taskDataHandler.getAttributeMapper(m_taskRepository),
					m_taskRepository.getConnectorKind(), m_taskRepository.getRepositoryUrl(),
					"2334gq34g3434qgq344q34g");
			taskData.setPartial(true);
			m_taskSelection.setTaskData(taskData);
			return true;
		}
		return false;
	}

	@Override
	public void setPreviousPage(IWizardPage a_page)
	{
		
	}

	@Override
	public void setWizard(IWizard a_newWizard)
	{
		m_wizard = (TrelloTaskWizard) a_newWizard;
		m_taskRepository = m_wizard.getTaskRepository();
		m_taskSelection = (TrelloTaskMapping)m_wizard.getTaskSelection();
	}
}
