//Created by Mateusz Szymanski
//email - Matreoxioo@gmail.com

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Decrypt {

	protected Shell shell;
	int info = SWT.ICON_INFORMATION;
	
	/**
	 * Launch the application.
	 * @wbp.parser.entryPoint
	 */
	public static void decryptStart() {
		try {
			Decrypt window = new Decrypt();
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
		shell.setText("Decrypt");
		//Coordinates of where the shell will be displayed
	    int x = 1300 / 2;
	    int y = 800/ 2;
	    // set the new location 
	    shell.setLocation(x, y);
	    
		/**
		 * MainMenu button: Returns the user to MainMenu screen
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
		btnMainMenu.setBounds(10, 226, 75, 25);
		btnMainMenu.setText("MainMenu");
		
		/**
		 * DecryptTextFiles button: Takes user to DecryptTextFile window
		 */
		Button btnDecryptTextFile = new Button(shell, SWT.NONE);
		btnDecryptTextFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					//Closing Encryption window
					shell.close();
					//Opening decrypt window
					DecryptTxt.decryptTxtStart();
			}
		});
		btnDecryptTextFile.setBounds(168, 84, 94, 36);
		btnDecryptTextFile.setText("Text File");
		
		/**
		 * DecryptImage button: Takes user to DecryptImg window
		 */
		Button btnDecryptImage = new Button(shell, SWT.NONE);
		btnDecryptImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					//Closing Encryption window
					shell.close();
					//Opening decrypt window
					DecryptImg.DecryptImgStart();
			}
		});
		btnDecryptImage.setBounds(168, 138, 94, 36);
		btnDecryptImage.setText("Image");
		
		/**
		 * DecryptFile button: Takes user to DecryptFile window
		 */
		Button btnDecryptFile = new Button(shell, SWT.NONE);
		btnDecryptFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Closing Encryption window
				shell.close();
				//Opening decrypt window
				DecryptFile.DecryptFileStart();		
			}
		});
		btnDecryptFile.setBounds(168, 192, 94, 36);
		btnDecryptFile.setText("Folder");
		
		/**
		 * Help button: Displays a window with additional information
		 */
		Button btnHelp = new Button(shell, SWT.NONE);
		btnHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(shell, info);
				messageBox.setMessage(
						"\u2022To encrypt .txt files select Text File\n\u2022To encrypt .jpg select Image\n\u2022To encrypt folders select Folder\n\u2022To return to MainMenu select Return ");
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
		lblNewLabel.setText("		Welcome to Decryption Menu\r\n                       Please select one of the available options:");

	}
}
