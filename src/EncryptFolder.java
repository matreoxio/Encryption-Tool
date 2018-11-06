//Created by Mateusz Szymanski
//email - Matreoxioo@gmail.com

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class EncryptFolder {
	

	protected Shell shell;
	private Text text;
	private Text text_1;
	private Label lblNewLabel;
	private Label lblPassword;
	int error = SWT.ICON_ERROR;
	int info = SWT.ICON_INFORMATION;
	private String textPath;

	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public static void encryptFolderStart() {
		try {
			EncryptFolder window = new EncryptFolder();
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
		shell.setText("Folder Encryption");
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
				// Open file dialog and allow user to look for a file and to
				// save selected files path
				try {
				    DirectoryDialog dialog = new DirectoryDialog(shell);
				    dialog.setFilterPath("c:\\"); // Windows specific
					String fileName = dialog.open();
					if (fileName != null) {
						text.setText(fileName);
						textPath = text.getText();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		btnBrowse.setBounds(23, 139, 75, 25);
		btnBrowse.setText("Browse");
		
		/**
		 * Encrypt Button: Initialises file encryption sequence
		 */
		Button btnEncrypt = new Button(shell, SWT.NONE);
		btnEncrypt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Try to find encrypted file
				
				try {
					// text path = path of file that is to be encrypted
					textPath = text.getText();
					// password = password that was set on the file
					String password = text_1.getText();
					
					EncryptFolder zf = new EncryptFolder();
					zf.zipFolder(Paths.get(textPath), Paths.get(textPath+".zip"));
					textPath = textPath + ".zip";
					
					//Encrypts zip folder
					encryptFile(textPath, password);
					//Deletes zip folder
					 Files.deleteIfExists(Paths.get(textPath));
					 //Delete the source directory
					textPath = textPath.replace(".zip", "");
					File file = new File(textPath);
					deleteDir(file);
					//Informs the user that his file has been encrypted
					MessageBox messageBox = new MessageBox(shell, info);
					 messageBox.setMessage("Folder encrypted");
					 messageBox.open();
				} catch (Exception e1) {
	                System.err.println(e1);
					 MessageBox messageBox = new MessageBox(shell, error);
					 messageBox.setMessage("No file chosen");
					 messageBox.open();
				}
			}
		});
		btnEncrypt.setBounds(170, 216, 83, 35);
		btnEncrypt.setText("Encrypt");

		/**
		 * MainMenu button: Returns to main menu
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
		//Text field for file path
		text = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text.setEditable(false);
		text.setBounds(114, 141, 275, 21);
		
		//Text field for user password
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(114, 173, 275, 21);
		
		/**
		 * Labels
		 */
		lblNewLabel = new Label(shell, SWT.BORDER);
		lblNewLabel.setBounds(24, 22, 385, 86);
		lblNewLabel.setText(
				" 1.Select \"Browse\" to search through your files\r\n 2.Select Open to get path of the desired folder\r\n 3.Type in a password for additional security \r\n (Note: You can leave the password field empty)\r\n 4.Press \"Encrypt\" to encrypt the selected folder");

		lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setBounds(25, 176, 75, 21);
		lblPassword.setText("Password:");
	}
	
	/**
	 *  Uses java.util.zip to create zip file
	 */
    private void zipFolder(Path sourceFolderPath, Path zipPath) throws Exception {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
        Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                Files.copy(file, zos);
                zos.closeEntry();
                return FileVisitResult.CONTINUE;
            }
        });
        zos.close();
    }

	/**
	 * Random 8-byte keyBytes sequence:
	 */
	private static final byte[] keyBytes = { (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7, (byte) 0x5b, (byte) 0xd7,
			(byte) 0x45, (byte) 0x17 };

	/**
	 * Create the cipher which will be used to encrypt a folder
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

		// Set the cipher mode to encryption:
		cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);

		return cipher;
	}

	/**
	 * Encrypts one file to a second file using a key derived from a passphrase:
	 */
	public static void encryptFile(String fileName, String pass) throws IOException, GeneralSecurityException {
		byte[] decData;
		byte[] encData;
		File inFile = new File(fileName);
		// Generate the cipher using pass:
		Cipher cipher = EncryptFolder.makeCipher(pass, true);

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
		fileName = fileName.replace(".zip", "");
		FileOutputStream outStream = new FileOutputStream(new File(fileName + ".encrypted"));
		outStream.write(encData);
		outStream.close();
	}
    
	/**
	 * Delete source directory
	 */
	void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}
}
