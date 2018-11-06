//Created by Mateusz Szymanski
//email - Matreoxioo@gmail.com

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

public class Encrypt {

	protected Shell shell;
	int info = SWT.ICON_INFORMATION;

	/**
	 * Launch the application.
	 * @wbp.parser.entryPoint
	 */
	public static void encryptStart() {
		try {
			Encrypt window = new Encrypt();
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
		shell.setText("Encrypt");
		// Coordinates of where the shell will be displayed
		int x = 1300 / 2;
		int y = 800 / 2;
		// set the new location
		shell.setLocation(x, y);

		/**
		 * MainMenu button: Used to allow sure to return to MainMenu screen
		 */
		Button btnMainMenu = new Button(shell, SWT.NONE);
		btnMainMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Closing Encryption window
				shell.close();
				// Opening main menu window
				MainMenu.main(null);
			}
		});
		btnMainMenu.setBounds(10, 218, 93, 33);
		btnMainMenu.setText("MainMenu");

		/**
		 * TextFile button: Takes the user to EncryptText screen
		 */
		Button btnTextFile = new Button(shell, SWT.NONE);
		btnTextFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Closing Encryption window
				shell.close();
				// Opening EncryptTxt window
				EncryptTxt.encryptTxtStart();
			}
		});
		btnTextFile.setBounds(173, 79, 93, 33);
		btnTextFile.setText("Text File");

		/**
		 * Img button: Takes the user to EncryptImg
		 */
		Button btnImg = new Button(shell, SWT.NONE);
		btnImg.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Closing Encryption window
				shell.close();
				// Opening main menu window
				EncryptImg.encryptImgStart();
			}
		});
		btnImg.setBounds(173, 128, 93, 33);
		btnImg.setText("Image");

		/**
		 * EncryptFolder button: Takes the user to EncryptFile screen
		 */
		Button btnEncryptFolder = new Button(shell, SWT.NONE);
		btnEncryptFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Closing Encryption window
				shell.close();
				// Opening folder encrypt window
				EncryptFolder.encryptFolderStart();
			}
		});
		btnEncryptFolder.setBounds(173, 173, 93, 33);
		btnEncryptFolder.setText("Folder");

		/**
		 * Help button: Displays a window with additional information
		 */
		Button btnHelp = new Button(shell, SWT.NONE);
		btnHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(shell, info);
				messageBox.setMessage(
						"\u2022To encrypt .txt files select Text File\n\u2022To encrypt .jpg select Image\n\u2022To encrypt folders select Folder\n\u2022To return to MainMenu select MainMenu ");
				messageBox.open();
			}
		});
		btnHelp.setBounds(403, 23, 21, 25);
		btnHelp.setText("i");

		/**
		 * Label with main on-screen text
		 */
		Label lblNewLabel = new Label(shell, SWT.BORDER);
		lblNewLabel.setBounds(40, 10, 360, 48);
		lblNewLabel.setText("		Welcome to Encryption Menu\r\n                       Please select one of the available options:");

	}
}
