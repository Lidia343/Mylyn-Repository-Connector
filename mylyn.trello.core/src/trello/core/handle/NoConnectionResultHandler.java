package trello.core.handle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Абстрактный класс обработчика результата попытки установки соединения с trello.com
 * (при отсутствии подключения).
 */
public abstract class NoConnectionResultHandler
{
	private static Display display;
	private static Shell shell;
	
	/**
	 * Метод для создания окна с сообщением пользователю.
	 * @param a_message - сообщение
	 */
	public static void createMessage(String a_message)
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
