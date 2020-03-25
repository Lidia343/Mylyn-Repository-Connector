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
	
	private Combo m_archivedCardSelections;
	private Combo m_completedCardSelections;

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
	
	private void setGroup(Label a_groupLabel, String a_name, Combo a_linesForSelect, Combo a_fieldLines, Combo a_selectedLines, Button a_cleaning, String[] a_fieldsForAdding)
	{
		a_groupLabel.setText(a_name);
		GridData g = createGridData (SWT.RIGHT, false, 0);
		a_groupLabel.setLayoutData(g);
		
		g = createGridData (SWT.FILL, true, 0);
		a_linesForSelect.setLayoutData(g);
		a_linesForSelect.add("All if they exist");
		a_linesForSelect.setText("All if they exist");
		
		g = createGridData (SWT.FILL, true, 0);
		a_fieldLines.setLayoutData(g);
		
		g = createGridData (SWT.FILL, true, 0);
		a_selectedLines.setLayoutData(g);
		
		g = createGridData (SWT.LEFT, false, 0);
		a_cleaning.setLayoutData(g);
		a_cleaning.setText("Clear list");
		addFieldsAndSetText(a_fieldLines, a_fieldsForAdding, a_fieldsForAdding[0]);
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
		m_membersForSelect = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_memberFields = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_selectedMembers = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_memberClearing = new Button (m_baseComposite, SWT.PUSH);
		setGroup(groupLabel, "Members:", m_membersForSelect, m_memberFields, m_selectedMembers, m_memberClearing, new String [] {"FullName:", "Username:", "Email:"});
	}
	
	private void createBoardGroup()
	{
		Label groupLabel = new Label (m_baseComposite, SWT.NONE);
		m_boardsForSelect = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_boardFields = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_selectedBoards = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_boardClearing = new Button (m_baseComposite, SWT.PUSH);
		setGroup(groupLabel, "Boards:", m_boardsForSelect, m_boardFields, m_selectedBoards, m_boardClearing, new String [] {"Name:", "URL:", "ID:"});
	}
	
	private void createListGroup()
	{
		Label groupLabel = new Label (m_baseComposite, SWT.NONE);
		m_listsForSelect = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_listFields = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_selectedLists = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_listClearing = new Button (m_baseComposite, SWT.PUSH);
		setGroup(groupLabel, "Lists:", m_listsForSelect, m_listFields, m_selectedLists, m_listClearing, new String [] {"Name:", "ID:"});
	}
	
	private void setCombo(Combo a_combo, GridData a_g, String[] a_fieldsForAdding)
	{
		a_g = createGridData (SWT.FILL, true, 0);
		a_combo.setLayoutData(a_g);
		for (String f : a_fieldsForAdding)
		{
			a_combo.add(f);
		}
		a_combo.setText(a_fieldsForAdding[0]);
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
		setCombo(m_dueDate, g, new String[] {"All", "No", "Select in calendar"});
		
		m_archivedCardSelections = new Combo(m_cardParametersComposite, SWT.DROP_DOWN);
		setCombo(m_archivedCardSelections, g, new String[] {"Only non-archived cards", "Archived and non-archived cards", "Only archived cards"});
		
		m_completedCardSelections = new Combo(m_cardParametersComposite, SWT.DROP_DOWN);
		setCombo(m_completedCardSelections, g, new String[] {"Completed and non-completed cards", "Only non-completed cards", "Only completed cards"});
		
		m_checkListMovingButton = new Button (m_cardParametersComposite, SWT.CHECK);
		g = createGridData (SWT.RIGHT, false, 2);
		m_checkListMovingButton.setLayoutData(g);
		m_checkListMovingButton.setText("Also move check-lists if they exist");
		m_checkListMovingButton.setSelection(true);
	}
}
