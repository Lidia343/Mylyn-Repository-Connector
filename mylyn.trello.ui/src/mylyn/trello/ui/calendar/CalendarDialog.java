package mylyn.trello.ui.calendar;

import trello.core.util.TrelloUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TouchEvent;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;

import mylyn.trello.ui.handle.FinishHandler;

import org.eclipse.swt.widgets.Button;

public class CalendarDialog
{
	private FinishHandler m_handler;
	
	private Shell m_parent;
	private Shell m_calendarShell;
	private DateTime m_calendarDate;
	private DateTime m_calendarTime;
	
	private String m_trelloDate = "";
	private String m_trelloTime = "";
	private String m_trelloDateAndTime = "";
	
	private String m_dateForShow = "";
	private String m_timeForShow = "";
	private String m_dateAndTimeForShow = "";
	
	public CalendarDialog (Shell a_parent, FinishHandler a_handler)
	{
		m_parent = a_parent;
		m_handler = a_handler;
		initializeComponentAndOpen();
	}
	
	private void initializeComponentAndOpen ()
	{
		m_calendarShell = new Shell (m_parent);
		GridLayout shellLayout = new GridLayout(2, false);
		m_calendarShell.setLayout(shellLayout);
		
		Button timeSelectButton = new Button (m_calendarShell, SWT.CHECK);
		GridData g = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		timeSelectButton.setText("Time: ");
		timeSelectButton.setLayoutData(g);
		timeSelectButton.setSelection(true);
		
		m_calendarTime = new DateTime (m_calendarShell, SWT.BORDER | SWT.TIME);
		g = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		m_calendarTime.setLayoutData(g);
		
		Composite composite = new Composite (m_calendarShell, SWT.BORDER);
		GridLayout compLayout = new GridLayout(1, false);
		composite.setLayout(compLayout);
		g = new GridData(SWT.FILL, SWT.FILL, true, true);
		g.horizontalSpan = 2;
		composite.setLayoutData(g);
		
		m_calendarDate = new DateTime (composite, SWT.BORDER | SWT.CALENDAR);
		g = new GridData(SWT.FILL, SWT.FILL, true, true);
		m_calendarDate.setLayoutData(g);
		
		Button finishButton = new Button (composite, SWT.PUSH);
		g = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		finishButton.setLayoutData(g);
		finishButton.setText("Finish");
		m_calendarShell.setDefaultButton(finishButton);
		
		setDate();
		setTime();
		
		timeSelectButton.addSelectionListener(new SelectionListener() 
		{
			@Override
			public void widgetSelected (SelectionEvent a_e)
			{
				if (timeSelectButton.getSelection() == false)
				{
					m_trelloTime = "";
					m_timeForShow = "";
					m_calendarTime.setEnabled(false);
				}
				else
				{
					m_calendarTime.setEnabled(true);
				}
				setDateAndTime();
			}

			@Override
			public void widgetDefaultSelected (SelectionEvent a_e)
			{
			}
			
		});
		
		m_calendarDate.addSelectionListener(new SelectionListener() 
		{
			@Override
			public void widgetSelected (SelectionEvent a_e)
			{
				setDate();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent a_e)
			{
			}
		});
		
		m_calendarTime.addSelectionListener(new SelectionListener() 
		{
			@Override
			public void widgetSelected (SelectionEvent a_e)
			{
				setTime();
			}
			
			@Override
			public void widgetDefaultSelected (SelectionEvent a_e)
			{
				
			}
		});
		
		m_calendarTime.addTouchListener(new TouchListener() 
		{
			@Override
			public void touch (TouchEvent a_e)
			{
				setTime();
			}
		});
		
		finishButton.addSelectionListener(new SelectionListener() 
		{
			@Override
			public void widgetSelected (SelectionEvent a_e)
			{
				m_handler.finish(m_trelloDateAndTime, m_dateAndTimeForShow);
				m_calendarShell.dispose();
			}

			@Override
			public void widgetDefaultSelected (SelectionEvent a_e)
			{
				
			}
		});

		composite.pack();
		m_calendarShell.pack();
		m_calendarShell.open();
	}
	
	private void setDate ()
	{
		String day = TrelloUtil.toStandartTimePart(m_calendarDate.getDay());
		String month = TrelloUtil.toStandartTimePart(m_calendarDate.getMonth() + 1);
		String year = TrelloUtil.toStandartTimePart(m_calendarDate.getYear());
		
		m_trelloDate = year + "-" + month + "-" + day + "T";
		m_dateForShow = day + "." + month + "." + year;
		
		setDateAndTime();
	}
	
	private void setTime ()
	{
		String trelloHours = TrelloUtil.toTrelloHours(m_calendarTime.getHours());
		String mylynHours = TrelloUtil.toStandartTimePart(m_calendarTime.getHours());
		
		String minutes = TrelloUtil.toStandartTimePart(m_calendarTime.getMinutes());
		
		String seconds = TrelloUtil.toStandartTimePart(m_calendarTime.getSeconds());
		
		m_trelloTime = trelloHours + ":" + minutes + ":" + seconds;
		m_timeForShow = " - " + mylynHours + ":" + minutes + ":" + seconds;
		
		setDateAndTime();
	}
	
	private void setDateAndTime ()
	{
		m_trelloDateAndTime = m_trelloDate + m_trelloTime;
		m_dateAndTimeForShow = m_dateForShow + m_timeForShow;
	}
	
	public boolean isDisposed ()
	{
		return m_calendarShell.isDisposed();
	}
}
