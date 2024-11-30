import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    private static final int PORT = 8000;

    public static void main(String[] args) {

        System.out.println("서버가 시작되었습니다...");

        try { // 서버 소켓 생성
            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket clientSocket = serverSocket.accept();
            System.out.println("클라이언트가 연결되었습니다: " + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("클라이언트로부터 받은 메시지: " + message);

                // 클라이언트에게 메시지 응답
                out.println("서버가 받은 메시지: " + message);
            }

            // 클라이언트 연결 종료
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
