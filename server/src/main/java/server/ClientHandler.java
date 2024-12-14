package server;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private int clientNumber;
    private GameState gameState;

    public ClientHandler(Socket clientSocket, int clientNumber, GameState gameState) {

        this.gameState = gameState;
        gameState.setClientHandler(this, clientNumber);

        initSetting(clientSocket,clientNumber);
        sendClientNumber(clientNumber);
    }


    // 초기 설정
    public void initSetting(Socket clientSocket, int clientNumber) {

        this.clientSocket = clientSocket;
        this.clientNumber = clientNumber;

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error setting up streams: " + e.getMessage());
        }
    }


    @Override
    public void run() {

        try {
            String request;
            while ((request = in.readLine()) != null) {

                String response = handleRequest(request);
                if (response != null)
                    out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } finally {
            cleanUp();
        }
    }


    // 클라이언트 연결 정리
    private void cleanUp() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error during cleanup: " + e.getMessage());
        }
    }


    // 클라이언트 요청을 처리하는 메서드
    public String handleRequest(String request) {
        try {
            JSONObject jsonRequest = new JSONObject(request);
            String command = jsonRequest.getString("command");
            System.out.println("Client" + clientNumber + ": " + jsonRequest.get("command"));

            switch (command) {
                case "CONNECT_FINISH":
                    if (clientNumber == 1) {
                        gameState.setClient1Ready(true);
                        System.out.println("client1 connect finish");
                    } else {
                        gameState.setClient2Ready(true);
                        System.out.println("client2 connect finish");
                    }
                    gameState.checkAndNotifyClients("CONNECT_FINISH");
                    return null;
                case "CHARACTER_SELECT_FINISH":
                    String character = jsonRequest.getString("character");
                    if (clientNumber == 1) {
                        gameState.setClient1Character(character);
                    } else {
                        gameState.setClient2Character(character);
                    }
                    return null;
                case "CARD_SELECT_FINISH":
                    String cardsArray = jsonRequest.getJSONArray("cardList").toString();
                    if (clientNumber == 1) {
                        gameState.setClient1CardList(cardsArray);
                    } else {
                        gameState.setClient2CardList(cardsArray);
                    }
                    return null;
                default:
                    System.out.println("Unknown command");
                    return null;
            }
        } catch (Exception e) {
            return "Invalid request format";
        }
    }

    public void sendJson(String json) {
        out.println(json);
    }

    public void sendClientNumber(int clientNumber) {
        System.out.println("Send Client Number " + clientNumber);
        out.println(clientNumber);
    }
}
