/*
 *  Created by Qianwen on 1/30/2016.
 */
public class MakeOutputClient1 {
    public static final String LF="\n";
    public static final String CRLF="\r\n";
    public static void main(String[] args) {
        System.out.println("CONNECT classroom.cs.unc.edu 21");
	System.out.printf("CONNECT accepted for FTP server at host classroom.cs.unc.edu and port 21%s",CRLF);
	System.out.printf("USER anonymous%s",CRLF);
	System.out.printf("PASS guest@%s",CRLF); 
	System.out.printf("SYST%s",CRLF);
	System.out.printf("TYPE I%s",CRLF);  

        System.out.println("GET file1");
	System.out.printf("GET accepted for file1%s",LF);
        System.out.printf("PORT 152,2,129,144,31,64%s",CRLF);
        System.out.printf("RETR file1%s",CRLF);

	System.out.println("GET     file1 ");
	System.out.printf("GET accepted for file1 %s",LF);
        System.out.printf("PORT 152,2,129,144,31,65%s",CRLF);
        System.out.printf("RETR file1 %s",CRLF);

	System.out.println("CONECT classroom.cs.unc.edu 21");
	System.out.printf("ERROR -- request%s",LF);

	System.out.println("CONNECT classroom.cs.unc.edu 22");
	System.out.printf("CONNECT accepted for FTP server at host classroom.cs.unc.edu and port 22%s",CRLF);  
        System.out.printf("USER anonymous%s",CRLF);
        System.out.printf("PASS guest@%s",CRLF); 
        System.out.printf("SYST%s",CRLF);
        System.out.printf("TYPE I%s",CRLF);

	System.out.println("GET file\u00ff");
	System.out.printf("ERROR -- pathname%s",LF);

	System.out.println("CONNECT classroom\u00ff.cs.unc.edu");
	System.out.printf("ERROR -- server-host%s",LF);		

	System.out.println("CONNECT classroom.cs.unc.edu 65536");
	System.out.printf("ERROR -- server-port%s",LF);

	System.out.println("QUIT");
	System.out.printf("QUIT accepted, terminating FTP client%s",LF);
	System.out.printf("QUIT%s",CRLF);
    }

}
