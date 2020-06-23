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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import mylyn.trello.ui.calendar.CalendarDialog;
import mylyn.trello.ui.handle.FinishHandler;
import trello.core.TrelloRepositoryConnector;
import trello.core.client.ITrelloClient;
import trello.core.client.TrelloClient;
import trello.core.model.Board;
import trello.core.model.CardList;
import trello.core.model.Member;
import trello.core.util.TrelloUtil;

public class TrelloQueryPage extends AbstractRepositoryQueryPage2
{
	private final static String PAGE_NAME = "TrelloQueryPage";
	private final String m_boards = "boards";
	private final String m_members = "members";
	private final String m_lists = "lists";
	private final String m_pageTitle = "Enter query parameters";
	private final String m_pageDescription = "If_attributes_are_blank_or_stale_press_the_Update_button";
	
	private final String m_fullName = "FullName:";
	private final String m_username = "Username:";
	private final String m_email = "Email:";
	private final String m_name = "Name:";
	private final String m_url = "URL:";
	private final String m_id = "ID:";
	private final String m_calendar = "Select in calendar";
	
	private final String[] m_memberFiledSelections = {m_fullName, m_username, m_email};
	private final String[] m_boardFiledSelections = {m_name, m_url, m_id};
	private final String[] m_listFiledSelections = {m_name, m_id};
	private final String[] m_dueSelections = {TrelloRepositoryConnector.ALL, TrelloRepositoryConnector.NO, m_calendar};
	private final String[] m_closingSelections = {TrelloRepositoryConnector.NON_CLOSED_CARDS, TrelloRepositoryConnector.CLOSED_AND_NON_CLOSED_CARDS,  TrelloRepositoryConnector.CLOSED_CARDS};
	private final String[] m_completingSelections = {TrelloRepositoryConnector.COMPLETED_AND_NON_COMPLETED_CARDS, TrelloRepositoryConnector.NON_COMPLETED_CARDS, TrelloRepositoryConnector.COMPLETED_CARDS};
	
	private ArrayList<String> m_namesOfListIds = new ArrayList<>();
	private ArrayList<String> m_listIds = new ArrayList<>();
	
	private String m_oldValueComboText = "";
	private String m_oldFieldComboText = "";
	
	private TrelloClient client;
	
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
	
	private Button m_closedBoardsButton;
	private Button m_closedListsButton;
	
	private Combo m_closedCardSelectionsCombo;
	private Combo m_completedCardSelectionsCombo;

	private Combo m_dueDateCombo;
	
	private Button m_checklistMovingButton;
	
	private int m_selectedMemberComboTextIndex = -1;
	private int m_selectedBoardComboTextIndex = -1;
	private int m_selectedListComboTextIndex = -1;
	
	private List<Member> m_allSugMembers = new ArrayList<>();
	private List<Board> m_allSugBoards;
	private List<CardList> m_allSugLists = new ArrayList<>();
	
	private List<Board> m_allSelBoards = new ArrayList<>();
	private List<Member> m_allSelMembers = new ArrayList<>();
	private List<CardList> m_allSelLists = new ArrayList<>();
	
	private boolean m_seeAlsoClosedBoards = false;
	private boolean m_seeAlsoClosedLists = false;
	
	private Shell m_parent;
	private CalendarDialog m_calendarDialog;
	private String m_dueDateAndTime = TrelloRepositoryConnector.ALL;
	
	String m_queryUrl = "";
	
	public TrelloQueryPage(TaskRepository a_repository, IRepositoryQuery a_query)
	{
		super(PAGE_NAME, a_repository, a_query);
		setTitle(m_pageTitle);
		setDescription(m_pageDescription);
		setNeedsClear(true);
		client = new TrelloClient(ITrelloClient.DEFAULT_KEY, ITrelloClient.DEFAULT_TOKEN);
	}
	
