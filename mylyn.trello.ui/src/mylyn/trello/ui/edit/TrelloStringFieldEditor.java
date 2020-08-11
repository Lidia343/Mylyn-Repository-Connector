package mylyn.trello.ui.edit;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class TrelloStringFieldEditor
{
	private Composite m_parent;
	private Label m_label;
	private Text m_text;
	
	private String m_labelText;
	
	private boolean m_fillVertical;
	
	public TrelloStringFieldEditor (@NonNull Composite a_parent, @NonNull String a_labelText, boolean a_fillVertical)
	{
		init (a_parent, a_labelText);
		m_fillVertical = a_fillVertical;
		createControls();
	}
	
	private void init (Composite a_parent, @NonNull String a_labelText)
	{
		Assert.isNotNull(a_labelText);
		
		m_parent = a_parent;
		m_labelText = a_labelText + " ";
	}
	
	private void createControls ()
	{
		GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		
		m_parent.setLayout(new GridLayout(2, false));
		m_parent.setLayoutData(data);
		
		m_label = new Label(m_parent, SWT.NONE);
		m_label.setText(m_labelText + " ");
		
		if (!m_fillVertical)
		{
			data.verticalAlignment = SWT.CENTER;
			data.grabExcessVerticalSpace = false;
			m_label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
			m_text = new Text(m_parent, SWT.BORDER);
		}
		else
		{
			m_label.setLayoutData(new GridData(SWT.RIGHT, SWT.UP, false, false));
			m_text = new Text(m_parent, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		}
		m_text.setLayoutData(data);
	}
	
	public void setStringValueChangeListener (int a_eventType, @NonNull Listener a_listener)
	{
		Assert.isNotNull(a_listener);
		if (!m_text.isListening(a_eventType))
		{
			m_text.addListener(a_eventType, a_listener);
		}
	}
	
	public String getLabelText ()
	{
		return m_labelText;
	}
	
	public void setEditable (boolean a_editable)
	{
		m_text.setEditable( a_editable);
	}
	
	public void setStringValue (@NonNull String a_value)
	{
		Assert.isNotNull(a_value);
		m_text.setText(a_value);
	}
	
	@Nullable
	public String getStringValue ()
	{
		return (m_text == null) ? null : m_text.getText();
	}
}
