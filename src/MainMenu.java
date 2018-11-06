//Created by Mateusz Szymanski
//email - Matreoxioo@gmail.com

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainMenu {

	protected Shell shell;
	int info = SWT.ICON_INFORMATION;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MainMenu window = new MainMenu();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("MainMenu");
		// Coordinates of where the shell will be displayed
		int x = 1300 / 2;
		int y = 800 / 2;
		// set the new location
		shell.setLocation(x, y);

		/**
		 * Encrypt button: Takes user to Encrypt screen
		 */
		Button btnEncrypt = new Button(shell, SWT.NONE);
		btnEncrypt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Closing Main Menu window
				shell.close();
				// Opening Encryption window
				Encrypt.encryptStart();
			}
		});
		btnEncrypt.setBounds(172, 96, 89, 35);
		btnEncrypt.setText("Encrypt");

		/**
		 * Decrypt button: Takes user to Decrypt screen
		 */
		Button btnDecrypt = new Button(shell, SWT.NONE);
		btnDecrypt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Closing Main Menu window
				shell.close();
				// Opening decrypt menu
				Decrypt.decryptStart();
			}
		});
		btnDecrypt.setBounds(172, 149, 89, 35);
		btnDecrypt.setText("Decrypt");

		Label lblNewLabel = new Label(shell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		lblNewLabel.setTouchEnabled(false);
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblNewLabel.setText(
				"\t\t Welcome to SuperCrypt\r\n\r\n   Use this program to encrypt or decrypt text files, images and folders");
		lblNewLabel.setBounds(25, 25, 376, 65);

		/**
		 * Exit button: Exits the program
		 */
		Button btnExit = new Button(shell, SWT.NONE);
		btnExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Creating a pop-up to confirm exit
				Display display = Display.getDefault();
				Shell shell = new Shell(display);

				// Declaring the style and elements of the pop-up
				int style = SWT.ICON_WARNING | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox(shell, style);
				messageBox.setMessage("Are you sure you want to exit this application ? ");
				int rc = messageBox.open();

				// Switch case with available options
				switch (rc) {
				case SWT.NO:
					break;
				case SWT.YES:
					display.dispose();
					break;
				}
			}
		});
		btnExit.setBounds(172, 202, 89, 35);
		btnExit.setText("Exit");

		/**
		 * Help button: Provides extra instructions
		 */
		Button btnHelp = new Button(shell, SWT.NONE);
		btnHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Display an information message with instructions
				MessageBox messageBox = new MessageBox(shell, info);
				messageBox.setMessage("\u2022To encrypt files press Encrypt\n\u2022To decrypt files press Decrypt ");
				messageBox.open();
			}
		});
		btnHelp.setBounds(403, 23, 21, 25);
		btnHelp.setText("i");
	}
}
