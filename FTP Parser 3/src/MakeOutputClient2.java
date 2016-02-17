/*
 *  Created by Qianwen on 1/30/2016.
 */
public class MakeOutputClient2 {
    public static final String LF="\n";
    public static final String CRLF="\r\n";
    public static void main(String[] args) {
        
        System.out.printf("220 COMP 431 FTP server ready.%s", CRLF);  
	System.out.println("FTP reply 220 accepted. Text is : COMP 431 FTP server ready.");
	System.out.printf("920 COMP 431 FTP server ready.%s", CRLF);
	System.out.println("ERROR -- reply-code");
	System.out.printf("220 COMP 431 FTP server ready.%s",LF);  
	System.out.println("ERROR -- <CRLF>");
	System.out.printf("220COMP 431 FTP server ready.%s",CRLF);
	System.out.println("ERROR -- reply-code");
	System.out.printf("220 COMP %s FTP server ready.%s",'\u00ff',CRLF);
	System.out.println("ERROR -- reply-text");
    }

}
