import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class FTPclient2 {
	
	// Parsing FTP replies
	// Decided to use InputStreamReader in this one just to try it out, a bit complicated
	
	private static final int REPLY_CODE_MAX = 599;
	private static final int REPLY_CODE_MIN = 100;
	
	public static final String ERROR_PREFIX = "ERROR -- ";
	public static final String ERROR_REPLY_CODE = "reply-code";
	public static final String ERROR_REPLY_TEXT = "reply-text";
	public static final String ERROR_CRLF = "<CRLF>";
	
	private static final int CHAR_EOF = -1;
	private static final char CHAR_CARRIAGE_RETURN = '\r';
	private static final char CHAR_NEW_LINE = '\n';
	
	public static int nextChar;
	
	public static void main(String[] args) throws IOException {
		InputStreamReader inputStream = new InputStreamReader(System.in);

		nextChar = inputStream.read();
		while (nextChar != CHAR_EOF ) {
			
			// Parse Reply Code
			String replyCode = "";
			while (nextChar != ' ' 
					&& nextChar != CHAR_CARRIAGE_RETURN 
					&& nextChar != CHAR_NEW_LINE 
					&& nextChar != CHAR_EOF) {
				replyCode += (char) nextChar;
				nextChar = inputStream.read();
			}
			
			try {
				parseReplyCode(replyCode);
			} catch (Exception e) {
				// Reply Code Error
				System.out.print(replyCode + (nextChar != CHAR_EOF ? ((char) nextChar) + readUntilEndOfCommand(inputStream) : ""));
				System.out.println(ERROR_PREFIX + ERROR_REPLY_CODE);
				continue;
			}
			
			if (nextChar == ' ') {
				// Move past the space
				nextChar = inputStream.read();				
			} else {
				// Reply Code Error
				System.out.print(replyCode + (nextChar != CHAR_EOF ? (char) nextChar + readUntilEndOfCommand(inputStream) : ""));
				System.out.println(ERROR_PREFIX + ERROR_REPLY_CODE);
				continue;
			}

			// Parse Reply Text
			String replyText = "";
			while (nextChar != CHAR_CARRIAGE_RETURN 
					&& nextChar != CHAR_NEW_LINE
					&& nextChar != CHAR_EOF) {
				replyText += (char) nextChar;
				nextChar = inputStream.read();
			}
			
			try {
				parseReplyText(replyText);
			} catch (Exception e) {
				// Reply Text Error
				System.out.print(replyCode + " " + replyText + (nextChar != CHAR_EOF ? (char) nextChar + readUntilEndOfCommand(inputStream) : ""));
				System.out.println(ERROR_PREFIX + ERROR_REPLY_TEXT);
				continue;
			}
			
			// Validate CRLF
			String CLRF = "" + (char) nextChar;
			if (nextChar == CHAR_CARRIAGE_RETURN) {
				nextChar = inputStream.read();
				if (nextChar == CHAR_NEW_LINE) {
					CLRF += (char) nextChar;
					nextChar = inputStream.read();
				} else {
					System.out.print(replyCode + " " + replyText + CLRF);
					System.out.println(ERROR_PREFIX + ERROR_CRLF);
					continue;
				}
			} else if (nextChar == CHAR_NEW_LINE) {
				System.out.print(replyCode + " " + replyText + CLRF);
				System.out.println(ERROR_PREFIX + ERROR_CRLF);
				nextChar = inputStream.read();
				continue;
			} else {
				System.out.print(replyCode + " " + replyText);
				System.out.println(ERROR_PREFIX + ERROR_CRLF);
				continue;
			}
			
			// Echo Command
			System.out.print(replyCode + " " + replyText + CLRF);
			// Valid FTP Reply Output
			System.out.println("FTP reply " + replyCode + " accepted. Text is : " + replyText);
		}
	}

	private static void parseReplyCode(String replyCode) throws Exception {
		int replyCodeInteger = Integer.parseInt(replyCode);
		if (replyCodeInteger < REPLY_CODE_MIN || replyCodeInteger > REPLY_CODE_MAX) {
			throw new Exception();
		}
		
	}
	
	private static void parseReplyText(String replyText) throws Exception {
		if (replyText.length() == 0) {
			throw new Exception();
		}
	
		for (int i = 0; i < replyText.length(); i++) {
			char currentChar = replyText.charAt(i);
			// Check if ASCII
			if (currentChar < 0 || currentChar > 128) {
				throw new Exception();
			}
		}
	}

	private static String readUntilEndOfCommand(InputStreamReader inputStream) throws IOException {
		String restOfCommand = "";
		nextChar = inputStream.read();
		while (nextChar != CHAR_CARRIAGE_RETURN && nextChar != CHAR_NEW_LINE && nextChar != CHAR_EOF) {
			restOfCommand += (char) nextChar;
			nextChar = inputStream.read();
		}
		
		// Get the CRLF characters at the end of a command
		if (nextChar != CHAR_EOF) {
			restOfCommand += (char) nextChar;
			if (nextChar == CHAR_CARRIAGE_RETURN) {
				nextChar = inputStream.read();
				if (nextChar == CHAR_NEW_LINE) {
					restOfCommand += (char) nextChar;
					nextChar = inputStream.read();
				}
			} else if (nextChar == CHAR_NEW_LINE) {
				nextChar = inputStream.read();
			}
		}

		return restOfCommand;
	}
}