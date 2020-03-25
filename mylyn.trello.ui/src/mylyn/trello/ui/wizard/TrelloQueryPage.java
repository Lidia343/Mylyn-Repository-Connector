package mylyn.trello.ui.wizard;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import trello.core.*;


public class TrelloQueryPage extends AbstractRepositoryQueryPage2
{
	private final static String PAGE_NAME = "TrelloQueryPage";  //Убрать static
	private final static String PAGE_TITLE = "Enter query parameters";
	private final static String PAGE_DESCRIPTION = "If_attributes_are_blank_or_stale_press_the_Update_button";
	
	private final AbstractRepositoryConnector m_connector;
	
	private Composite m_baseComposite;
	private Composite m_cardParametersComposite;
	
	//getQueryTitle()
	
	private Combo m_membersForSelect;
	private Combo m_boardsForSelect;
	private Combo m_listsForSelect;
	
	private Combo m_memberFields;
	private Combo m_boardFields;
	private Combo m_listFields;
	
	private Combo m_selectedMembers;
	private Combo m_selectedBoards;
	private Combo m_selectedLists;
	
	private Button m_memberClearing;
	private Button m_boardClearing;
	private Button m_listClearing;
	
	private Button m_closedBoardsButton;
	private Button m_closedListsButton;
	
	private Button m_nonArchivedCardsButton;
	private Button m_bothArchivedCardsButton;
	private Button m_onlyArchivedCardsButton;
	
	private Button m_nonCompletedCardsButton;
	private Button m_bothCompletedCardsButton;
	private Button m_onlyCompletedCardsButton;

	private Combo m_dueDate;
	
	private Button m_checkListMovingButton;
	
	public TrelloQueryPage(TaskRepository a_repository, IRepositoryQuery a_query)
	{
		super(PAGE_NAME, a_repository, a_query);
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
		setNeedsClear(true);
		m_connector = TasksUi.getRepositoryConnector(getTaskRepository().getConnectorKind());
	}
	
	@Override
	protected void doRefreshControls()
	{
		
	}

	@Override
	protected boolean hasRepositoryConfiguration()
	{
		return false;
	}

	@Override
	protected boolean restoreState(@NonNull IRepositoryQuery a_query)
	{
		return false;
	}

	@Override
	public void applyTo(@NonNull IRepositoryQuery a_query)
	{
		a_query.setUrl(getQueryUrl(getTaskRepository().getRepositoryUrl()));
		a_query.setSummary(getQueryTitle());

	}

	private String getQueryUrl(String a_repositoryUrl)
	{
		return null;
	}

	@Override
	protected void createPageContent(SectionComposite a_composite)
	{
		Composite control = a_composite.getContent();

		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		control.setLayout(layout);
		
		createBaseGroup(control);
		createCardParametersGroup(control);
	}
	
	private GridData createGridData(int a_horAl, boolean a_grab, int a_horSpan) 
	{
		GridData g = new GridData();
		g.horizontalAlignment = a_horAl; 
		g.grabExcessHorizontalSpace = a_grab; 
		if (a_horSpan != 0) g.horizontalSpan = a_horSpan; 
		return g;
	}
	
	private void createGridLayout(Composite a_composite, int a_numColumns, boolean makeColumnsEqualWidth) 
	{
		GridLayout g = new GridLayout(a_numColumns, makeColumnsEqualWidth);
		a_composite.setLayout(g);
	}
	
	private void createBaseGroup(Composite a_control)
	{
		m_baseComposite = new Composite(a_control, SWT.BORDER);
		GridData g = createGridData(SWT.FILL, true, 0);
		m_baseComposite.setLayoutData(g);
		createGridLayout(m_baseComposite, 5, false);
		
		createMemberGroup();
		createBoardGroup();
		createListGroup();
		
		m_closedBoardsButton = new Button (m_baseComposite, SWT.CHECK);
		g = createGridData (SWT.RIGHT, false, 5);
		m_closedBoardsButton.setLayoutData(g);
		m_closedBoardsButton.setText("See also archived boards");
		
		m_closedListsButton = new Button (m_baseComposite, SWT.CHECK);
		g = createGridData (SWT.RIGHT, false, 5);
		m_closedListsButton.setLayoutData(g);
		m_closedListsButton.setText("See also archived lists");
	}
	
