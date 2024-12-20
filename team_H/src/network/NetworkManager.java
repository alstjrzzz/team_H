// NetworkManager.java

package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import controller.GameController;
import model.Card;
import model.GameState;

public class NetworkManager {

	//private static final String SERVER_IP = "172.17.41.35"; // MNU_Guest
	private static final String SERVER_IP = "localhost";
	private static final int PORT = 8000;
	
	private static GameState gameState;
	private GameController gameController;
	
	private static Socket clientSocket;
	private static BufferedReader in;
    private static PrintWriter out;
	
	public NetworkManager(GameState gameState, GameController gameController) {
		
		this.gameState = gameState;
		this.gameController = gameController;
		
	}
	
	
	 // 서버에 연결
    public void connectToServer() throws IOException {
        if (clientSocket != null && !clientSocket.isClosed()) {
            System.out.println("이미 서버에 연결되어 있습니다.");
            return;
        }

        try {
            clientSocket = new Socket(SERVER_IP, PORT);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // 서버로부터 클라이언트 번호 수신
            gameState.setClientNumber(Integer.parseInt(receiveJson()));
            System.out.println("My Client Number: " + gameState.getClientNumber());
        } catch (IOException e) {
            throw new IOException("서버에 연결할 수 없습니다: " + e.getMessage());
        }
    }

	
	
	
	public void sendCardSelection() {
		
		JSONObject json = new JSONObject();
		json.put("command", "CARD_SELECT_FINISH");
		
		JSONArray cardsArray = new JSONArray();
		LinkedList<Card> cardList = gameState.getSelectedCardList();
        for (Card card : cardList) {
            cardsArray.put(card.toJSON());
        }
        json.put("cardList", cardsArray);
		
		out.println(json.toString());
	}
	
	
	public void sendCharacterSelection() {
	    if (clientSocket == null || clientSocket.isClosed()) {
	        System.out.println("서버와 연결되지 않았습니다.");
	        return;
	    }

	    JSONObject json = new JSONObject();
	    json.put("command", "CHARACTER_SELECT_FINISH");
	    json.put("character", gameState.getMyCharacter().getName());
	    out.println(json.toString());
	    System.out.println("캐릭터 데이터 서버로 전송: " + json.toString());
	}
	

	// 메시지 송신
    public void sendConnectFinish() {
        if (clientSocket == null || clientSocket.isClosed()) {
            System.out.println("서버에 연결되지 않았습니다. 연결 후 시도하세요.");
            return;
        }

        JSONObject json = new JSONObject();
        json.put("command", "CONNECT_FINISH");
        out.println(json.toString());
    }
    
    
    // 타이머 대용 ㅋㅋ
    public void sendStopPlease(int ms) {
    	if (clientSocket == null || clientSocket.isClosed()) {
	        System.out.println("서버와 연결되지 않았습니다.");
	        return;
	    }
    	JSONObject json = new JSONObject();
	    json.put("command", "WAIT");
	    json.put("millisecond", ms);
	    out.println(json.toString());
    }
	
	
    // 메시지 수신
    public String receiveJson() throws IOException {
        if (clientSocket == null || clientSocket.isClosed()) {
            throw new IOException("서버에 연결되지 않았습니다.");
        }

        return in.readLine();
    }
}
