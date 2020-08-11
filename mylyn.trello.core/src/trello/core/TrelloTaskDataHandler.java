package trello.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.mylyn.commons.net.Policy;
import org.eclipse.mylyn.tasks.core.ITaskAttachment;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.RepositoryResponse.ResponseKind;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;

import trello.core.client.ITrelloClient;
import trello.core.client.TrelloClient;
import trello.core.model.Action;
import trello.core.model.Card;
import trello.core.util.TrelloUtil;

public class TrelloTaskDataHandler extends AbstractTaskDataHandler
{
	private static final String TASK_DATA_VERSION = "2"; 
	
	public TaskData getTaskData(TaskRepository a_repository, String a_taskId, IProgressMonitor a_monitor)
	{
		a_monitor = Policy.monitorFor(a_monitor);
		try
		{
			a_monitor.beginTask("Task Download", IProgressMonitor.UNKNOWN);
			return downloadTaskData(a_repository, a_taskId, a_monitor);
		}
		finally
		{
			a_monitor.done();
		}
	}
	
	private TaskData downloadTaskData(TaskRepository a_repository, String a_taskId, IProgressMonitor a_monitor)
	{
		ITrelloClient client = new TrelloClient();
		Card card = null;
		try
		{
			card = client.getCardById(a_taskId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return createTaskDataFromCard(a_repository, client, card);
	}

	private TaskData createTaskDataFromCard(TaskRepository a_repository, ITrelloClient a_client, @NonNull Card a_card)
	{
		TaskData taskData = new TaskData(new TaskAttributeMapper(a_repository), TrelloRepositoryConnector.CONNECTOR_KIND, a_repository.getRepositoryUrl(), a_card.getId()); 
		taskData.setVersion(TASK_DATA_VERSION);
		taskData.setPartial(true);
		
		TaskAttribute root = taskData.getRoot();
		
		root.createAttribute(TrelloAttribute.ID_LIST).setValue(a_card.getIdList());
		root.createAttribute(TrelloAttribute.CLOSED).setValue(a_card.getClosed());
		root.createAttribute(TrelloAttribute.DUE_COMPLETE).setValue(a_card.getDueComplete());
		root.createAttribute(TaskAttribute.SUMMARY).setValue(a_card.getName());
		root.createAttribute(TaskAttribute.TASK_URL).setValue(a_card.getUrl());
		
		String trelloDue = a_card.getDue();
		if  (trelloDue != null) createDateAttribute(taskData, TaskAttribute.DATE_DUE, trelloDue);
		
		String lastActivity = a_card.getDateLastActivity();
		if (lastActivity != null)
		{
			createDateAttribute(taskData, TaskAttribute.DATE_MODIFICATION, lastActivity);
		}
		
		try
		{
			for (Action action : a_client.getActions(a_card.getIdList()))
			{
				Card actionCard = action.getData().getCard();
				if (actionCard.getId().equals(a_card.getId()))
				{
					if (action.getType().equals(Action.CREATE_CARD))
					{
						createDateAttribute(taskData, TaskAttribute.DATE_CREATION, action.getDate());
					}
					else
					{
						if (action.getData().getOld().getDueComplete() != null)
						{
							if (a_card.getDueComplete().equals("true"))
							{
								createDateAttribute(taskData, TaskAttribute.DATE_COMPLETION, action.getDate());
								break;
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			Date now = new Date();
			createDateAttribute(taskData, TaskAttribute.DATE_CREATION, now);
			if (a_card.getDueComplete().equals("true"))
			{
				createDateAttribute(taskData, TaskAttribute.DATE_COMPLETION, now);
			}
		}
		return taskData;
	}
	
	private void createDateAttribute (TaskData a_taskData, String a_attrType, String a_date)
	{
		TaskAttribute attr = a_taskData.getRoot().createAttribute(a_attrType);
		a_taskData.getAttributeMapper().setDateValue(attr, TrelloUtil.toMylynDate(a_date));
	}
	
	private void createDateAttribute (TaskData a_taskData, String a_attrType, Date a_date)
	{
		TaskAttribute attr = a_taskData.getRoot().createAttribute(a_attrType);
		a_taskData.getAttributeMapper().setDateValue(attr, a_date);
	}
	
	@Override
	public RepositoryResponse postTaskData(@NonNull TaskRepository a_repository, @NonNull TaskData a_taskData, @Nullable Set<TaskAttribute> a_oldAttributes,
										   @Nullable IProgressMonitor a_monitor) throws CoreException
	{
		RepositoryResponse response = null;
		
		ITrelloClient client = new TrelloClient();
		Card card;
		try
		{
			String taskId = a_taskData.getTaskId();
			if (a_taskData.isNew())
			{
				Map<String, String> attributes = new HashMap<>();
				for (Entry<String, TaskAttribute> attrEntry : a_taskData.getRoot().getAttributes().entrySet())
				{
					String trelloAttributeId = TrelloAttribute.valueOf(attrEntry.getKey());
					if (trelloAttributeId == null) break;
					
					String attrValue = attrEntry.getValue().getValue();
					
					attributes.put(trelloAttributeId, attrValue);
				}
				card = client.createCard(a_taskData.getRoot().getAttribute(TrelloAttribute.ID_LIST).getValue(),
										 attributes);
				response = new RepositoryResponse(ResponseKind.TASK_CREATED, card.getId());
			}
			else
			{
				card = client.getCardById(taskId);
				response = new RepositoryResponse(ResponseKind.TASK_UPDATED, taskId);
				
				for (Entry<String, TaskAttribute> attrEntry : a_taskData.getRoot().getAttributes().entrySet())
				{
					String trelloAttributeId = TrelloAttribute.valueOf(attrEntry.getKey());
					if (trelloAttributeId == null) break;
					
					String value = attrEntry.getValue().getValue();
					
					if (!value.equals(card.getValue(trelloAttributeId)))
					{
						client.changeCard(taskId, trelloAttributeId, value);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public boolean initializeTaskData(@NonNull TaskRepository a_repository, @NonNull TaskData a_data, @Nullable ITaskMapping a_initializationData,
									  @Nullable IProgressMonitor a_monitor) throws CoreException
	{
		a_data.setVersion(TASK_DATA_VERSION);
		a_data.setPartial(true);
		
		
		return true;
	}

	@Override
	public TaskAttributeMapper getAttributeMapper(@NonNull TaskRepository a_repository)
	{
		return new TaskAttributeMapper(a_repository);
	}
}
