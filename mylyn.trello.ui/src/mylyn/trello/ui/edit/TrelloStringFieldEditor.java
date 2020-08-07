package mylyn.trello.ui.edit;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import mylyn.trello.ui.validation.StringValueValidator;

public class TrelloStringFieldEditor
{
	private Composite m_parent;
	private Label m_label;
	private Text m_text;
	
	private String m_labelText;
	
	private ModifyListener m_modifyListener;
	
	private String m_errorMessage;
	
	public TrelloStringFieldEditor (@NonNull Composite a_parent, @NonNull String a_labelText)
	{
		init (a_parent, a_labelText);
		createControl();
	}
	
	private void init (Composite a_parent, String a_labelText)
	{
		//Assert.isNotNull(m_parent);
		//Assert.isNotNull(m_labelText);
		
		m_parent = a_parent;
		m_labelText = a_labelText + " ";
	}
	
	private void createControl ()
	{
		m_parent.setLayout(new GridLayout(2, false));
		
		GridData g = new GridData(GridData.FILL_HORIZONTAL);
		m_parent.setLayoutData(g);
		
		m_label = new Label(m_parent, SWT.NONE);
		m_label.setText(m_labelText);
		m_label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		
		m_text = new Text(m_parent, SWT.BORDER);
		m_text.setLayoutData(g);
	}
	
	public void setStringValueValidator (@NonNull StringValueValidator a_validator)
	{
		//Assert.isNotNull(a_validator);
		
		if (m_modifyListener == null)
		{
			m_modifyListener = new ModifyListener ()
			{
				@Override
				public void modifyText(ModifyEvent a_e)
				{
					m_errorMessage = a_validator.validate((String)a_e.data);
				}
			};
		}
		
		if (!m_text.isListening(SWT.Modify))
		{
			m_text.addModifyListener(m_modifyListener);
		}
	}
	
	public String getLabelText ()
	{
		return m_labelText;
	}
	
	@Nullable
	public String getStringValue ()
	{
		return (m_text == null) ? null : m_text.getText();
	}
	
	@Nullable
	public String getErrorMessage ()
	{
		return m_errorMessage;
	}
}
