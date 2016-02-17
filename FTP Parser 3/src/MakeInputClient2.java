/*
 *  Created by Qianwen on 1/30/2016.
 */
public class MakeInputClient2 {
    public static final String CRLF="\r\n";
    public static void main(String[] args) {
        
        System.out.printf("220 COMP 431 FTP server ready.%s", CRLF);  
	System.out.printf("920 COMP 431 FTP server ready.%s", CRLF);
	System.out.printf("220 COMP 431 FTP server ready.%s",'\n');  
	System.out.printf("220COMP 431 FTP server ready.%s",CRLF);
	System.out.printf("220 COMP %s FTP server ready.%s",'\u00ff',CRLF);
    }

}
