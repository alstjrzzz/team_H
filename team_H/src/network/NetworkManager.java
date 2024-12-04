package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;

import controller.GameController;
import model.GameState;

public class NetworkManager {

	private static final String SERVER_IP = "127.0.0.1"; // 현재는 LocalHost
	private static final int PORT = 8000;
	private GameState gameState;
	private GameController gameController;
	
	private static Socket clientSocket;
	private static BufferedReader in;
    private static PrintWriter out;
	
	public NetworkManager(GameState gameState, GameController gameController) {
		
		this.gameState = gameState;
		this.gameController = gameController;
		
		connectToServer();
		connectTest("Hello Server"); // 테스트
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
	public void connectTest(String message) {
		
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
		System.out.println(response);
	}
	
	
	public void sendCharacterSelection() {
		
		JSONObject json = new JSONObject();
		json.put("command", "CHARACTER_SELECT_FINISH");
		json.put("character", gameState.getMyCharacter());
		
		out.println(json);
	}
	
	
	public String receiveJson() throws IOException {
		
		return in.readLine();
	}
}
