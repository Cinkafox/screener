import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Handle extends Thread{
    private Socket socket;
    public Handle(Socket socket) {
        this.socket = socket;
    }
    public void run(){
        File file = new File("Screen.png");
        try(OutputStream outputStream = socket.getOutputStream(); InputStream inputStream = socket.getInputStream()){
            String get = new Scanner(inputStream).nextLine();
            if(get.equals("GET / HTTP/1.1")){
                Scanner s = new Scanner(new File("index.html"));
                String temp = s.nextLine();
                while(s.hasNextLine()) temp = temp + s.nextLine();

                System.out.println(temp);
                outputStream.write(setup(temp.length(),"text/html").getBytes(StandardCharsets.UTF_8));
                outputStream.write(temp.getBytes(StandardCharsets.UTF_8));
            }else {
                BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                ImageIO.write(image, "JPEG", file);
                outputStream.write(setup((int) file.length(), "image/JPEG").getBytes(StandardCharsets.UTF_8));
                ImageIO.write(image, "JPEG", outputStream);
            }
        } catch (IOException | AWTException e) {
            System.out.println("Все по пипде у " + socket.getInetAddress());
        }
    }

    public String setup(int length,String type){
        return "HTTP/1.1 200 OK\r\n" +
                "Server: MyWeb\r\n" +
                "Content-Type: " + type +"\r\n" +
                "Content-length: "+ length +" \r\n" +
                "Connection: close\r\n\r\n";
    }

}
