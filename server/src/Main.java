import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 8000;

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) {

        System.out.println("Server Start...");
        startServer();
        testInputOutput();
    }


    public static void startServer() {

        try { // 서버 및 클라이언트 소켓 생성, 입출력 설정
            serverSocket = new ServerSocket(PORT);
            clientSocket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 테스트
    public static void testInputOutput() {
        // 클라이언트로부터 메시지 수신
        String clientMessage;
        try {
            clientMessage = in.readLine();
            System.out.println("Received from client: " + clientMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 클라이언트에게 응답
        out.println("Hello from Server");
    }
}