	private void addFieldsAndSetText(Combo combo, String[] fields, String text)
	{
		if (combo == null) return;
		for (String f : fields)
		{
			combo.add(f);
		}
		combo.setText(text);
	}
	
	private void createMemberGroup()
	{
		Label groupLabel = new Label (m_baseComposite, SWT.NONE);
		groupLabel.setText("Members:");
		GridData g = createGridData (SWT.RIGHT, false, 0);
		groupLabel.setLayoutData(g);
		
		m_membersForSelect = new Combo(m_baseComposite, SWT.DROP_DOWN);
		g = createGridData (SWT.FILL, true, 0);
		m_membersForSelect.setLayoutData(g);
		m_membersForSelect.add("All if they exist");
		m_membersForSelect.setText("All if they exist");
		
		m_memberFields = new Combo(m_baseComposite, SWT.DROP_DOWN);
		g = createGridData (SWT.FILL, true, 0);
		m_memberFields.setLayoutData(g);
		
		m_selectedMembers = new Combo(m_baseComposite, SWT.DROP_DOWN);
		g = createGridData (SWT.FILL, true, 0);
		m_selectedMembers.setLayoutData(g);
		
		m_memberClearing = new Button (m_baseComposite, SWT.PUSH);
		g = createGridData (SWT.LEFT, false, 0);
		m_memberClearing.setLayoutData(g);
		m_memberClearing.setText("Clear list");
		if (m_memberFields != null) 
		{
			addFieldsAndSetText(m_memberFields, new String [] {"FullName:", "Username:", "Email:"}, "FullName:");
		}
	}
	
	private void createBoardGroup()
	{
		Label groupLabel = new Label (m_baseComposite, SWT.NONE);
		groupLabel.setText("Boards:");
		GridData g = createGridData (SWT.RIGHT, false, 0);
		groupLabel.setLayoutData(g);
		
		m_boardsForSelect = new Combo(m_baseComposite, SWT.DROP_DOWN);
		g = createGridData (SWT.FILL, true, 0);
		m_boardsForSelect.setLayoutData(g);
		m_boardsForSelect.add("All if they exist");
		m_boardsForSelect.setText("All if they exist");
		
		m_boardFields = new Combo(m_baseComposite, SWT.DROP_DOWN);
		g = createGridData (SWT.FILL, true, 0);
		m_boardFields.setLayoutData(g);
		
		m_selectedBoards = new Combo(m_baseComposite, SWT.DROP_DOWN);
		g = createGridData (SWT.FILL, true, 0);
		m_selectedBoards.setLayoutData(g);
		
		m_boardClearing = new Button (m_baseComposite, SWT.PUSH);
		g = createGridData (SWT.RIGHT, false, 0);
		m_boardClearing.setLayoutData(g);
		m_boardClearing.setText("Clear list");
		
		if (m_boardFields != null) 
		{
			addFieldsAndSetText(m_boardFields, new String [] {"Name:", "URL:", "ID:"}, "Name:");
		}
	}
	
	private void createListGroup()
	{
		Label groupLabel = new Label (m_baseComposite, SWT.NONE);
		groupLabel.setText("Lists:");
		GridData g = createGridData (SWT.RIGHT, false, 0);
		groupLabel.setLayoutData(g);
		
		m_listsForSelect = new Combo(m_baseComposite, SWT.DROP_DOWN);
		g = createGridData (SWT.FILL, true, 0);
		m_listsForSelect.setLayoutData(g);
		m_listsForSelect.add("All if they exist");
		m_listsForSelect.setText("All if they exist");
		
		m_listFields = new Combo(m_baseComposite, SWT.DROP_DOWN);
		g = createGridData (SWT.FILL, true, 0);
		m_listFields.setLayoutData(g);
		
		m_selectedLists = new Combo(m_baseComposite, SWT.DROP_DOWN);
		g = createGridData (SWT.FILL, true, 0);
		m_selectedLists.setLayoutData(g);
		
		m_listClearing = new Button (m_baseComposite, SWT.PUSH);
		g = createGridData (SWT.RIGHT, false, 0);
		m_listClearing.setLayoutData(g);
		m_listClearing.setText("Clear list");
		
		if (m_listFields != null) 
		{
			addFieldsAndSetText(m_listFields, new String [] {"Name:", "ID:"}, "Name:");
		}
	}
	
