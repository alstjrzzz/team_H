package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 8000;

    private static ServerSocket serverSocket;
    private static GameState gameState;

    public static void main(String[] args) {

        startServer();
    }


    public static void startServer() {

        System.out.println("Server started...");

        try { // 서버 및 클라이언트 소켓 생성, 입출력 설정
            serverSocket = new ServerSocket(PORT);
            gameState = new GameState();

            Socket client1 = serverSocket.accept();
            System.out.println("Client 1 connected");
            Thread clientHandler1 = new Thread(new ClientHandler(client1, 1, gameState));
            clientHandler1.start();

            Socket client2 = serverSocket.accept();
            System.out.println("Client 2 connected");
            Thread clientHandler2 = new Thread(new ClientHandler(client2, 2, gameState));
            clientHandler2.start();


            // 메인 스레드는 여기서 두 스레드가 끝날 때까지 대기
            clientHandler1.join();
            clientHandler2.join();

        } catch (IOException | InterruptedException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            cleanUp();
            System.exit(0);
        }
    }


    private static void cleanUp() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error during cleanup: " + e.getMessage());
        }
    }

}
