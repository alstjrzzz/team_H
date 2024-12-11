package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import org.json.JSONArray;
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
		
		connectToServer();
	}
	
	
	public static void connectToServer() {
		try {
			// 클라이언트 소켓 생성, 입출력 설정
			clientSocket = new Socket(SERVER_IP, PORT);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    out = new PrintWriter(clientSocket.getOutputStream(), true);
		    
		    // 서버로부터 clientNum 입력 대기
		    gameState.setClientNumber(Integer.parseInt(receiveJson()));
		    System.out.println("My Client Number: " + gameState.getClientNumber());
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
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
		
		JSONObject json = new JSONObject();
		json.put("command", "CHARACTER_SELECT_FINISH");
		json.put("character", gameState.getMyCharacter().getName());
		
		out.println(json);
	}
	
	
	public void sendConnectFinish() {

		JSONObject json = new JSONObject();
		json.put("command", "CONNECT_FINISH");
		
		out.println(json);
	}
	
	
	public static String receiveJson() throws IOException {
		
		return in.readLine();
	}
}
