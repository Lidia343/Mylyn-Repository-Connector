package mylyn.trello.ui.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import mylyn.trello.ui.edit.TrelloStringFieldEditor;

public class TrelloTaskWizardPage implements IWizardPage
{	
	private String m_title = "New Task";
	private String m_name = "Task";
	private String m_description = "Enter task parameters";
	private String m_errorMessage = "";
	
	private IWizard m_wizard;
	
	private Composite m_parent;
	
	private TrelloStringFieldEditor m_nameEditor;
	private TrelloStringFieldEditor m_descEditor;
	private TrelloStringFieldEditor m_dueEditor;
	private TrelloStringFieldEditor m_dueCompleteEditor;
	
	//Чек-листы
	//Размер
	
	@Override
	public void createControl(Composite a_parent)
	{
		m_parent = a_parent;
		
		m_parent.getShell().setText(m_title);
		
		m_nameEditor = new TrelloStringFieldEditor(m_parent, "Name:");
		m_descEditor = new TrelloStringFieldEditor(m_parent, "Description:");
		m_dueEditor = new TrelloStringFieldEditor(m_parent, "Due:");
		m_dueCompleteEditor = new TrelloStringFieldEditor(m_parent, "Due complete:");
		
		Shell shell = m_parent.getShell();
		shell.pack();
		shell.setMinimumSize(shell.getSize());
	}

	
	
	@Override
	public void dispose()
	{
		
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
		return m_errorMessage;
	}

	@Override
	public String getTitle()
	{
		return m_title;
	}

	@Override
	public void performHelp()
	{
		System.out.println("help");
	}

	@Override
	public void setDescription(String a_description)
	{
		m_description = a_description;
	}

	@Override
	public void setImageDescriptor(ImageDescriptor a_image)
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
		return false;
	}

	@Override
	public void setPreviousPage(IWizardPage a_page)
	{
		
	}

	@Override
	public void setWizard(IWizard a_newWizard)
	{
		m_wizard = a_newWizard;
	}
}
