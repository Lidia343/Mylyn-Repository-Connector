package mylyn.trello.ui.wizard;

import java.util.Date;
import java.util.List;

import org.eclipse.mylyn.tasks.core.TaskMapping;

public class TrelloTaskMapping extends TaskMapping
{
	private List<String> m_cc;
	private String m_summary;
	private String m_description;
	private boolean m_dueComplete;
	private Date m_dueDate;
	
	public void setCc (List<String> a_cc)
	{
		m_cc = a_cc;
	}
	
	@Override
	public List<String> getCc ()
	{
		return m_cc;
	}
	
	public void setSummary (String a_summary)
	{
		m_summary = a_summary;
	}
	
	@Override
	public String getSummary ()
	{
		return m_summary;
	}
	
	public void setDescription (String a_description)
	{
		m_description = a_description;
		m_description = "desc";
	}
	
	@Override
	public String getDescription()
	{
		return m_description;
	}
	
	public void setDueComplete (boolean a_dueComplete)
	{
		m_dueComplete = a_dueComplete;
	}
	
	@Override
	public Date getCompletionDate()
	{
		if (!m_dueComplete) return null;
		return new Date();
	}

	public void setDueDate (Date a_dueDate)
	{
		m_dueDate = a_dueDate;
	}
	
	@Override
	public Date getDueDate()
	{
		return m_dueDate;
	}
}