	/*
	@Override
	public boolean isPageComplete() {
		if (getTitle() != null && getTitle().length() > 0) {
			return true;
		}
		setMessage("Enter_a_title");
		return false;
	}*/
	
	@Override
	protected void doRefreshControls ()
	{
		try
		{
			clearCombo(m_suggestedBoardsCombo);
			clearCombo(m_suggestedListsCombo);
			clearCombo(m_suggestedMembersCombo);
			m_suggestedMembersCombo.add(TrelloRepositoryConnector.ALL);
			
			clearCombo(m_members, m_selectedMembersCombo);
			clearCombo(m_boards, m_selectedBoardsCombo);
			clearCombo(m_lists, m_selectedListsCombo);
			
			m_allSugBoards = client.getBoardList(m_seeAlsoClosedBoards).getBoards();
			List<String> boardIds = new ArrayList<>();
			
			for (Board b : m_allSugBoards)
			{
				boardIds.add(b.getId());
			}
			m_allSugMembers = client.getMembers(boardIds);
			for (Member m : m_allSugMembers)
			{
				m_suggestedMembersCombo.add(m.getFullName());
			}
		}
		catch (Exception e)
		{
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
		
		int listIdNumbers = 0;
		for (String s : m_listIds)
		{
			a_query.setAttribute(m_namesOfListIds.get(m_listIds.indexOf(s)), s);
			listIdNumbers++;
		}
		
		a_query.setAttribute(TrelloRepositoryConnector.LIST_ID_NUMBERS_QUERY_KEY, Integer.toString(listIdNumbers));
		a_query.setAttribute(TrelloRepositoryConnector.DUE_QUERY_KEY, m_dueDateAndTime);
		a_query.setAttribute(TrelloRepositoryConnector.CLOSED_QUERY_KEY, m_closedCardSelectionsCombo.getText());
		a_query.setAttribute(TrelloRepositoryConnector.COMPLETED_QUERY_KEY, m_completedCardSelectionsCombo.getText());
		a_query.setAttribute(TrelloRepositoryConnector.CHECKLISTS_QUERY_KEY, Boolean.toString(m_checklistMovingButton.getSelection()));
		
		a_query.setSummary(getQueryTitle());

	}

	private String getQueryUrl(String a_repositoryUrl)
	{
		m_queryUrl = a_repositoryUrl;
		return m_queryUrl;
	}

	private void clearCombo(String a_comboName, Combo a_combo)
	{
		a_combo.removeAll();
		
		if (a_comboName.equals(m_boards))
			m_allSelBoards.clear();
		
		if (a_comboName.equals(m_members))
			m_allSelMembers.clear();
		
		if (a_comboName.equals(m_lists))
		{
			m_allSelLists.clear();
			m_listIds.clear();
			m_namesOfListIds.clear();
		}
	}
	
	private void clearCombo(Combo a_combo)
	{
		a_combo.removeAll();
	}
	
	@Override
	public void doClearControls()
	{
		m_memberFieldsCombo.setText(m_fullName);
		m_boardFieldsCombo.setText(m_name);
		m_listFieldsCombo.setText(m_name);
		
		m_suggestedMembersCombo.setText("");
		m_suggestedBoardsCombo.setText("");
		m_suggestedListsCombo.setText("");
		
		clearCombo(m_boards, m_selectedMembersCombo);
		clearCombo(m_boards, m_selectedBoardsCombo);
		clearCombo(m_lists, m_selectedListsCombo);
		
		if (!TrelloUtil.contains(m_dueDateCombo.getText(), m_dueDateCombo.getItems())) 
		{
			m_dueDateCombo.setText(TrelloRepositoryConnector.ALL);
			m_dueDateAndTime = TrelloRepositoryConnector.ALL;
		}
	}
	
	@Override
	protected void createPageContent(SectionComposite a_composite)
	{
		m_parent = a_composite.getShell();
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
		createArchivedGroup();
	}
	
	private void setGroup (Label a_groupLabel, String a_groupName, Combo a_suggestedItemsCombo, Combo a_fieldItemsCombo, Combo a_selectedItemsCombo, 
			              Button a_clearingButton, String[] a_fieldsForAdding, SelectionListener a_clearingListener)
	{
		a_groupLabel.setText(a_groupName);
		GridData g = createGridData (SWT.RIGHT, false, 0);
		a_groupLabel.setLayoutData(g);
		
		g = createGridData (SWT.FILL, true, 0);
		a_suggestedItemsCombo.setLayoutData(g);
		
		g = createGridData (SWT.FILL, true, 0);
		a_fieldItemsCombo.setLayoutData(g);
		
		g = createGridData (SWT.FILL, true, 0);
		a_selectedItemsCombo.setLayoutData(g);
		
		g = createGridData (SWT.LEFT, false, 0);
		a_clearingButton.setLayoutData(g);
		a_clearingButton.setText("Clear list");
		addFieldsAndSetText(a_fieldItemsCombo, a_fieldsForAdding, a_fieldsForAdding[0]);
		
		a_clearingButton.addSelectionListener(a_clearingListener);
	}
	
	private void addFieldsAndSetText (Combo a_combo, String[] a_fields, String a_text)
	{
		if (a_combo == null) return;
		for (String f : a_fields)
		{
			a_combo.add(f);
		}
		a_combo.setText(a_text);
	}
	
	private SelectionListener getClearingListener (String a_comboName, Combo a_combo)
	{
		SelectionListener listener = new SelectionListener ()
		{
			@Override
			public void widgetSelected (SelectionEvent a_e)
			{
				clearCombo(a_comboName, a_combo);
			}

			@Override
			public void widgetDefaultSelected (SelectionEvent a_e)
			{
			}
		};
		return listener;
	}
	
	private void setOldTextOnCombo (Combo a_combo, String a_oldText)
	{
		String newText = a_combo.getText();
		if (!TrelloUtil.contains(newText, a_combo.getItems()))
			a_combo.setText(a_oldText);
	}
	
	private void addFocusComboListener (Combo a_combo)
	{
		a_combo.addFocusListener(new FocusListener() 
		{
			@Override
			public void focusGained(FocusEvent a_e)
			{
				m_oldValueComboText = a_combo.getText();
			}

			@Override
			public void focusLost(FocusEvent a_e)
			{
				setOldTextOnCombo(a_combo, m_oldValueComboText);
			}
		});
	}
	
	private void addFocusAndSelectionFieldsComboLisener (String a_comboName, Combo a_combo)
	{
		a_combo.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(FocusEvent a_e)
			{
				m_oldFieldComboText = a_combo.getText();
			}

			@Override
			public void focusLost(FocusEvent a_e)
			{
				setOldTextOnCombo(a_combo, m_oldFieldComboText);
			}
		});
		
