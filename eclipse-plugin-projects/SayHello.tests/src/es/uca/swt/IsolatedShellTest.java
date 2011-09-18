package es.uca.swt;

/*
 Copyright (c) 2011, Antonio García-Domínguez
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Neither the name of the <organization> nor the
 names of its contributors may be used to endorse or promote products
 derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.junit.After;
import org.junit.Before;

/**
 * <p>
 * Base class for a JUnit test suite using a SWTBot from a single shell, which
 * needs to be recreated before every test. Users of this class should only
 * define the contents of the {@link #createShell()} method so it produces the
 * shell to be tested. To access the {@link SWTBot} for the current test, the
 * protected <code>bot</code> field can be used.
 * </p>
 * 
 * <p>
 * This class starts up the UI thread and has it loop indefinitely. Every test
 * creates the dialog, makes it available through the <code>bot</code> protected
 * field and then operates the SWT event loop until the shell is closed by this
 * class.
 * </p>
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
						// Open and lay out the shell
						final Display display = Display.getDefault();
						appShell = createShell();
						bot = new SWTBot(appShell);

						// The bot is ready: wait in the barrier for the test
						// setup
						swtBarrier.await();

						// Run the SWT event loop until this shell is disposed
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

		/*
		 * Use a daemon thread for the UI, so it will be stopped automatically
		 * once the tests finish
		 */
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
