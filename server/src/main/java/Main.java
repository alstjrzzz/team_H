import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {

    private static final int PORT = 8000;

    private static ServerSocket serverSocket;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {

        startServer();
    }


    public static void startServer() {

        System.out.println("Server started...");
        try { // 서버 및 클라이언트 소켓 생성, 입출력 설정
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected!");

                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                clients.add(clientHandler);

                clientHandler.start(); // 스레드 시작
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }



}
