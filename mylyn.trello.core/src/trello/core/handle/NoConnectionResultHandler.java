package trello.core.handle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Класс обработчика результата попытки установки соединения с trello.com
 * (при отсутствии подключения).
 */
public class NoConnectionResultHandler implements INoConnectionResultHandler
{
	private Display display;
	private Shell shell;
	
	@Override
	public void createMessage(String a_message)
	{
		display = new Display();
		shell = new Shell(display);
		MessageBox messageBox = new MessageBox(shell, SWT.ERROR);
		messageBox.setText("Error");
		messageBox.setMessage(a_message);
		messageBox.open();
		display.dispose();
	}
}
