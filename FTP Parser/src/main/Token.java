package main;

public class Token {
	
	/*
	 * Token class used in FTPParser to disambiguate and aid in parsing
	 */
	
	/*
	 * TokenKind describes the kinds of tokens that will be used in FTPParser
	 */
	public enum TokenKind{
		USER,
		PASS,
		TYPE,
		SYST,
		NOOP,
		QUIT,
		WHITESPACE,
		UNRECOGNIZED
	}
	
	private final String[] tokenKinds = 
		{"user", 
		 "pass",
		 "type",
		 "syst",
		 "noop",
		 "quit"};
	
	private int numTokenKinds = 6;
	
	private TokenKind tokenKind;
	private String tokenString;
	
	public Token(String tokenString) {
		matchTokenString(tokenString);
		this.tokenString = tokenString;
	}
	/*
	 * Aids in matching strings with the TokenKind enum
	 */
	private void matchTokenString(String tokenString) {
		boolean matched = false;
		for (int i = 0; i < numTokenKinds; i++) {
			if (tokenKinds[i].equalsIgnoreCase(tokenString)) {
				this.tokenKind = TokenKind.values()[i];
				matched = true;
				break;
			}
		}
		
		if (!matched) {
			// Differentiate between "" (UNRECOGNIZED) and "    " (WHITESPACE)
			if (tokenString.length() > 0 && tokenString.trim().length() == 0) {
				this.tokenKind = TokenKind.WHITESPACE;
			} else {
				this.tokenKind = TokenKind.UNRECOGNIZED;
			}
		}
	}
	
	public TokenKind getTokenKind() {
		return this.tokenKind;
	}
	
	public String getTokenString() {
		return this.tokenString;
	}
}