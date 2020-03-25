package mylyn.trello.ui.wizard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import trello.core.connection.ITrelloConnection;
import trello.core.connection.TrelloConnection;
import trello.core.model.Board;
import trello.core.model.CardList;
import trello.core.model.User;

public class TrelloQueryPage extends AbstractRepositoryQueryPage2
{
	private final static String PAGE_NAME = "TrelloQueryPage";  //Убрать static
	private final String pageTitle = "Enter query parameters";
	private final String pageDescription = "If_attributes_are_blank_or_stale_press_the_Update_button";
	
	private final String m_defaultTrelloObjectSelectionText = "All if they exist";
	private final String m_fullName = "FullName:";
	private final String m_username = "Username:";
	private final String m_email = "Email:";
	private final String m_name = "Name:";
	private final String m_url = "URL:";
	private final String m_id = "ID:";
	private final String m_all = "All";
	private final String m_no = "No";
	private final String m_calendar = "Select in calendar";
	private final String m_nonArchCards = "Only non-archived cards";
	private final String m_bothArchCards = "Archived and non-archived cards";
	private final String m_archCards = "Only archived cards";
	private final String m_bothCompCards = "Completed and non-completed cards";
	private final String m_nonCompCards = "Only non-completed cards";
	private final String m_compCards = "Only completed cards";
	
	private final String[] m_memberFiledSelections = {m_fullName, m_username, m_email};
	private final String[] m_boardFiledSelections = {m_name, m_url, m_id};
	private final String[] m_listFiledSelections = {m_name, m_id};
	private final String[] m_dueSelections = {m_all, m_no, m_calendar};
	private final String[] m_archiveSelections = {m_nonArchCards, m_bothArchCards,  m_archCards};
	private final String[] m_completingSelections = { m_bothCompCards, m_nonCompCards, m_compCards};
	
	private String[] m_suggestedMembers;
	private String[] m_suggestedBoards;
	private String[] m_suggestedLists;
	
	private String[] m_selectedMembers;
	private String[] m_selectedBoards;
	private String[] m_selectedLists;
	
	//getQueryTitle()
	//getConnector()
	
	private Composite m_baseComposite;
	private Composite m_cardParametersComposite;
	
	private Combo m_suggestedMembersCombo;
	private Combo m_suggestedBoardsCombo;
	private Combo m_suggestedListsCombo;
	
	private Combo m_memberFieldsCombo;
	private Combo m_boardFieldsCombo;
	private Combo m_listFieldsCombo;
	
	private Combo m_selectedMembersCombo;
	private Combo m_selectedBoardsCombo;
	private Combo m_selectedListsCombo;
	
	private Button m_memberClearingButton;
	private Button m_boardClearingButton;
	private Button m_listClearingButton;
	
	private Button m_archivedBoardsButton;
	private Button m_archivedListsButton;
	
	private Combo m_archivedCardSelectionsCombo;
	private Combo m_completedCardSelectionsCombo;

	private Combo m_dueDateCombo;
	
	private Button m_checkListMovingButton;
	
	private List<Board> m_allBoards;
	private List<User> m_allMembers;
	private List<CardList> m_allLists;
	
	public TrelloQueryPage(TaskRepository a_repository, IRepositoryQuery a_query)
	{
		super(PAGE_NAME, a_repository, a_query);
		setTitle(pageTitle);
		setDescription(pageDescription);
		setNeedsClear(true);
	}
	
