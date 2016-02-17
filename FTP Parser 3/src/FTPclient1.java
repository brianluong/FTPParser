import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class FTPclient1 {

	private static final String NEW_LINE_CHAR = "\n";
	public static final String CRLF = "\r\n";
	
	public static final String CONNECT_REQUEST = "CONNECT";
	public static final String GET_REQUEST = "GET";
	public static final String QUIT_REQUEST = "QUIT";

	public static final int LENGTH_CONNECT_REQUEST = 7;
	public static final int LENGTH_GET_REQUEST = 3;
	public static final int LENGTH_QUIT_REQUEST = 4;
	
	public static final String ERROR_PREFIX = "ERROR -- ";
	public static final String ERROR_TOKEN_REQUEST = "request";
	public static final String ERROR_TOKEN_SERVERHOST = "server-host";
	public static final String ERROR_TOKEN_SERVERPORT = "server-port";
	public static final String ERROR_TOKEN_PATHNAME = "pathname";
	public static final String ERROR_BEFORE_CONNECT = "expecting CONNECT";
		
	public static final int DEFAULT_PORT_NUMBER = 8000;
	private static final int MAX_SERVER_PORT = 65535;

	public static String SERVER_HOST = "";
	public static String SERVER_PORT = "";
	public static String PATH_NAME = "";
	public static int PORT_NUMBER = DEFAULT_PORT_NUMBER;
	
	public static boolean hasQuit = false;
	public static boolean hasConnected = false;
	// Program 1: FTP Command Generation
	// Read in User inputs to request FTP operations
	// Currently only accept CONNECT, GET, QUIT commands
	
	
	public static void main(String[] args) throws IOException {
		inputLoop();
	}

	private static void inputLoop() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String line = "";
		while ((line = reader.readLine()) != null) {
			System.out.println(line); // Echo the input back to the user
			String request = parseRequest(line);
			line = line.substring(request.length());
			String replyMessage = "";
			
			switch (request) {
			
			case (CONNECT_REQUEST):
				replyMessage = parseConnectRequest(line);
				if (!isErrorMessage(replyMessage)) {
					// Reset the state??
					//PORT_NUMBER = DEFAULT_PORT_NUMBER;
					hasConnected = true;
				}
				break;
			case (GET_REQUEST):
				replyMessage = parseGetRequest(line);
				if (!isErrorMessage(replyMessage) && !hasConnected) {
					replyMessage = ERROR_PREFIX + ERROR_BEFORE_CONNECT + NEW_LINE_CHAR;
				} else if (!isErrorMessage(replyMessage)) {
					PORT_NUMBER++;
				}
				break;
			case (QUIT_REQUEST):
				replyMessage = parseQuitRequest(line);
				if (!isErrorMessage(replyMessage) && !hasConnected) {
					replyMessage = ERROR_PREFIX + ERROR_BEFORE_CONNECT + NEW_LINE_CHAR;
				} else if (!isErrorMessage(replyMessage)) {
					hasQuit = true;					
				}
				break;
			default:
				replyMessage = ERROR_PREFIX + ERROR_TOKEN_REQUEST + NEW_LINE_CHAR;
				break;
			}
			
			System.out.print(replyMessage);
			if (hasQuit) {
				System.exit(0);
			}
			
		}
	}
	
	private static String parseQuitRequest(String line) {
		if (line.length() != 0) {
			return ERROR_PREFIX + ERROR_TOKEN_REQUEST + NEW_LINE_CHAR;
		}
		return generateValidQuitReply();
	}
	
	private static String parseGetRequest(String line) throws UnknownHostException {
		try {
			line = removeLeadingWhitespace(line);
		} catch(Exception e) {
			return ERROR_PREFIX + ERROR_TOKEN_REQUEST + NEW_LINE_CHAR;
		}
		
		PATH_NAME = "";
		try {
			line = parsePathName(line); 
		} catch(Exception e) {
			return ERROR_PREFIX + ERROR_TOKEN_PATHNAME + NEW_LINE_CHAR;
		}
		
		return generateValidGetReply();
	}
	
	private static String parsePathName(String line) throws Exception {
		for (int i = 0; i < line.length(); i++) {
			char currentChar = line.charAt(i);
			// Check if ASCII
			if (currentChar < 0 || currentChar > 128) {
				throw new Exception();
			}
			PATH_NAME += line.charAt(i);
		}
		return line;
	}
	
	private static String parseConnectRequest(String line) {
		SERVER_HOST = "";
		SERVER_PORT = "";
		try {
			line = removeLeadingWhitespace(line);
		} catch(Exception e) {
			return ERROR_PREFIX + ERROR_TOKEN_REQUEST + NEW_LINE_CHAR;
		}
		
		try {
			line = parseServerHost(line);
		} catch(Exception e) {
			return ERROR_PREFIX + ERROR_TOKEN_SERVERHOST + NEW_LINE_CHAR;
		}
		
		try {
			line = removeLeadingWhitespace(line);
		} catch(Exception e) {
			return ERROR_PREFIX + ERROR_TOKEN_SERVERHOST + NEW_LINE_CHAR;
		}
		
		try {
			line = parseServerPort(line);
		} catch(Exception e) {
			return ERROR_PREFIX + ERROR_TOKEN_SERVERPORT + NEW_LINE_CHAR;
		}
		
		return generateValidConnectReply();
	}
	
	private static String parseServerPort(String line) throws Exception {
		if (line.length() > 0 || line.length() < 6) {
			for (int i = 0 ; i < line.length(); i++) {
				if (!isDigit(line.charAt(i))) {
					throw new Exception("");
				}
				SERVER_PORT += line.charAt(i);
			}
			// Range of values the port can take
			if (Integer.parseInt(SERVER_PORT) < 0 || Integer.parseInt(SERVER_PORT) > MAX_SERVER_PORT) {
				throw new Exception("");
			}
		} else {
			throw new Exception("");
		}
		return line;
	}
	
	public static String parseServerHost(String line) {
		return parseDomain(line);
	}
	
	public static String parseDomain(String line) {
		try {
			line = parseElement(line);
		} catch(Exception e) {
			return ERROR_PREFIX + ERROR_TOKEN_SERVERHOST + NEW_LINE_CHAR;
		}
		
		if (line.length() > 0) {
			if (line.charAt(0) == '.') {
				try {
					SERVER_HOST += ".";
					line = parseDomain(line.substring(1));
				} catch (Exception e) {
					return ERROR_PREFIX + ERROR_TOKEN_SERVERHOST + NEW_LINE_CHAR;
				}
			}
		}
		return line;
	}
	
	public static String parseElement(String line) throws Exception {
		try {
			line = parseA(line);
		} catch (Exception e) {
			return ERROR_PREFIX + ERROR_TOKEN_SERVERHOST + NEW_LINE_CHAR;
		}
		
		try {
			line = parseLetDigStr(line);
		} catch (Exception e) {
			return ERROR_PREFIX + ERROR_TOKEN_SERVERHOST + NEW_LINE_CHAR;
		}
		return line;
	}
	
	private static String parseA(String line) throws Exception {
		if (isAlphaNumeric(line.charAt(0))) {
			SERVER_HOST += line.charAt(0);
			return line.substring(1);
		} else {
			throw new Exception("");
		}
	}
	
	private static String parseLetDig(String line) throws Exception {
		if (isLetDig(line.charAt(0))) {
			SERVER_HOST += line.charAt(0);
			return line.substring(1);
		} else {
			throw new Exception("");
		}
	}

	private static boolean isLetDig(char c) {
		return isAlphaNumeric(c) || isDigit(c);
	}
	
	private static boolean isAlphaNumeric(char c) {
		return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
	}
	
	private static boolean isDigit(char c) {
		return c >= 48 && c <= 57;
	}
	
	public static String parseLetDigStr(String line) {
		try {
			line = parseLetDig(line);
		} catch (Exception e) {
			return ERROR_PREFIX + ERROR_TOKEN_SERVERHOST + NEW_LINE_CHAR;
		}
		
		if (line.length() > 0 && isLetDig(line.charAt(0))) {
			try {
				line = parseLetDigStr(line);
			} catch(Exception e) {
				return ERROR_PREFIX + ERROR_TOKEN_SERVERHOST + NEW_LINE_CHAR;
			}
		}
		return line;
	}
	
	private static String removeLeadingWhitespace(String line) throws Exception {
		int whiteSpaceCount = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == ' ') {
				whiteSpaceCount++;
			} else {
				break;
			}
		}
		if (whiteSpaceCount == 0) {
			throw new Exception("");
		}
		return line.substring(whiteSpaceCount);
	}
	 
	private static String parseRequest(String line) {
		String request = "";
		for (int i = 0; i < line.length(); i++) {
			char currentChar = line.charAt(i);
			if (currentChar != ' ') {
				request += currentChar;
			} else {
				break;
			}
		}
		return request.toUpperCase();
	}
	
	private static String generateValidQuitReply() {
		return "QUIT accepted, terminating FTP client\nQUIT\r\n";
	}
	
	private static String generateValidGetReply() throws UnknownHostException {
		String myIP;
		InetAddress myInet;
		myInet = InetAddress.getLocalHost();
		myIP = myInet.getHostAddress();
		
		String myIPConverted = "";
		for (int i = 0; i < myIP.length(); i++) {
			if (myIP.charAt(i) == '.') {
				myIPConverted += ",";
			} else {
				myIPConverted += myIP.charAt(i);				
			}
		}
		
		myIPConverted += "," + PORT_NUMBER / 256 + "," + PORT_NUMBER % 256;
		
		return "GET accepted for " 
				+ PATH_NAME  + NEW_LINE_CHAR
				+ "PORT " + myIPConverted + CRLF
				+ "RETR " + PATH_NAME + CRLF;
	}
	
	private static String generateValidConnectReply() {
		return "CONNECT accepted for FTP server at host " 
				+ SERVER_HOST 
				+ " and port " 
				+ SERVER_PORT + CRLF
				+ "USER anonymous\r\nPASS guest@\r\nSYST\r\nTYPE I\r\n";
	}
	
	private static boolean isErrorMessage(String replyMessage) {
		return replyMessage.substring(0, ERROR_PREFIX.length()).equals(ERROR_PREFIX);
	}
}