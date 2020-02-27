package graphics;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import connection.TrelloConnection;
import handle.NoConnectionResultHandler;
import model.Board;
import model.BoardList;
import model.Card;
import model.CardList;
import model.User;

/**
 * Класс предназначен для управления графическими компонентами приложения в зависимости от данных, получаемых с сайта, и действий пользователя.
 */
public class Graphics
{
	private Display m_display;
	private Shell m_shell;
	private Color m_blueColor;
	private Color m_whiteColor;
	private Color m_blackColor;
	private Font m_font;
	
	private Composite m_autComposite;
	private Label m_keyLabel;
	private Text m_keyText;
	private Label m_tokenLabel;
	private Text m_tokenText;
	private Button m_loginButton;
	
	private Composite m_userComposite;
	private Label m_userFullNameLabel;
	private Text m_userFullNameText;
	private Label m_userNameLabel;
	private Text m_userNameText;
	private Label m_userEmailLabel;
	private Text m_userEmailText;
	
	private Label m_urlLabel;
	private Text m_urlText;
	
	private Composite m_cardDescComposite;
	private Label m_cardDescLabel;
	private Text m_cardDescText;
	
	private Label m_boardLabel;
	
	private Composite m_boardTableComposite;
	private Table m_boardTable;
	private TableViewer m_boardTableViewer;
	
	private Composite m_cardTableComposite;
	private Table m_cardTable;
	private TableViewer m_cardTableViewer;
	
	private boolean m_firstRun;
	private boolean m_firstBoardChoice;
	
	private Map<String, List<CardList>> m_boardCardLists;
	
	private TrelloConnection m_connection;
	
	private List<CardList> m_cardLists;
	
	private int m_cardTableColumnCount;
	
	/**
	 * Конструктор класса Graphics.
	 */
	public Graphics()
	{
		m_display = new Display();
		m_shell = new Shell (m_display);
		m_blueColor = new Color (m_display, 40, 40, 70);
		m_whiteColor = new Color (m_display, 255, 255, 255);
		m_blackColor = new Color (m_display, 0, 0, 20);
		m_font = new Font (m_display, "Courier New", 15, SWT.NORMAL);
		m_firstRun = true;
		m_firstBoardChoice = true;
		m_connection = new TrelloConnection(noConnectHandler);
	}
	
