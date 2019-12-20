import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(20006);
        Socket client = null;
        boolean f = true;
        while(f){
            System.out.println("Start");
            client = server.accept();
            System.out.println("Connection established.");
            new Thread(new ServerThread(client)).start();
        }
        server.close();
    }
}
