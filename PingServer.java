import java.io.*;
import java.net.*;
import java.util.*;

class PingServer {
    public static void main(String args[]) throws Exception {
        System.out.println("PingServer started. Waiting for connections...");

        ServerSocket ss = new ServerSocket(5555);
        
        Socket s = ss.accept();

        System.out.println("Connection established with client: " + s.getInetAddress());

        int c = 0;
        while (c < 4) {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintStream out = new PrintStream(s.getOutputStream());
            
            String str = br.readLine();
            System.out.println("Received message from client: " + str);
            
            out.println("Reply from " + InetAddress.getLocalHost() + "; Length: " + str.length());
            
            c++;
        }

        s.close();

        ss.close();

        System.out.println("PingServer closed.");
    }
}
