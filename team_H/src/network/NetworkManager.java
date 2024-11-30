package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import model.GameState;

public class NetworkManager {

	private static final String SERVER_IP = "127.0.0.1"; // 현재는 LocalHost
	private static final int PORT = 8000;
	
	private static Socket clientSocket;
	private static BufferedReader in;
    private static PrintWriter out;
	
	public NetworkManager() {
		
		connectToServer();
		System.out.println(getServerResponse("Hello from Client")); // 테스트
	}
	
	
	public static void connectToServer() {
		try { // 클라이언트 소켓 생성, 입출력 설정
			clientSocket = new Socket(SERVER_IP, PORT);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	// 테스트 메소드
	public String getServerResponse(String message) {
		
		if (out != null) {
            out.println(message);
        }
		
		String response = null;
		if (in != null) {
			try {
				response = in.readLine(); // readLine() 사용 시 서버에서 응답을 받을때까지 대기 상태로 들어감
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return response;
	}
	
	
	
}
