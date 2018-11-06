//Created by Mateusz Szymanski
//email - Matreoxioo@gmail.com

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

public class EncryptTxt {

	protected Shell shell;
	private Text text;
	private Text text_1;
	private Label lblNewLabel;
	private Label lblPassword;
	private String textPath;
	int error = SWT.ICON_ERROR;
	int info = SWT.ICON_INFORMATION;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public static void encryptTxtStart() {
		try {
			EncryptTxt window = new EncryptTxt();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open shell window
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
		shell.setText("Text File Encryption");
		// Coordinates of where the shell will be displayed
		int x = 1300 / 2;
		int y = 800 / 2;
		// set the new location
		shell.setLocation(x, y);

	    /**
	     * Browse button: Allows user to browse through his files
	     */
		Button btnBrowse = new Button(shell, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Open file dialog and allow user to look for a .txt file and
				// to save selected file path
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
		 * Text boxes
		 */
		// Text field for file path
		text = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text.setBounds(114, 141, 275, 21);

		// Text field for user password
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(114, 173, 275, 21);

		/**
		 * Used to initialise encryption
		 */
		Button btnEncrypt = new Button(shell, SWT.NONE);
		btnEncrypt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Try to perform encryption
				try {
					// text path = path of file that is to be decrypted
					textPath = text.getText();
					// password = password that was set on the file
					String password = text_1.getText();
					encryptFile(textPath, password);
					//Delete source file
					 Files.deleteIfExists(Paths.get(textPath));
					 
					//Encryption confirmation message
					MessageBox messageBox = new MessageBox(shell, info);
					messageBox.setMessage("Text file encrypted");
					messageBox.open();
				} catch (Exception e1) {
					//Error if no file path found
					MessageBox messageBox = new MessageBox(shell, error);
					messageBox.setMessage("No file chosen");
					messageBox.open();
				}
			}
		});
		btnEncrypt.setBounds(170, 216, 83, 35);
		btnEncrypt.setText("Encrypt");

		/**
		 * Returns to main menu
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
		btnMainMenu.setBounds(23, 226, 75, 25);
		btnMainMenu.setText("MainMenu");

		/**
		 * Labels
		 */
		lblNewLabel = new Label(shell, SWT.BORDER);
		lblNewLabel.setBounds(24, 22, 385, 86);
		lblNewLabel.setText(
				" 1.Select \"Browse\" to search through your files\r\n 2.Select \"Open\" or double-click targer .txt file\r\n 3.Type in a password for additional security \r\n (Note: You can leave the password field empty)\r\n 4.Press \"Encrypt\" to encrypt the selected text file");

		lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setBounds(25, 176, 75, 21);
		lblPassword.setText("Password:");

	}

	/**
	 * Random 8-byte keyBytes sequence:
	 */
	private static final byte[] keyBytes = { (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7, (byte) 0x5b,
			(byte) 0xd7, (byte) 0x45, (byte) 0x17 };

	/**
	 * Create the cipher which will be used to encrypt .txt file
	 */
	private static Cipher makeCipher(String pass, Boolean decryptMode) throws GeneralSecurityException {

		// Use a KeyFactory to derive the corresponding key from the passphrase:
		PBEKeySpec keySpec = new PBEKeySpec(pass.toCharArray());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(keySpec);

		// Create parameters from the keyBytes and random number of
		// iterations:
		PBEParameterSpec pbeParamSpec = new PBEParameterSpec(keyBytes, 42);

		// Set up the cipher:
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

		// Set the cipher mode to encryption:
		cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
		
		return cipher;
	}

	/**
	 * Encrypts file using a key derived from a passphrase:
	 */
	public static void encryptFile(String fileName, String pass) throws IOException, GeneralSecurityException {
		byte[] decData;
		byte[] encData;
		File inFile = new File(fileName);
		// Generate the cipher using pass:
		Cipher cipher = EncryptTxt.makeCipher(pass, true);

		// Read in the file:
		FileInputStream inStream = new FileInputStream(inFile);

		int blockSize = 8;
		// Figure out how many bytes are padded
		int paddedCount = blockSize - ((int) inFile.length() % blockSize);

		// Figure out full size including padding
		int padded = (int) inFile.length() + paddedCount;

		decData = new byte[padded];

		inStream.read(decData);

		inStream.close();

		// Write out padding bytes as per PKCS5 algorithm
		for (int i = (int) inFile.length(); i < padded; ++i) {
			decData[i] = (byte) paddedCount;
		}

		// Encrypt the file data:
		encData = cipher.doFinal(decData);

		// Write the encrypted data to a new file:
		FileOutputStream outStream = new FileOutputStream(new File(fileName + ".encrypted"));
		outStream.write(encData);
		outStream.close();
	}
}
