package main;

import java.io.IOException;
import java.util.Scanner;

import main.Result.ResultKind;

public class FTP1Server {
	
	/*
	 * FTPParser class to parse FTP commands on an input source
	 * Returns the correctness based on FTP grammar rules
	 */
	
	private static final String ERROR_MESSAGE_CLRF = "CRLF";
	private static final String ERROR_MESSAGE_COMMAND = "command";
	private static final String ERROR_MESSAGE_TYPECODE = "type-code";
	private static final String ERROR_MESSAGE_PASSWORD = "password";
	private static final String ERROR_MESSAGE_USERNAME = "username";
	private static final char[] typeCodes = {'A','I'};
					
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in).useDelimiter("\n");
		while (sc.hasNext()) {
			String line = sc.next();
			String[] commands = line.split("\r");
			
			for (int i = 0; i < commands.length; i++) {
				Result r = parse(commands[i]);
				if (i != commands.length-1) {
					// All these commands (here) are erroneous, since each of these commands will end with only <CR>
					r.setResultKind(ResultKind.INVALID);
					r.setMessage(ERROR_MESSAGE_CLRF);
					// Re-add '\r'
					System.out.print(commands[i] + '\r');
				} else {
					if (line.charAt(line.length()-1) != '\r' && r.getResultKind() != ResultKind.INVALID){
						// Not <CR><LF>, so need to mark invalid if valid
						r.setResultKind(ResultKind.INVALID);
						r.setMessage(ERROR_MESSAGE_CLRF);
						System.out.println(commands[i]);
					} else if (line.charAt(line.length()-1) == '\r'){
						// Re-add '\r'
						System.out.println(commands[i] + '\r');
					} else {
						// Already invalid
						System.out.println(commands[i]);
					}
				}
				System.out.println(r.getMessage());
			}
		}
	}
	
	public static Result parse(String line) {
		Token command = parseCommand(line);
		line = line.substring(command.getTokenString().length());
		Result result = null;
		Token whiteSpace = parseWhiteSpace(line);
		
		switch (command.getTokenKind()) {
			case USER:
				if (whiteSpace.getTokenKind() == Token.TokenKind.WHITESPACE) {
					line = line.substring(whiteSpace.getTokenString().length());
					result = parseParameter(line);
					if (result.getResultKind() == Result.ResultKind.INVALID) {
						result.setMessage(ERROR_MESSAGE_USERNAME);
					}
				} else {
					result = new Result(Result.ResultKind.INVALID, ERROR_MESSAGE_COMMAND);
				}
				break;
			case PASS:
				if (whiteSpace.getTokenKind() == Token.TokenKind.WHITESPACE) {
					line = line.substring(whiteSpace.getTokenString().length());
					result = parseParameter(line);
					if (result.getResultKind() == Result.ResultKind.INVALID) {
						result.setMessage(ERROR_MESSAGE_PASSWORD);
					}
				} else {
					result = new Result(Result.ResultKind.INVALID, ERROR_MESSAGE_COMMAND);
				}
				break;
			case TYPE:
				if (whiteSpace.getTokenKind() == Token.TokenKind.WHITESPACE) {
					line = line.substring(whiteSpace.getTokenString().length());
					result = parseTypeCodeParameter(line);
					if (result.getResultKind() == Result.ResultKind.INVALID) {
						result.setMessage(ERROR_MESSAGE_TYPECODE);
					}
				} else {
					result = new Result(Result.ResultKind.INVALID, ERROR_MESSAGE_COMMAND);
				}
				break;
			case SYST:
				result = parseNoArgCommand(line);
				break;
			case NOOP:
				result = parseNoArgCommand(line);
				break;
			case QUIT:
				result = parseNoArgCommand(line);
				break;
			default:
				result = new Result(Result.ResultKind.INVALID, ERROR_MESSAGE_COMMAND);
		}
		return result;
	}

	private static Result parseNoArgCommand(String line) {
		Result result;
		if (line.length() == 0) {
			result = new Result(Result.ResultKind.VALID, "");

		} else {
			result = new Result(Result.ResultKind.INVALID, ERROR_MESSAGE_CLRF);					
		}
		return result;
	}
	
	private static Token parseCommand(String line) {
		String currentToken = "";
		for (int i = 0; i < line.length(); i++) {
			char currentChar = line.charAt(i);
			if (currentChar != ' ') {
				currentToken += currentChar;
			} else {
				break;
			}
		}
		return new Token(currentToken);
	}
	
	private static Token parseWhiteSpace(String line) {
		String whiteSpace = "";
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == ' ') {
				whiteSpace += ' ';
			} else {
				break;
			}
		}
		return new Token(whiteSpace);
	}
	
	private static Result parseParameter(String line) {
		for (int i = 0; i < line.length(); i++) {
			char currentChar = line.charAt(i);
			// Check if ASCII
			if (currentChar < 0 || currentChar > 128) {
				return new Result(Result.ResultKind.INVALID, line);
			}
		}
		if (line.length() == 0) {
			return new Result(Result.ResultKind.INVALID, line);
		}
		return new Result(Result.ResultKind.VALID, "");
	}
	
	private static Result parseTypeCodeParameter(String line) {
		if (line.length() != 1) { // optimization
			return new Result(Result.ResultKind.INVALID, line);
		} 
		char typeCode = line.charAt(0);
		if (isValidTypeCode(typeCode)) {
			return new Result(Result.ResultKind.VALID, "");
		}
		return new Result(Result.ResultKind.INVALID, ERROR_MESSAGE_TYPECODE);
	}
	
	private static boolean isValidTypeCode(char typeCode) {
		for (char c : typeCodes) {
			if (typeCode == c) {
				return true;
			}
		}
		return false;
	}
}