	@Override
	protected void doRefreshControls()
	{
		try
		{
			String oldBoardId = "";
			String oldBoardName = "";
			
			String oldMemberEmail = "";
			String oldMemberName = "";
			
			String oldListId = "";
			String oldListName = "";
			
			if (m_allBoards != null)
			{
				Board oldBoard = m_allBoards.get(m_suggestedBoardsCombo.indexOf(m_suggestedBoardsCombo.getText()));
				oldBoardId = oldBoard.getId();
				oldBoardName = oldBoard.getName();
				
				if (m_allMembers != null)
				{
					User oldMember = m_allMembers.get(m_suggestedMembersCombo.indexOf(m_suggestedMembersCombo.getText()));
					oldMemberEmail = oldMember.getEmail();
					oldMemberName = oldMember.getFullName();
				}
				if (m_allLists != null)
				{
					CardList oldList = m_allLists.get(m_suggestedListsCombo.indexOf(m_suggestedListsCombo.getText()));
					oldListId = oldList.getId();
					oldListName = oldList.getName();
				}
			}
			
			m_suggestedBoardsCombo.removeAll();
			m_suggestedBoardsCombo.setText("");
			
			m_suggestedMembersCombo.removeAll();
			m_suggestedMembersCombo.setText(m_defaultTrelloObjectSelectionText);
			
			m_suggestedListsCombo.removeAll();
			m_suggestedListsCombo.setText(m_defaultTrelloObjectSelectionText);
			
			TrelloConnection client = new TrelloConnection(ITrelloConnection.DEFAULT_KEY, ITrelloConnection.DEFAULT_TOKEN);
			m_allBoards = client.getBoardList().getBoards();
			if (m_allBoards == null) return;
			
			//List<User> members
			
			for (Board b : m_allBoards)
			{
				m_suggestedBoardsCombo.add(b.getName());
				if (oldBoardId.length() > 0)
					if (b.getId().equals(oldBoardId)) 
					{
						m_suggestedBoardsCombo.setText(oldBoardName);
						m_allMembers = client.getMembers(b.getId());
						m_allLists = client.getCardLists(b.getId());
						for (User m : m_allMembers)
						{
							m_suggestedMembersCombo.add(m.getFullName());
							if (m.getEmail().equals(oldMemberEmail))
							{
								m_suggestedMembersCombo.setText(oldMemberName);
							}
						}
						for (CardList l : m_allLists)
						{
							m_suggestedListsCombo.add(l.getName());
							if (l.getId().equals(oldListId))
							{
								m_suggestedListsCombo.setText(oldListName);
							}
						}
					}
			}
			m_allMembers = client.getMembers(m_allBoards.get(m_suggestedBoardsCombo.indexOf(m_suggestedBoardsCombo.getText())).getId());
			m_allLists = client.getCardLists(m_allBoards.get(m_suggestedBoardsCombo.indexOf(m_suggestedBoardsCombo.getText())).getId());
		}
		catch(IOException e)
		{
			setMessage(e.getMessage());
		}
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

	private void clearCombo(Combo a_combo)
	{
		a_combo.removeAll();
		a_combo.setText("");
	}
	
	@Override
	public void doClearControls()
	{
		m_suggestedMembersCombo.setText(m_defaultTrelloObjectSelectionText);
		m_suggestedBoardsCombo.setText(m_defaultTrelloObjectSelectionText);
		m_suggestedListsCombo.setText(m_defaultTrelloObjectSelectionText);
		
		m_memberFieldsCombo.setText(m_fullName);
		m_boardFieldsCombo.setText(m_name);
		m_listFieldsCombo.setText(m_name);
		
		clearCombo(m_selectedMembersCombo);
		clearCombo(m_selectedBoardsCombo);
		clearCombo(m_selectedListsCombo);
		
		if (m_archivedBoardsButton.getSelection()) m_archivedBoardsButton.setSelection(false);
		if (m_archivedListsButton.getSelection()) m_archivedListsButton.setSelection(false);
		
		m_dueDateCombo.setText(m_all);
		m_archivedCardSelectionsCombo.setText(m_nonArchCards);
		m_completedCardSelectionsCombo.setText(m_bothCompCards);
		
		if (!m_checkListMovingButton.getSelection()) m_checkListMovingButton.setSelection(true);
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
		
		createBoardGroup();
		createMemberGroup();
		createListGroup();
		
		m_archivedBoardsButton = new Button (m_baseComposite, SWT.CHECK);
		g = createGridData (SWT.RIGHT, false, 5);
		m_archivedBoardsButton.setLayoutData(g);
		m_archivedBoardsButton.setText("See also archived boards");
		
		m_archivedListsButton = new Button (m_baseComposite, SWT.CHECK);
		g = createGridData (SWT.RIGHT, false, 5);
		m_archivedListsButton.setLayoutData(g);
		m_archivedListsButton.setText("See also archived lists");
	}
	
	private void setGroup(Label a_groupLabel, String a_name, Combo a_linesForSelect, Combo a_fieldLines, Combo a_selectedLines, Button a_cleaning, String[] a_fieldsForAdding)
	{
		a_groupLabel.setText(a_name);
		GridData g = createGridData (SWT.RIGHT, false, 0);
		a_groupLabel.setLayoutData(g);
		
		g = createGridData (SWT.FILL, true, 0);
		a_linesForSelect.setLayoutData(g);
		a_linesForSelect.add(m_defaultTrelloObjectSelectionText);
		a_linesForSelect.setText(m_defaultTrelloObjectSelectionText);
		
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
		m_suggestedMembersCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_memberFieldsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_selectedMembersCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_memberClearingButton = new Button (m_baseComposite, SWT.PUSH);
		setGroup(groupLabel, "Members:", m_suggestedMembersCombo, m_memberFieldsCombo, m_selectedMembersCombo, m_memberClearingButton, m_memberFiledSelections);
	}
	
	private void createBoardGroup()
	{
		Label groupLabel = new Label (m_baseComposite, SWT.NONE);
		m_suggestedBoardsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_boardFieldsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_selectedBoardsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_boardClearingButton = new Button (m_baseComposite, SWT.PUSH);
		setGroup(groupLabel, "Boards:", m_suggestedBoardsCombo, m_boardFieldsCombo, m_selectedBoardsCombo, m_boardClearingButton, m_boardFiledSelections);
	}
	
	private void createListGroup()
	{
		Label groupLabel = new Label (m_baseComposite, SWT.NONE);
		m_suggestedListsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_listFieldsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_selectedListsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_listClearingButton = new Button (m_baseComposite, SWT.PUSH);
		setGroup(groupLabel, "Lists:", m_suggestedListsCombo, m_listFieldsCombo, m_selectedListsCombo, m_listClearingButton, m_listFiledSelections);
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
		
		m_dueDateCombo = new Combo(dueComposite, SWT.DROP_DOWN);
		setCombo(m_dueDateCombo, g, m_dueSelections);
		
		m_archivedCardSelectionsCombo = new Combo(m_cardParametersComposite, SWT.DROP_DOWN);
		setCombo(m_archivedCardSelectionsCombo, g, m_archiveSelections);
		
		m_completedCardSelectionsCombo = new Combo(m_cardParametersComposite, SWT.DROP_DOWN);
		setCombo(m_completedCardSelectionsCombo, g, m_completingSelections);
		
		m_checkListMovingButton = new Button (m_cardParametersComposite, SWT.CHECK);
		g = createGridData (SWT.RIGHT, false, 2);
		m_checkListMovingButton.setLayoutData(g);
		m_checkListMovingButton.setText("Also move check-lists if they exist");
		m_checkListMovingButton.setSelection(true);
	}
}
