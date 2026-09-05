import java.net.*;
import java.io.*;

class Client {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 7153);
            System.out.println("Connection done...");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            StatrtReading();
            StartWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void StatrtReading() {
        Runnable r1 = () -> {
            System.out.println("Reader started...");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg == null || msg.equals("exit")) {
                        System.out.println("Server terminated the chat session...");
                        socket.close();
                        break;
                    }

                    System.out.println("Server: " + msg);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(r1).start();
    }

    public void StartWriting() {
        Runnable r2 = () -> {
            System.out.println("Writer started...");
            try {
                while (true && !socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Client is running...");
        new Client();
    }
}
