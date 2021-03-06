//Created by Mateusz Szymanski
//email - Matreoxioo@gmail.com

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DecryptImg {

	protected Shell shell;
	private Text text;
	private Text text_1;
	// Style of error message
	int error = SWT.ICON_ERROR;
	int info = SWT.ICON_INFORMATION;
	private String textPath;

	/**
	 * Launch the application.
	 * 
	 */
	public static void DecryptImgStart() {
		try {
			DecryptImg window = new DecryptImg();
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
		shell.setText("Image Decryption");
		//Coordinates of where the shell will be displayed
	    int x = 1300 / 2;
	    int y = 800/ 2;
	    // set the new location 
	    shell.setLocation(x, y);
		
	    /**
	     * Browse button: Allows user to browse through his files
	     */
		Button btnBrowse = new Button(shell, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Open file dialog and allow user to look for a file and to
				// save selected files path to text
				try {
					final Shell shell = new Shell();
					FileDialog dlg = new FileDialog(shell, SWT.OPEN);
					String fileName = dlg.open();
					if (fileName != null) {
						text.setText(fileName);
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnBrowse.setBounds(23, 139, 75, 25);
		btnBrowse.setText("Browse");

		/**
		 * Decrypt button: Initializes decrypt sequence
		 */
		Button btnDecrypt = new Button(shell, SWT.NONE);
		btnDecrypt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Try to decrypt an image
				try {
					// text path = path of file that is to be decrypted
					String textPath = text.getText();
					// password = password that was set on the file
					String password = text_1.getText();
					decryptFile(textPath, password);
					//Delete source file
					 Files.deleteIfExists(Paths.get(textPath));
					// Confirm image decryption
					MessageBox messageBox = new MessageBox(shell, info);
					messageBox.setMessage("Image decrypted");
					messageBox.open();
				} 
				// file not chosen error
				catch (IOException e1) {
					MessageBox messageBox = new MessageBox(shell, error);
					messageBox.setMessage("No file chosen");
					messageBox.open();
				} 
				// Incorrect password error
				catch (GeneralSecurityException e2) {
					MessageBox messageBox = new MessageBox(shell, error);
					messageBox.setMessage("Incorrect password");
					messageBox.open();
				}
			}
		});
		btnDecrypt.setBounds(170, 216, 83, 35);
		btnDecrypt.setText("Decrypt");

		/**
		 * MainMenu Button: Return to Main Menu
		 */
		Button btnMainMenu = new Button(shell, SWT.NONE);
		btnMainMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					// Closing Encryption window
					shell.close();
					// Opening main menu window
					MainMenu.main(null);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnMainMenu.setBounds(23, 226, 75, 25);
		btnMainMenu.setText("Main Menu");
		
		
		/**
		 * Text boxes
		 */
		// Text field for file path
		text = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text.setBounds(114, 141, 275, 21);

		// Text field for user password
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(114, 173, 275, 21);

		
		/**
		 * labels
		 */
		Label lblpressbrowse = new Label(shell, SWT.BORDER);
		lblpressbrowse.setText(
				" 1.Select \"Browse\" to search through your files\r\n 2.Select \"Open\" or double-click the encrypted .jpg file\r\n 3.Type in the password to decrypt the file\r\n (Note: If selected file is not password protected leave this field empty)\r\n 4.Click\"Decrypt\" to decrypt your selected image file");
		lblpressbrowse.setBounds(24, 22, 385, 86);

		Label lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setBounds(25, 176, 75, 21);
		lblPassword.setText("Password: ");

	}

	/**
	 * Random 8-byte keyBytes sequence:
	 */
	private static final byte[] keyBytes = { (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7, (byte) 0x5b, (byte) 0xd7,
			(byte) 0x45, (byte) 0x17 };

	/**
	 * Create the cipher which will be used to decrypt .jpg file
	 */
	private static Cipher makeCipher(String pass, Boolean decryptMode) throws GeneralSecurityException {

		// Use a KeyFactory to derive the corresponding key from the passphrase:
		PBEKeySpec keySpec = new PBEKeySpec(pass.toCharArray());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(keySpec);

		// Create parameters from the salt and an arbitrary number of
		// iterations:
		PBEParameterSpec pbeParamSpec = new PBEParameterSpec(keyBytes, 42);

		// Set up the cipher:
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

		// Set the cipher mode to decryption:
		cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);

		return cipher;
	}

	/**
	 * Decrypts one file to a second file using a key derived from a passphrase:
	 */
	public static void decryptFile(String fileName, String pass) throws GeneralSecurityException, IOException {
		byte[] encData;
		byte[] decData;
		File inFile = new File(fileName);

		// Generate the cipher using pass:
		Cipher cipher = DecryptImg.makeCipher(pass, false);

		// Read in the file:
		FileInputStream inStream = new FileInputStream(inFile);
		encData = new byte[(int) inFile.length()];
		inStream.read(encData);
		inStream.close();
		// Decrypt the file data:
		decData = cipher.doFinal(encData);

		// Figure out how much padding to remove

		int padCount = (int) decData[decData.length - 1];

		// Naive check, will fail if plaintext file actually contained
		// this at the end
		// For robust check, check that padCount bytes at the end have same
		// value
		if (padCount >= 1 && padCount <= 8) {
			decData = Arrays.copyOfRange(decData, 0, decData.length - padCount);
		}

		// Write the decrypted data to a new file:
		fileName = fileName.replace(".encrypted", "");
		FileOutputStream target = new FileOutputStream(new File(fileName));
		target.write(decData);
		target.close();
	}

}
