import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if(!args[0].isEmpty()){
            try {
                port = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                System.out.println("Але этож не числа!");
            }
        }
        ServerSocket serverSocket = new ServerSocket(port);
        while (true){
            Socket socket = serverSocket.accept();
            Handle handle = new Handle(socket);
            handle.start();
        }
    }
}