	private void createCardParametersGroup(Composite a_control)
	{
		m_cardParametersComposite = new Composite(a_control, SWT.BORDER);
		GridData g = createGridData(SWT.FILL, true, 0);
		m_cardParametersComposite.setLayoutData(g);
		createGridLayout(m_cardParametersComposite, 2, false);
		
		Label cardParametersLabel = new Label (m_cardParametersComposite, SWT.NONE);
		cardParametersLabel.setText("Card selection parameters:");
		g = createGridData (SWT.LEFT, false, 2);
		cardParametersLabel.setLayoutData(g);
		
		Composite dueComposite = new Composite(m_cardParametersComposite, SWT.NONE);
		g = createGridData(SWT.FILL, true, 2);
		dueComposite.setLayoutData(g);
		createGridLayout(dueComposite, 2, false);
				
		Label dueLabel = new Label (dueComposite, SWT.NONE);
		dueLabel.setText("Due:");
		g = createGridData (SWT.RIGHT, false, 0);
		dueLabel.setLayoutData(g);
		
		m_dueDate = new Combo(dueComposite, SWT.DROP_DOWN);
		g = createGridData (SWT.FILL, true, 0);
		m_dueDate.setLayoutData(g);
		m_dueDate.add("No");
		m_dueDate.add("All");
		m_dueDate.add("Select in calendar");
		m_dueDate.setText("No");
		
		m_nonArchivedCardsButton = new Button (m_cardParametersComposite, SWT.CHECK);
		g = createGridData (SWT.LEFT, false, 0);
		m_nonArchivedCardsButton.setLayoutData(g);
		m_nonArchivedCardsButton.setText("Only non-archived cards");
		m_nonArchivedCardsButton.setSelection(true);
		
		m_nonCompletedCardsButton = new Button (m_cardParametersComposite, SWT.CHECK);
		g = createGridData (SWT.LEFT, false, 0);
		m_nonCompletedCardsButton.setLayoutData(g);
		m_nonCompletedCardsButton.setText("Only non-completed cards");
		
		m_bothArchivedCardsButton = new Button (m_cardParametersComposite, SWT.CHECK);
		g = createGridData (SWT.LEFT, false, 0);
		m_bothArchivedCardsButton.setLayoutData(g);
		m_bothArchivedCardsButton.setText("Archived and non-archived cards");
		
		m_bothCompletedCardsButton = new Button (m_cardParametersComposite, SWT.CHECK);
		g = createGridData (SWT.LEFT, false, 0);
		m_bothCompletedCardsButton.setLayoutData(g);
		m_bothCompletedCardsButton.setText("Completed and non-completed cards");
		m_bothCompletedCardsButton.setSelection(true);
		
		m_onlyArchivedCardsButton = new Button (m_cardParametersComposite, SWT.CHECK);
		g = createGridData (SWT.LEFT, false, 0);
		m_onlyArchivedCardsButton.setLayoutData(g);
		m_onlyArchivedCardsButton.setText("Only archived cards");
		
		m_onlyCompletedCardsButton = new Button (m_cardParametersComposite, SWT.CHECK);
		g = createGridData (SWT.LEFT, false, 0);
		m_onlyCompletedCardsButton.setLayoutData(g);
		m_onlyCompletedCardsButton.setText("Only completed cards");
		
		m_checkListMovingButton = new Button (m_cardParametersComposite, SWT.CHECK);
		g = createGridData (SWT.LEFT, false, 0);
		m_checkListMovingButton.setLayoutData(g);
		m_checkListMovingButton.setText("Also move check-lists if they exist");
		m_checkListMovingButton.setSelection(true);
	}
}
