package main;

public class Result {

	/*
	 * Result class to describe the results of parsing to be used in FTPParser
	 * Each object will have ResultKind and a message to display to the user
	 */
	
	public enum ResultKind {
		VALID,
		INVALID
	}
	
	private ResultKind resultKind;
	private String message;
	
	/*
	 * If the ResultKind is VALID, then default the message string to "Command ok"
	 */
	public Result(ResultKind resultKind, String message) {
		this.resultKind = resultKind;
		this.message = resultKind == ResultKind.VALID ? "Command ok" : "ERROR -- " + message;
	}
	
	public void setResultKind(ResultKind resultKind) {
		this.resultKind = resultKind;
	}
	
	public ResultKind getResultKind() {
		return resultKind;
	}
	
	
	public void setMessage(String message) {
		if (this.resultKind == ResultKind.INVALID) {
			this.message = "ERROR -- " + message;
		}
	}
	public String getMessage() {
		return message;
	}
}