		a_combo.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				if (a_comboName.equals(m_boards))
				{
					String boardFieldText = m_boardFieldsCombo.getText();
					
					if (boardFieldText.equals(m_oldFieldComboText)) return;
					
					m_oldFieldComboText = boardFieldText;
					if (m_selectedBoardsCombo.getText().equals(""))
					{
						m_selectedBoardComboTextIndex = -1;
					}
					
					for (int i = 0; i < m_allSelBoards.size(); i++)
					{
						Board board = m_allSelBoards.get(i);
						
						if (boardFieldText.equals(m_id))
							setItemAndText(m_selectedBoardsCombo, board.getId(), i, m_selectedBoardComboTextIndex);
						
						if (boardFieldText.equals(m_name))
							setItemAndText(m_selectedBoardsCombo, board.getName(), i, m_selectedBoardComboTextIndex);
							
						if (boardFieldText.equals(m_url))
							setItemAndText(m_selectedBoardsCombo, board.getUrl(), i, m_selectedBoardComboTextIndex);
					}
				}
				
				if (a_comboName.equals(m_members))
				{
					String memberFieldText = m_memberFieldsCombo.getText();
					
					if (memberFieldText.equals(m_oldFieldComboText)) return;
					
					m_oldFieldComboText = memberFieldText;
					
					if (m_selectedMembersCombo.getText().equals(""))
					{
						m_selectedMemberComboTextIndex = -1;
					}
					
					for (int i = 0; i < m_allSelMembers.size(); i++)
					{
						Member member = m_allSelMembers.get(i);
						
						if (memberFieldText.equals(m_fullName))
							setItemAndText(m_selectedMembersCombo, member.getFullName(), i, m_selectedMemberComboTextIndex);
						
						if (memberFieldText.equals(m_username))
							setItemAndText(m_selectedMembersCombo, member.getUsername(), i, m_selectedMemberComboTextIndex);
							
						if (memberFieldText.equals(m_email))
							setItemAndText(m_selectedMembersCombo, member.getEmail(), i, m_selectedMemberComboTextIndex);
						
					}
				}
				
