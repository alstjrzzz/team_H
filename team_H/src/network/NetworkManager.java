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
	
	private Socket clientSocket;
	private BufferedReader in;
    private PrintWriter out;
	
	public NetworkManager() {
		
		try { // 클라이언트 소켓 생성, 입출력 설정
			clientSocket = new Socket(SERVER_IP, PORT);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	// 예시로 만든 메소드
	public String getServerResponse() {
		
		if (out != null) {
            out.println("엄준식");  // 서버로 데이터 전송 예시
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
