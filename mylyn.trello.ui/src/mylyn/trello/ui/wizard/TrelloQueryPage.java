package mylyn.trello.ui.wizard;

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
import trello.core.connection.ITrelloConnection;
import trello.core.connection.TrelloConnection;
import trello.core.model.Board;
import trello.core.model.CardList;
import trello.core.model.Member;

public class TrelloQueryPage extends AbstractRepositoryQueryPage2
{
	private final static String PAGE_NAME = "TrelloQueryPage";
	private final String m_boards = "boards";
	private final String m_members = "members";
	private final String m_lists = "lists";
	private final String m_pageTitle = "Enter query parameters";
	private final String m_pageDescription = "If_attributes_are_blank_or_stale_press_the_Update_button";
	
	private final String m_defaultTrelloObjectSelectionText = "All if they exist";
	private final String m_fullName = "FullName:";
	private final String m_username = "Username:";
	private final String m_email = "Email:";
	private final String m_name = "Name:";
	private final String m_url = "URL:";
	private final String m_id = "ID:";
	private final String m_all = "(All)";
	private final String m_no = "(No)";
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
	
	private String m_oldValueComboText = "";
	private String m_oldFieldComboText = "";
	
	private TrelloConnection client;
	
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
	
	private int m_selectedMemberComboTextIndex = -1;
	private int m_selectedBoardComboTextIndex = -1;
	private int m_selectedListComboTextIndex = -1;
	
	private List<Member> m_allSugMembers = new ArrayList<>();
	private List<Board> m_allSugBoards;
	//private List<Member> m_sugMembers = new ArrayList<>();
	//private List<CardList> m_allSugLists = new ArrayList<>();
	
	private List<Board> m_allSelBoards = new ArrayList<>();
	private List<Member> m_allSelMembers = new ArrayList<>();
	private List<CardList> m_allSelLists = new ArrayList<>();
	
	private Shell m_parent;
	private CalendarDialog m_calendarDialog;
	private String m_dateAndTime = "";
	
	public TrelloQueryPage(TaskRepository a_repository, IRepositoryQuery a_query)
	{
		super(PAGE_NAME, a_repository, a_query);
		setTitle(m_pageTitle);
		setDescription(m_pageDescription);
		setNeedsClear(true);
		client = new TrelloConnection(ITrelloConnection.DEFAULT_KEY, ITrelloConnection.DEFAULT_TOKEN);
	}
	