				if (a_comboName.equals(m_lists))
				{
					String listFieldText = m_listFieldsCombo.getText();
					
					if (listFieldText.equals(m_oldFieldComboText)) return;
					
					m_oldFieldComboText = listFieldText;
					if (m_selectedListsCombo.getText().equals(""))
					{
						m_selectedListComboTextIndex = -1;
					}
					
					for (int i = 0; i < m_allSelLists.size(); i++)
					{
						CardList list = m_allSelLists.get(i);
						
						if (listFieldText.equals(m_id))
							setItemAndText(m_selectedListsCombo, list.getId(), i - 1, m_selectedListComboTextIndex);
						
						if (listFieldText.equals(m_name))
							setItemAndText(m_selectedListsCombo, list.getName(), i - 1, m_selectedListComboTextIndex);
					}
				}
			}
				
			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
			}
		});
	}
	
	private void setItemAndText (Combo a_combo, String a_item, int a_id, int a_index)
	{
		if (a_item.equals(""))
			a_item = TrelloRepositoryConnector.NO;
		
		a_combo.setItem(a_id + 1, a_item);
		if (a_id == a_index)
			a_combo.setText(a_item);
	}
	
	private void createMemberGroup()
	{
		Label groupLabel = new Label (m_baseComposite, SWT.NONE);
		m_suggestedMembersCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_memberFieldsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_selectedMembersCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_memberClearingButton = new Button (m_baseComposite, SWT.PUSH);
		SelectionListener listener = getClearingListener (m_members, m_selectedMembersCombo);
		
		setGroup(groupLabel, "Members:", m_suggestedMembersCombo, m_memberFieldsCombo, m_selectedMembersCombo, m_memberClearingButton, m_memberFiledSelections, listener);
		
		m_suggestedMembersCombo.addSelectionListener(new SelectionListener() 
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				m_oldValueComboText = m_suggestedMembersCombo.getText();
				
				addDefaultItemIfComboIsEmpty (m_selectedMembersCombo);
				
				if (m_suggestedMembersCombo.getText().equals(TrelloRepositoryConnector.ALL))
				{
					for (Member m : m_allSugMembers)
					{
						addUniqueMemberToSelectedCombo(m);
					}
				}
				else
				{
					Member m = m_allSugMembers.get(m_suggestedMembersCombo.getSelectionIndex() - 1);
					addUniqueMemberToSelectedCombo(m);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
			}
		});
		
		m_selectedMembersCombo.addSelectionListener(new SelectionListener ()
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				int selectionIndex = m_selectedMembersCombo.getSelectionIndex() - 1;
				if (selectionIndex != -1)
					if (selectionIndex == m_selectedMemberComboTextIndex) return;
				
				m_selectedMemberComboTextIndex = selectionIndex;
				m_oldValueComboText = m_selectedMembersCombo.getText();
				
				m_allSugBoards.clear();
				m_suggestedBoardsCombo.removeAll();
				m_suggestedBoardsCombo.add(TrelloRepositoryConnector.ALL);

				String[] idBoards;
				if (m_selectedMembersCombo.getText().equals(TrelloRepositoryConnector.ALL))
				{
					boolean contain;
					for (Member m : m_allSelMembers)
					{
						idBoards = m.getaIdBoards();
						
						for (int i = 0; i < idBoards.length; i++)
						{
							contain = false;
							for (Board b : m_allSugBoards)
							{
								if (idBoards[i].equals(b.getId())) contain = true;
							}
							
							if (!contain) addBoardToSuggestedCombo(idBoards[i]);
						}
					}
				}
				else
				{
					Member m = m_allSelMembers.get(m_selectedMemberComboTextIndex);
					idBoards = m.getaIdBoards();
					for (int i = 0; i < idBoards.length; i++)
					{
						addBoardToSuggestedCombo(idBoards[i]);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
			}
		});
		
		addFocusComboListener(m_suggestedMembersCombo);
		addFocusComboListener(m_selectedMembersCombo);
		addFocusAndSelectionFieldsComboLisener(m_members, m_memberFieldsCombo);
	}
	
	private void addBoardToSuggestedCombo (String a_boardId)
	{
		Board board = client.getBoard(a_boardId);
		if (!m_seeAlsoClosedBoards)
			if (Boolean.parseBoolean(board.getClosed()) == true)
				return;
		String name = board.getName();
		m_allSugBoards.add(board);
		m_suggestedBoardsCombo.add(name);
	}
	
	private void addUniqueMemberToSelectedCombo (Member m)
	{
		if (!m_allSelMembers.contains(m))
		{
			m_allSelMembers.add(m);
			
			String memberFieldText = m_memberFieldsCombo.getText();
			if (memberFieldText.equals(m_fullName)) m_selectedMembersCombo.add(m.getFullName());
			if (memberFieldText.equals(m_username)) m_selectedMembersCombo.add(m.getUsername());
			if (memberFieldText.equals(m_email)) 
			{
				if (m.getEmail().equals("")) m_selectedMembersCombo.add(TrelloRepositoryConnector.NO);
				else m_selectedMembersCombo.add(m.getEmail());
			}
		}
	}
	
	private void addDefaultItemIfComboIsEmpty (Combo a_combo)
	{
		if (a_combo.getItemCount() == 0) a_combo.add(TrelloRepositoryConnector.ALL); 
	}
	
	private void createBoardGroup()
	{
		Label groupLabel = new Label (m_baseComposite, SWT.NONE);
		m_suggestedBoardsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_boardFieldsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_selectedBoardsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_boardClearingButton = new Button (m_baseComposite, SWT.PUSH);
		SelectionListener listener = getClearingListener (m_boards, m_selectedBoardsCombo);
		setGroup(groupLabel, "Boards:", m_suggestedBoardsCombo, m_boardFieldsCombo, m_selectedBoardsCombo, m_boardClearingButton, m_boardFiledSelections, listener);
		
		m_suggestedBoardsCombo.addSelectionListener(new SelectionListener() 
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				m_oldValueComboText =  m_suggestedBoardsCombo.getText();
				
				addDefaultItemIfComboIsEmpty (m_selectedBoardsCombo);
				
				if (m_suggestedBoardsCombo.getText().equals(TrelloRepositoryConnector.ALL))
				{
					for (Board b : m_allSugBoards)
					{
						addUniqueBoardToSelectedCombo(b);
					}
				}
				else
				{
					Board b = m_allSugBoards.get(m_suggestedBoardsCombo.getSelectionIndex() - 1);
					addUniqueBoardToSelectedCombo(b);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
			}
		});
		
		m_selectedBoardsCombo.addSelectionListener(new SelectionListener ()
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				int selectionIndex = m_selectedBoardsCombo.getSelectionIndex() - 1;
				if (selectionIndex != -1)
					if (selectionIndex == m_selectedBoardComboTextIndex) return;
				
				m_selectedBoardComboTextIndex = selectionIndex;
				m_oldValueComboText = m_selectedBoardsCombo.getText();
				
				refreshListGroup();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
			}
		});
		
		addFocusComboListener(m_suggestedBoardsCombo);
		addFocusComboListener(m_selectedBoardsCombo);
		addFocusAndSelectionFieldsComboLisener(m_boards, m_boardFieldsCombo);
	}
	
	private void refreshListGroup ()
	{
		m_allSugLists.clear();
		m_suggestedListsCombo.removeAll();
		m_suggestedListsCombo.add(TrelloRepositoryConnector.ALL);

		try
		{
			List<CardList> lists;
			if (m_selectedBoardsCombo.getText().equals(TrelloRepositoryConnector.ALL))
			{
				boolean contain;
				for (Board b : m_allSelBoards)
				{
					lists = client.getCardLists(b.getId(), m_seeAlsoClosedLists);
					
					for (CardList l : lists)
					{
						contain = false;
						for (CardList c : m_allSugLists)
						{
							if (l.getId().equals(c.getId())) contain = true;
						}
						
						if (!contain) addListToSuggestedCombo(l);
					}
				}
			}
			else
			{
				if (m_selectedBoardComboTextIndex == -1) return;
				Board b = m_allSelBoards.get(m_selectedBoardComboTextIndex);
				lists = client.getCardLists(b.getId(), m_seeAlsoClosedLists);
				for (CardList l : lists)
				{
					addListToSuggestedCombo(l);
				}
			}
			if (m_suggestedListsCombo.getItemCount() == 1) m_suggestedListsCombo.removeAll();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void addUniqueBoardToSelectedCombo (Board a_b)
	{
		if (!m_allSelBoards.contains(a_b))
		{
			m_allSelBoards.add(a_b);
			
			String boardFieldText = m_boardFieldsCombo.getText();
			if (boardFieldText.equals(m_id)) m_selectedBoardsCombo.add(a_b.getId());
			if (boardFieldText.equals(m_name)) m_selectedBoardsCombo.add(a_b.getName());
			if (boardFieldText.equals(m_url))  m_selectedBoardsCombo.add(a_b.getUrl());
		}
	}
	
	private void addListToSuggestedCombo (CardList a_l)
	{
		String name = a_l.getName();
		m_allSugLists.add(a_l);
		m_suggestedListsCombo.add(name);
	}
	
	private void createListGroup()
	{
		Label groupLabel = new Label (m_baseComposite, SWT.NONE);
		m_suggestedListsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_listFieldsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_selectedListsCombo = new Combo(m_baseComposite, SWT.DROP_DOWN);
		m_listClearingButton = new Button (m_baseComposite, SWT.PUSH);
		SelectionListener listener = getClearingListener (m_lists, m_selectedListsCombo);
		setGroup(groupLabel, "Lists:", m_suggestedListsCombo, m_listFieldsCombo, m_selectedListsCombo, m_listClearingButton, m_listFiledSelections, listener);
		
		m_suggestedListsCombo.addSelectionListener(new SelectionListener() 
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				m_oldValueComboText =  m_suggestedListsCombo.getText();
				
				if (m_suggestedListsCombo.getText().equals(TrelloRepositoryConnector.ALL))
				{
					for (CardList l : m_allSugLists)
						addUniqueListToSelectedCombo(l);
				}
				else
				{
					CardList l = m_allSugLists.get(m_suggestedListsCombo.getSelectionIndex() - 1);
					addUniqueListToSelectedCombo(l);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
			}
		});
		
		m_selectedListsCombo.addSelectionListener(new SelectionListener ()
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				m_selectedListComboTextIndex = m_selectedListsCombo.getSelectionIndex();
				m_oldValueComboText = m_selectedListsCombo.getText();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
			}
		});
		
		addFocusComboListener(m_suggestedListsCombo);
		addFocusComboListener(m_selectedListsCombo);
		addFocusAndSelectionFieldsComboLisener(m_lists, m_listFieldsCombo);
	}
	
	private void addUniqueListToSelectedCombo (CardList a_l)
	{
		if (!m_allSelLists.contains(a_l))
		{
			m_allSelLists.add(a_l);
			
			addCardlistIdAndCardlistNameToLists(a_l.getId(), TrelloRepositoryConnector.LIST_ID_QUERY_KEY + Integer.toString(m_allSelLists.indexOf(a_l)));
			
			String listFieldText = m_listFieldsCombo.getText();
			if (listFieldText.equals(m_id)) 
			{
				addFieldsAndSetText (m_selectedListsCombo, new String[] {a_l.getId()}, a_l.getId());
			}
			if (listFieldText.equals(m_name)) 
			{
				addFieldsAndSetText (m_selectedListsCombo, new String[] {a_l.getName()}, a_l.getName());
			}
		}
	}
	
	private void addCardlistIdAndCardlistNameToLists (String a_id, String a_name)
	{
		m_listIds.add(a_id);
		m_namesOfListIds.add(a_name);
	}
	
	private void createArchivedGroup ()
	{
		m_closedBoardsButton = new Button (m_baseComposite, SWT.CHECK);
		GridData g = createGridData (SWT.RIGHT, false, 5);
		m_closedBoardsButton.setLayoutData(g);
		m_closedBoardsButton.setText("See also archived boards");
		
		m_closedListsButton = new Button (m_baseComposite, SWT.CHECK);
		g = createGridData (SWT.RIGHT, false, 5);
		m_closedListsButton.setLayoutData(g);
		m_closedListsButton.setText("See also archived lists");
		
		m_closedBoardsButton.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				if (m_closedBoardsButton.getSelection() == true)
					m_seeAlsoClosedBoards = true;
				else
					m_seeAlsoClosedBoards = false;
				doRefreshControls();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
			}
		});
		
		m_closedListsButton.addSelectionListener(new SelectionListener() 
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				if (m_closedListsButton.getSelection() == true)
					m_seeAlsoClosedLists = true;
				else
					m_seeAlsoClosedLists = false;
				
				m_allSelLists.clear();
				m_selectedListsCombo.removeAll();
				refreshListGroup();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
			}
		});
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
		
		m_closedCardSelectionsCombo = new Combo(m_cardParametersComposite, SWT.DROP_DOWN);
		setCombo(m_closedCardSelectionsCombo, g, m_closingSelections);
		
		m_completedCardSelectionsCombo = new Combo(m_cardParametersComposite, SWT.DROP_DOWN);
		setCombo(m_completedCardSelectionsCombo, g, m_completingSelections);
		
		m_checklistMovingButton = new Button (m_cardParametersComposite, SWT.CHECK);
		g = createGridData (SWT.RIGHT, false, 2);
		m_checklistMovingButton.setLayoutData(g);
		m_checklistMovingButton.setText("Also move checklists if they exist");
		m_checklistMovingButton.setSelection(true);
		
		FinishHandler m_handler = new FinishHandler () 
		{
			@Override
			public void finish(String a_trelloDateAndTime, String a_dateAndTimeForShow)
			{
				m_dueDateAndTime = a_trelloDateAndTime;
				m_dueDateCombo.setText(a_dateAndTimeForShow);
			}
		};
		
		m_dueDateCombo.addSelectionListener(new SelectionListener() 
		{
			@Override
			public void widgetSelected(SelectionEvent a_e)
			{
				if (m_dueDateCombo.getText().equals(m_calendar) && (m_calendarDialog == null || m_calendarDialog.isDisposed()))
				{
					m_calendarDialog = new CalendarDialog (m_parent, m_handler);
				}
				else
				{
					m_dueDateAndTime = m_dueDateCombo.getText();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
				
			}
		});
		
		addFocusComboListener(m_closedCardSelectionsCombo);
		addFocusComboListener(m_completedCardSelectionsCombo);
	}
}
