package es.uca.swt;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.junit.After;
import org.junit.Before;

/**
 * <p>Base class for a JUnit test suite using a SWTBot from a single shell, which needs
 * to be recreated before every test. Users of this class should only define the
 * contents of the {@link #createShell()} method so it produces the shell to be
 * tested. To access the {@link SWTBot} for the current test, the protected <code>bot</code>
 * field can be used.</p>
 * 
 * <p>This class starts up the UI thread and has it loop indefinitely. Every test creates
 * the dialog, makes it available through the <code>bot</code> protected field and then operates
 * the SWT event loop until the shell is closed by this class.</p> 
 */
public abstract class IsolatedShellTest {

	protected static SWTBot bot;

	private final static CyclicBarrier swtBarrier = new CyclicBarrier(2);
	private static Thread uiThread;
	private static Shell appShell;

	public IsolatedShellTest() {
		if (uiThread == null) {
			initializeUIThread();
			uiThread.start();
		}
	}

	private void initializeUIThread() {
		uiThread = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						final Display display = Display.getDefault();
						appShell = createShell();
						bot = new SWTBot(appShell);
						swtBarrier.await();
						while (!appShell.isDisposed()) {
							if (!display.readAndDispatch()) {
								display.sleep();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		uiThread.setDaemon(true);
	}

	@Before
	public void setup() throws InterruptedException, BrokenBarrierException {
		swtBarrier.await();
	}

	@After
	public void teardown() throws InterruptedException {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				appShell.close();
			}
		});
	}

	/**
	 * This method must be overridden by users. It should return the
	 * {@link Shell} to be tested, after being opened and laid out. This class
	 * will take care of running its event loop afterwards, until the test ends:
	 * at this point, this class will close the {@link Shell} automatically.
	 */
	protected abstract Shell createShell();
}
