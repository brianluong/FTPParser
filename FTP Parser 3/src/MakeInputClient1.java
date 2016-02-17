/*
 *  Created by Qianwen on 1/30/2016.
 */
public class MakeInputClient1 {
    public static final String LF="\n";
    public static final String CRLF="\r\n";
    public static void main(String[] args) {
        System.out.printf("CONNECT classroom.cs.unc.edu 21%s", LF);

        System.out.printf("GET file1%s",CRLF);

	System.out.printf("GET     file1 %s",LF);

	System.out.printf("CONECT classroom.cs.unc.edu 21%s",CRLF);

	System.out.printf("CONNECT classroom.cs.unc.edu 22%s",CRLF);

	System.out.printf("GET file%s%s",'\u00ff',LF);

	System.out.printf("CONNECT classroom%s.cs.unc.edu%s",'\u00ff',LF);

	System.out.printf("CONNECT classroom.cs.unc.edu 65536%s",CRLF);

	System.out.printf("QUIT%s", LF);
    }

}
