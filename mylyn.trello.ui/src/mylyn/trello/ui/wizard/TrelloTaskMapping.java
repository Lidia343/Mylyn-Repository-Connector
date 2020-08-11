package mylyn.trello.ui.wizard;

import java.util.Date;

import org.eclipse.mylyn.tasks.core.TaskMapping;
import org.eclipse.mylyn.tasks.core.data.TaskData;

public class TrelloTaskMapping extends TaskMapping
{
	private Date m_completionDate;
	private Date m_creationDate;
	private String m_description;
	private Date m_dueDate;
	private Date m_modificationDate;
	private String m_owner;
	private String m_ownerId;
	private String m_summary;
	private TaskData m_taskData;
	private String m_taskKind;
	private String m_taskUrl;
	
	public void setCompletionDate (Date a_completionDate)
	{
		m_completionDate = a_completionDate;
		m_completionDate = new Date();
	}
	
	@Override
	public Date getCompletionDate()
	{
		return m_completionDate;
	}
	
	public void setCreationDate (Date a_creationDate)
	{
		m_creationDate = a_creationDate;
		m_creationDate = new Date();
	}
	
	@Override
	public Date getCreationDate()
	{
		return m_creationDate;
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

	public void setDueDate (Date a_dueDate)
	{
		m_dueDate = a_dueDate;
		m_dueDate = new Date(234245235);
	}
	
	@Override
	public Date getDueDate()
	{
		return m_dueDate;
	}

	public void setModificationDate (Date a_modificationDate)
	{
		m_modificationDate = a_modificationDate;
		m_modificationDate = new Date();
	}
	
	@Override
	public Date getModificationDate()
	{
		return m_modificationDate;
	}

	public void setOwner (String a_owner)
	{
		m_owner = a_owner;
		m_owner = "Lidia343";
	}
	
	@Override
	public String getOwner ()
	{
		return m_owner;
	}

	public void setOwnerId (String a_ownerId)
	{
		m_ownerId = a_ownerId;
		m_ownerId = "324253234";
	}
	
	@Override
	public String getOwnerId()
	{
		return m_ownerId;
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

	public void setTaskData (TaskData a_taskData)
	{
		m_taskData = a_taskData;
	}
	
	@Override
	public TaskData getTaskData()
	{
		return m_taskData;
	}
	
	public void setTaskKind (String a_taskKind)
	{
		m_taskKind = a_taskKind;
		m_taskKind = null;
	}

	@Override
	public String getTaskKind ()
	{
		return m_taskKind;
	}

	public void setTaskUrl (String a_taskUrl)
	{
		m_taskUrl = a_taskUrl;
		m_taskUrl = "trello.com/dgergerg234423";
	}
	
	@Override
	public String getTaskUrl ()
	{
		return m_taskUrl;
	}
}