	@Override
	protected void doRefreshControls()
	{
		try
		{
			clearCombo(m_suggestedBoardsCombo);
			clearCombo(m_suggestedListsCombo);
			clearCombo(m_suggestedMembersCombo);
			m_suggestedMembersCombo.add(m_defaultTrelloObjectSelectionText);
			
			clearCombo(m_members, m_selectedMembersCombo);
			clearCombo(m_boards, m_selectedBoardsCombo);
			clearCombo(m_lists, m_selectedListsCombo);
			
			m_allSugBoards = client.getBoardList().getBoards();
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
		a_query.setSummary(getQueryTitle());

	}

	private String getQueryUrl(String a_repositoryUrl)
	{
		
		return null;
	}

	private void clearCombo(String a_comboName, Combo a_combo)
	{
		a_combo.removeAll();
		
		if (a_comboName.equals(m_boards))
			m_allSelBoards.clear();
		
		if (a_comboName.equals(m_members))
			m_allSelMembers.clear();
		
		if (a_comboName.equals(m_lists))
			m_allSelLists.clear();
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
	
	private boolean setOldTextOnCombo (Combo a_combo, String a_text)
	{
		boolean contain = false;
		String text = a_combo.getText();
		
		for (String s : a_combo.getItems())
		{
			if (text.equals(s))
			{
				contain = true;
				break;
			}
		}
		if (!contain)
		{
			a_combo.setText(a_text);
		}
		return contain;
	}
	
	//////////////////////////////////////////////////////////////////////////
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
							setItemAndText(m_selectedListsCombo, list.getId(), i, m_selectedListComboTextIndex);
						
						if (listFieldText.equals(m_name))
							setItemAndText(m_selectedListsCombo, list.getName(), i, m_selectedListComboTextIndex);
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
			a_item = m_no;
		
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
				
				if (m_suggestedMembersCombo.getText().equals(m_defaultTrelloObjectSelectionText))
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
				if (selectionIndex == m_selectedMemberComboTextIndex) return;
				
				m_selectedMemberComboTextIndex = selectionIndex;
				m_oldValueComboText = m_selectedMembersCombo.getText();
				
				m_allSugBoards.clear();
				m_suggestedBoardsCombo.removeAll();
				m_suggestedBoardsCombo.add(m_defaultTrelloObjectSelectionText);

				String[] idBoards;
				if (m_selectedMembersCombo.getText().equals(m_defaultTrelloObjectSelectionText))
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
				if (m.getEmail().equals("")) m_selectedMembersCombo.add(m_no);
				else m_selectedMembersCombo.add(m.getEmail());
			}
		}
	}
	
	private void addDefaultItemIfComboIsEmpty (Combo a_combo)
	{
		if (a_combo.getItemCount() == 0) a_combo.add(m_defaultTrelloObjectSelectionText); 
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
				/*int selIndex = m_suggestedBoardsCombo.getSelectionIndex();
				
				clearCombo("", m_suggestedMembersCombo);
				clearCombo(m_suggestedListsCombo);
				
				if (selIndex != 0)
				{
					Board board = m_allSugBoards.get(selIndex - 1);
					String boardName = board.getName();
					String boardId = board.getId();
					String boardUrl = board.getUrl();
					
					if (!m_allSelBoards.contains(board))
					{
						m_allSelBoards.add(board);
						String boardFieldText = m_boardFieldsCombo.getText();
						
						m_selectedBoardComboTextIndex = m_allSelBoards.indexOf(board); //////////////////////////////////////////
						
						if (boardFieldText.equals(m_id))
						{
							m_selectedBoardsCombo.setText(boardId);
							m_selectedBoardsCombo.add(boardId);
						}
						
						if (boardFieldText.equals(m_name))
						{
							m_selectedBoardsCombo.setText(boardName);
							m_selectedBoardsCombo.add(boardName);
						}
							
						if (boardFieldText.equals(m_url))
						{
							m_selectedBoardsCombo.setText(boardUrl);
							m_selectedBoardsCombo.add(boardUrl);
						}
					}
				}*/
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
				m_selectedBoardComboTextIndex = m_selectedBoardsCombo.getSelectionIndex() - 1;
				m_oldValueComboText = m_selectedBoardsCombo.getText();
				/*m_selectedBoardComboTextIndex = m_selectedBoardsCombo.getSelectionIndex();
				
				m_sugMembers.clear();
				m_suggestedMembersCombo.removeAll();
				//List<Member> members = m_allSugMembers.get(m_allSelBoards.get(m_selectedBoardsCombo.getSelectionIndex()).getId());
				List<Member> members = new ArrayList<>();
				
				for (Member m : members)
				{
					for (Member u : m_sugMembers)
					{
						if (m.getId().equals(u.getId())) return;
					}
					
					m_sugMembers.add(m);
					
					String memberFieldText = m_memberFieldsCombo.getText();
					
					if (memberFieldText.equals(m_fullName))
					{
						m_suggestedMembersCombo.add(m.getFullName());
						m_suggestedMembersCombo.setText(m.getFullName());
					}
					
					if (memberFieldText.equals(m_username))
					{
						m_suggestedMembersCombo.add(m.getUsername());
						m_suggestedMembersCombo.setText(m.getUsername());
					}
						
					if (memberFieldText.equals(m_email))
					{
						m_suggestedMembersCombo.add(m.getEmail());
						m_suggestedMembersCombo.setText(m.getEmail());
					}
				}*/
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
	
	private void createArchivedGroup ()
	{
		m_archivedBoardsButton = new Button (m_baseComposite, SWT.CHECK);
		GridData g = createGridData (SWT.RIGHT, false, 5);
		m_archivedBoardsButton.setLayoutData(g);
		m_archivedBoardsButton.setText("See also archived boards");
		
		m_archivedListsButton = new Button (m_baseComposite, SWT.CHECK);
		g = createGridData (SWT.RIGHT, false, 5);
		m_archivedListsButton.setLayoutData(g);
		m_archivedListsButton.setText("See also archived lists");
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
		
		FinishHandler m_handler = new FinishHandler () 
		{
			@Override
			public void finish(String a_result)
			{
				m_dateAndTime = a_result;
				System.out.println(m_dateAndTime);
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
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
				
			}
		});
	}
}
