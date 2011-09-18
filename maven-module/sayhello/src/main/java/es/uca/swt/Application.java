package es.uca.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Application {

	protected Shell shlExample;
	private Label lblInfo;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Application window = new Application();
			window.open();
			window.eventLoop(Display.getDefault());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public Shell open() {
		// Ensure a Display exists
		Display.getDefault();

		createContents();
		shlExample.open();
		shlExample.layout();
		return shlExample;
	}

	public void eventLoop(Display display) {
		while (!shlExample.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	Shell getShell() {
		return shlExample; 
	}

	/**
	 * Create contents of the window.
	 */
	public void createContents() {
		shlExample = new Shell();
		shlExample.setMinimumSize(new Point(175, 100));
		shlExample.setSize(100, 100);
		shlExample.setText("Example");
		RowLayout rl_shlExample = new RowLayout(SWT.VERTICAL);
		rl_shlExample.center = true;
		rl_shlExample.fill = true;
		shlExample.setLayout(rl_shlExample);
		
		Button btnNewButton = new Button(shlExample, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblInfo.setText("I am changed!");
			}
		});
		btnNewButton.setText("Press me!");
		
		lblInfo = new Label(shlExample, SWT.CENTER);
		lblInfo.setLayoutData(new RowData(113, SWT.DEFAULT));
		lblInfo.setText("I am empty.");
	}

}