	/**
	 * Метод для создания окна приложения со всеми его графическими компонентами.
	 */
	public void createWindow()
	{
		setShell();
		createGridLayout();
	
		m_autComposite = new Composite (m_shell, SWT.BORDER);
		GridData g = createGridData(SWT.FILL, true, 0, 0, 0, 2);
		m_autComposite.setBackground(m_blueColor);
		m_autComposite.setLayoutData(g);
		createGridLayout(m_autComposite, 2);
		
		m_keyLabel = new Label (m_autComposite, SWT.NONE);
		setLabel (m_keyLabel, "Ключ:");
		g = createGridData(SWT.LEFT, false, 0, 0, 0, 0);
		m_keyLabel.setLayoutData(g);
		
		m_keyText = new Text (m_autComposite, SWT.BORDER | SWT.PASSWORD);
		setText(m_keyText, true);
		g = createGridData(SWT.FILL, true, 300, 0, 0, 0);
		m_keyText.setLayoutData(g);
		
		m_tokenLabel = new Label (m_autComposite, SWT.NONE);
		setLabel (m_tokenLabel, "Токен:");
		g = createGridData(SWT.RIGHT, false, 0, 0, 0, 0);
		m_tokenLabel.setLayoutData(g);
		
		m_tokenText = new Text (m_autComposite, SWT.BORDER | SWT.PASSWORD);
		setText(m_tokenText, true);
		g = createGridData(SWT.FILL, true, 300, 0, 0, 0);
		m_tokenText.setLayoutData(g);
		
		m_loginButton = new Button (m_shell, SWT.PUSH);
		setButton (m_loginButton, "Войти");
		g = createGridData(SWT.RIGHT, true, 160, 0, 0, 2);
		m_loginButton.setLayoutData(g);
		m_loginButton.addSelectionListener(loginSelection);
		
		//----------------------------------------------------------
		m_userComposite = new Composite (m_shell, SWT.BORDER);
		g = createGridData(SWT.FILL, true, 0, 0, 0, 2);
		m_userComposite.setBackground(m_blueColor);
		m_userComposite.setLayoutData(g);
		createGridLayout(m_userComposite, 2);
		
		m_userFullNameLabel = new Label (m_userComposite, SWT.NONE);
		setLabel (m_userFullNameLabel, "Имя пользователя:");
		g = createGridData(SWT.LEFT, false, 0, 0, 0, 0);
		m_userFullNameLabel.setLayoutData(g);
		
		m_userFullNameText = new Text (m_userComposite, SWT.BORDER);
		setText(m_userFullNameText, false);
		g = createGridData(SWT.FILL, true, 300, 0, 0, 0);
		m_userFullNameText.setLayoutData(g);
		
		m_userNameLabel = new Label (m_userComposite, SWT.NONE);
		setLabel (m_userNameLabel, "Логин пользователя:");
		g = createGridData(SWT.LEFT, false, 0, 0, 0, 0);
		m_userNameLabel.setLayoutData(g);
		
		m_userNameText = new Text (m_userComposite, SWT.BORDER);
		setText(m_userNameText, false);
		g = createGridData(SWT.FILL, true, 300, 0, 0, 0);
		m_userNameText.setLayoutData(g);
		
		m_userEmailLabel = new Label (m_userComposite, SWT.NONE);
		setLabel (m_userEmailLabel, "Адрес электронной почты:");
		g = createGridData(SWT.LEFT, false, 0, 0, 0, 0);
		m_userEmailLabel.setLayoutData(g);
		
		m_userEmailText = new Text (m_userComposite, SWT.BORDER);
		setText(m_userEmailText, false);
		g = createGridData(SWT.FILL, true, 300, 0, 0, 0);
		m_userEmailText.setLayoutData(g);
		
		m_cardDescComposite = new Composite (m_shell, SWT.NONE);
		g = createGridData(SWT.FILL, true, 0, 0, 0, 2);
		m_cardDescComposite.setBackground(m_blueColor);
		m_cardDescComposite.setLayoutData(g);
		createGridLayout(m_cardDescComposite, 2);
		
		m_urlLabel = new Label (m_cardDescComposite, SWT.NONE);
		setLabel (m_urlLabel, "URL:");
		g = createGridData(SWT.LEFT, false, 200, 0, 100, 0);
		m_urlLabel.setLayoutData(g);
		
		m_urlText = new Text (m_cardDescComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		setText(m_urlText, false);
		g = createGridData(SWT.FILL, true, 700, 0, 0, 0);
		m_urlText.setLayoutData(g);
		
		m_cardDescLabel = new Label (m_cardDescComposite, SWT.NONE);
		setLabel (m_cardDescLabel, "Описание карточки:");
		g = createGridData(SWT.LEFT, false, 0, 0, 0, 0);
		g.verticalAlignment = SWT.BEGINNING;
		g.grabExcessVerticalSpace = false;
		m_cardDescLabel.setLayoutData(g);
		
		m_cardDescText = new Text (m_cardDescComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		setText(m_cardDescText, false);
		g = createGridData(SWT.FILL, true, 300, 100, 0, 0);
		m_cardDescText.setLayoutData(g);
		
		m_boardLabel = new Label (m_shell, SWT.NONE);
		setLabel (m_boardLabel, "Списки карточек:");
		g = createGridData(SWT.LEFT, false, 750, 0, 0, 2);
		m_boardLabel.setLayoutData(g);
		m_boardLabel.setVisible(false);
		
		m_cardTableComposite = new Composite (m_shell, SWT.BORDER);
		g = createGridData(SWT.FILL, true, 400, 0, 0, 0);
		g.verticalAlignment = SWT.FILL;
		g.grabExcessVerticalSpace = true;
		m_cardTableComposite.setLayoutData(g);
		
		m_boardTableComposite = new Composite (m_shell, SWT.BORDER);
		g = createGridData(SWT.FILL, true, 0, 0, 0, 0);
		g.verticalAlignment = SWT.FILL;
		g.grabExcessVerticalSpace = true;
		m_boardTableComposite.setLayoutData(g);
		
		m_cardTableViewer = new TableViewer(m_cardTableComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		m_boardTableViewer = new TableViewer(m_boardTableComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		
		setMainComponentsVisible(false);
		setCardDescComponentsVisible(false);
		setCardTableVisible(false);
		//----------------------------------------------------------
		
		m_shell.setSize(800, 170);
		m_shell.open();
		while (!m_shell.isDisposed())
		{
			if (!m_display.readAndDispatch()) m_display.sleep();
		}
		m_display.dispose();
		m_blueColor.dispose();
		m_whiteColor.dispose();
	    m_blackColor.dispose();
	}

	/**
	 * Метод для создания столбцов таблицы с карточками (cardTable).
	 * @param a_cardLists - список списков карточек одной доски
	 */
	private void createCardsColumns(List<CardList> a_cardLists)
	{
		for (int i = 0; i < a_cardLists.size(); i++)
		{
			createTableViewerColumn (m_cardTableViewer, a_cardLists.get(i).getName(), i);
		}
	}
	
	/**
	 * Метод для создания столбца таблицы с досками (boardTable).
	 */
	private void createBoardColumn()
	{
		TableViewerColumn viewerColumn = createTableViewerColumn (m_boardTableViewer, "Доски", 0);
		viewerColumn.setLabelProvider(new CellLabelProvider() 
		{
            @Override
            public void update(ViewerCell a_cell) 
            {
                a_cell.setText(((Board) a_cell.getElement()).getName());
            }
        });
	}

	/**
	 * Метод для создания и возврата столбца TableViewerColumn.
	 * @param a_viewer - объект класса TableViewer
	 * @param a_title - зоголовок столбца
	 * @param a_colNumber - номер столбца
	 * @return
	 */
	private TableViewerColumn createTableViewerColumn(TableViewer a_viewer, String a_title, int a_colNumber)
	{
		TableViewerColumn viewerColumn = new TableViewerColumn(a_viewer, SWT.CENTER);
        TableColumn tableColumn = viewerColumn.getColumn();
        tableColumn.setText(a_title);
        return viewerColumn;
	}

	/**
	 * Метод для установки свойств компонента m_shell.
	 */
	private void setShell()
	{
		m_shell.setMinimumSize(800, 170);
		m_shell.setText("Trello. Авторизация");
		m_shell.setBackground(m_blueColor);
		setIcon();
	}
	
	/**
	 * Метод для установки иконки приложения.
	 */
	private void setIcon()
	{
		try
		{
			InputStream in = Graphics.class.getResourceAsStream("/icons/icon.jpg");
			Image icon = new Image (m_display, in);
			m_shell.setImage(icon);
			in.close();
		}
		catch(IOException e)
		{
			createMessageBox(SWT.ERROR, e.getMessage());
		}
	}
	
	/**
	 * Метод для вызова дочернего по отношению к m_shell окна с сообщением пользователю.
	 * @param a_code - код сообщения
	 * @param a_message - сообщение
	 */
	private void createMessageBox(int a_code, String a_message)
	{
		MessageBox messageBox = new MessageBox (m_shell, a_code);
		messageBox.setText("Error");
		messageBox.setMessage(a_message);
		messageBox.open();
	}

	/**
	 * Метод для установки свойств компонента Label.
	 * @param a_label - объект класса Label
	 * @param a_text - текст для a_label
	 */
	private void setLabel (Label a_label, String a_text)
	{
		a_label.setText(a_text);
		a_label.setBackground(m_blueColor);
		a_label.setForeground(m_whiteColor);
		a_label.setFont(m_font);
	}

	/**
	 * Метод для создания объекта класса GridLayout и установки его на композит.
	 * @param a_composite - композит
	 * @param a_numColumns - количество столбцов
	 */
	private void createGridLayout(Composite a_composite, int a_numColumns) 
	{
		GridLayout g = new GridLayout();
		g.numColumns = a_numColumns; //Количество столбцов
		g.marginWidth = 5; //Расстояние от левого и правого краев окна
		g.marginHeight = 5; //Расстояние от верхнего и нижненго краев окна
		g.horizontalSpacing = 5; //Расстояние между соседними ячейками по горизонтали
		g.verticalSpacing = 10; //Расстояние между соседними ячейками по вертикали
		a_composite.setLayout(g); //Установка слоя на окно shell
	}
	
	/**
	 * Метод для создания объекта класса GridLayout и установки его на объект m_shell.
	 */
	private void createGridLayout() 
	{
		GridLayout g = new GridLayout();
		g.numColumns = 2; //Количество столбцов
		g.marginWidth = 5; //Расстояние от левого и правого краев окна
		g.marginHeight = 5; //Расстояние от верхнего и нижненго краев окна
		g.horizontalSpacing = 5; //Расстояние между соседними ячейками по горизонтали
		g.verticalSpacing = 10; //Расстояние между соседними ячейками по вертикали
		m_shell.setLayout(g); //Установка слоя на окно shell
	}
	
	/**
	 * Метод для создания объекта класса GridData (для управления данными, связанными с различными компонентами).
	 * Обязательным является указание значений a_horAl и a_grab, остальные параметры можно передать как 0 (будут установлены значения по умолчанию).
	 * @param a_horAl - выравнивание по горизонтали
	 * @param a_grab - разрешение/запрет захвата свободного пространства по горизонтали при изменении размеров окна
	 * @param a_width - ширина компонента
	 * @param a_height - высота компонента
	 * @param a_horInd - отступ по горизонтали от начального положения (что задаётся способом выравнивания) компонента в ячейке
	 * @param a_horSpan - количество ячеек, занимаемое компонентом по горизонтали
	 * @return a_gridData - объект класса GridData
	 */
	private GridData createGridData(int a_horAl, boolean a_grab, int a_width, int a_height, int a_horInd, int a_horSpan) 
	{
		GridData gridData = new GridData();
		gridData.horizontalAlignment = a_horAl; 
		gridData.grabExcessHorizontalSpace = a_grab; 
		if (a_width != 0) gridData.widthHint = a_width; 
		if (a_height != 0) gridData.heightHint = a_height; 
		if (a_horInd != 0) gridData.horizontalIndent = a_horInd; 
		if (a_horSpan != 0) gridData.horizontalSpan = a_horSpan; 
		return gridData;
	}
	
	/**
	 * Метод установки свойств для компонента Text.
	 * @param a_text - компонент Text
	 * @param a_editable - редактируемость
	 */
	private void setText (Text a_text, boolean a_editable)
	{
		a_text.setEditable(a_editable);
		a_text.setBackground(m_whiteColor);
		a_text.setForeground(m_blackColor);
		a_text.setFont(m_font);
	}
	
	/**
	 * Метод установки свойств для компонента Button.
	 * @param a_button - компонент Button
	 * @param a_text - название кнопки
	 */
	private void setButton (Button a_button, String a_text)
	{
		a_button.setText(a_text);
		a_button.setBackground(m_whiteColor);
		a_button.setForeground(m_blueColor);
		a_button.setFont(m_font);
	}

	
	/**
	 * Слушатель нажатия кнопки "Войти"
	 */
	private SelectionAdapter loginSelection = new SelectionAdapter ()
	{
		@Override
		public void widgetSelected (SelectionEvent a_s)
		{
			String key = m_keyText.getText();
			String token = m_tokenText.getText();
			if (key.equals(""))
			{
				createMessageBox(SWT.ICON_WARNING, "Введите ключ.");
				return;
			}
			if (token.equals(""))
			{
				createMessageBox(SWT.ICON_WARNING, "Введите токен.");
				return;
			}
			try
			{
				User user = m_connection.connectAndGetUserData(key, token);
				if (user == null) return;
				m_userFullNameText.setText(user.getFullName());
				m_userNameText.setText(user.getUsername());
				m_userEmailText.setText(user.getEmail());
				
				BoardList boardList = m_connection.getBoardList();
				if (boardList == null) return;
				int boardNumbers = boardList.getBoards().size();
				if (boardNumbers == 0)
				{
					createMessageBox(SWT.OK, "Нет досок.");
					return;
				}

				m_boardCardLists = new HashMap<>();
				List<CardList> tempList;
				
				for (Board b : boardList.getBoards())
				{
					tempList = m_connection.getCardLists(b.getId());
					if (tempList.size() != 0)
					{
						if (!m_boardCardLists.containsKey(b.getId()))
							m_boardCardLists.put(b.getId(), tempList);
				
					}
				}
				
				if (m_firstRun) createBoardTable();
			    
			    m_boardTableViewer.setInput(boardList);
			    m_boardTableViewer.refresh();
			    
				if (!m_firstRun) setMainComponentsVisible(true);
				m_shell.pack();
			}
			catch(IOException e)
			{
				createMessageBox (SWT.ERROR, e.getMessage());
			}
		}

	};
	
	/**
	 * Метод для создания таблицы с досками (boardTable).
	 */
	private void createBoardTable()
	{
		createBoardColumn();
		m_boardTable = m_boardTableViewer.getTable();
	    setTable(m_boardTable);
	    m_boardTableViewer.setContentProvider(new BoardsContentProvider());
	    m_boardTableViewer.addSelectionChangedListener(boardsTableRowSelection);
	    TableColumnLayout tableColumnLayout = new TableColumnLayout();
	    m_boardTableComposite.setLayout(tableColumnLayout);
	    	
	    ColumnWeightData columnWeightData = new ColumnWeightData(100);
	    columnWeightData.resizable = true;
	    columnWeightData.minimumWidth = 100;
	    tableColumnLayout.setColumnData(m_boardTable.getColumn(0), columnWeightData);
	    m_firstRun = false;
	}
	
	/**
	 * Слушатель нажатия на строку таблицы с досками (boardTable).
	 */
	private ISelectionChangedListener boardsTableRowSelection = new ISelectionChangedListener() 
	{
        @Override
        public void selectionChanged(SelectionChangedEvent a_event) 
        {
            IStructuredSelection selection = m_boardTableViewer.getStructuredSelection();
            Board board = (Board) selection.getFirstElement();
            if (board != null) 
            {
            	m_urlLabel.setText("URL доски:");
            	m_urlText.setText(board.getUrl());
            	m_boardLabel.setText("Доска \"" + board.getName() + "\". Списки карточек:");
            	m_boardLabel.setVisible(true);
            	System.out.println(board.getId()); ////////////////////////////////////////////////////////////////////////////
            	
            	m_cardLists = new ArrayList<>();
            	
        		for (Entry<String, List<CardList>> i : m_boardCardLists.entrySet())
        		{
        			if (i.getKey().equals(board.getId()))
        			{
        				for (CardList j : i.getValue())
        				{
        					m_cardLists.add(j);
        				}
        			}
        		}
        		
        		TableColumnLayout tableColumnLayout = new TableColumnLayout();
        		ColumnWeightData columnWeightData = new ColumnWeightData(100);
			    columnWeightData.resizable = true;
			    columnWeightData.minimumWidth = 100;
            	if (m_firstBoardChoice)
				{
            		createCardsColumns(m_cardLists);
					m_cardTable = m_cardTableViewer.getTable();
				    setTable(m_cardTable);
				    m_cardTableComposite.setLayout(tableColumnLayout);
				    	
				    
				    for (int i = 0; i < m_cardLists.size(); i++)
				    {
				    	tableColumnLayout.setColumnData(m_cardTable.getColumn(i), columnWeightData);
				    }
				    m_firstBoardChoice = false;
				    setCardTableVisible(true);
				    m_cardTableColumnCount = m_cardTable.getColumnCount();  
				    m_cardTable.addListener(SWT.MouseDown, e ->
			    	{
			    		Point point = new Point (e.x, e.y);
						TableItem item = m_cardTable.getItem(point);
						if (item != null)
						{
							for (int c = 0; c < m_cardTable.getColumnCount(); c++)
							{
								Rectangle rect = item.getBounds(c);
								if (rect.contains(point))
								{		
									if (m_cardTable.getSelectionIndex() < m_cardLists.get(c).getCards().size())
									{
										Card card = m_cardLists.get(c).getCards().get(m_cardTable.getSelectionIndex());
										String cardDesc = card.getDesc();
										if (cardDesc != null)
											m_cardDescText.setText(cardDesc);
										if (cardDesc.equals("")) 
											m_cardDescText.setText("<Описание отсутствует>");
										m_urlLabel.setText("URL карточки:");
										m_urlText.setText(card.getUrl());
										//System.out.println(card.getId()); ////////////////////////////////////////////////////////////////////////////
										setCardDescComponentsVisible(true);
									}
								}
							 }
						 }
					 });  
				} else
				{
					if (m_cardLists.size() > m_cardTableColumnCount)
					{
						int t = m_cardLists.size() - m_cardTableColumnCount;
						for (int i = 0; i < t; i++)
						{
							createTableViewerColumn (m_cardTableViewer, m_cardLists.get(i).getName(), m_cardTableColumnCount + i);
							tableColumnLayout.setColumnData(m_cardTable.getColumn(m_cardTableColumnCount + i), columnWeightData);
						}
						
					}
					if (m_cardLists.size() < m_cardTableColumnCount)
					{
						int t = m_cardTableColumnCount - m_cardLists.size();
						for (int i = 0; i < t; i++)
						{
							m_cardTable.getColumn(i).dispose();
						}
					}
					for (int i = 0; i < m_cardLists.size(); i++)
					{
						m_cardTable.getColumn(i).setText(m_cardLists.get(i).getName());
						
					}
					m_cardTableColumnCount = m_cardTable.getColumnCount();
				}
            	       
            	m_cardTableViewer.getTable().removeAll();
            	int maxCount = m_cardLists.get(0).getCards().size();
            	
            	for (CardList l : m_cardLists)
            	{
            		if (l.getCards().size() > maxCount) maxCount = l.getCards().size();
            	}
            	
            	m_cardTableViewer.getTable().setItemCount(maxCount);
     
            	for (int l = 0; l < m_cardLists.size(); l++)
            	{
            		for (int c = 0; c < m_cardLists.get(l).getCards().size(); c++)
            		{
            			m_cardTableViewer.getTable().getItem(c).setText(l, m_cardLists.get(l).getCards().get(c).getName());
            		}
            	}
            	
				m_shell.pack();
				for (int i = 0; i < m_cardTable.getColumnCount(); i++)
				{
					m_cardTable.getColumn(i).pack();
				}
            }
        }
    };
  
	/**
	 * Метод для установки свойств таблицы.
	 * @param a_table - объект класса Table
	 */
	private void setTable(Table a_table)
	{
		GridData gridData = createGridData(SWT.FILL, true, 0, 0, 0, 0);
		a_table.setLayoutData(gridData);
		a_table.setBackground(m_whiteColor);
		a_table.setForeground(m_blueColor);
		a_table.setFont(m_font);
		a_table.setHeaderVisible(true);
		a_table.setLinesVisible(true);
	}
	
	/**
	 * Метод для установки свойства видимости графических компонентов, которые должны отображаться после нажатия на кнопку "Войти".
	 * @param a_visible - видимость
	 */
	private void setMainComponentsVisible(boolean a_visible)
	{
		m_userComposite.setVisible(a_visible);
		m_userFullNameLabel.setVisible(a_visible);
		m_userFullNameText.setVisible(a_visible);
		m_userNameLabel.setVisible(a_visible);
		m_userNameText.setVisible(a_visible);
		m_userEmailLabel.setVisible(a_visible);
		m_userEmailText.setVisible(a_visible);
		m_urlLabel.setVisible(a_visible);
		m_urlText.setVisible(a_visible);
        m_boardTableComposite.setVisible(a_visible);
	}
	
	/**
	 * Метод для установки свойства видимости графических компонентов, которые должны отображаться после выбора пользователем карточки в таблице cardTable.
	 * @param a_visible - видимость
	 */
	private void setCardDescComponentsVisible(boolean a_visible)
	{
		m_cardDescLabel.setVisible(a_visible);
		m_cardDescText.setVisible(a_visible);
	}
	
	/**
	 * Метод для установки свойства видимости таблицы с карточками (cardTable).
	 * @param a_visible - видимость
	 */
	private void setCardTableVisible(boolean a_visible)
	{
		if (m_cardTable != null)
			m_cardTable.setVisible(a_visible);
		m_cardTableComposite.setVisible(a_visible);
	}
	
	/**
	 * Обработчик результата попытки установки соединения с trello.com (при отсутствии подключения). Выводит сообщение об ошибке.
	 */
	private NoConnectionResultHandler noConnectHandler = new NoConnectionResultHandler()
	{
		public void createMessage(String a_message)
		{
			createMessageBox(SWT.ERROR, a_message);
		}
	};
}
