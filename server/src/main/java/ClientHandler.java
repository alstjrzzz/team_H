import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    private Socket clientSocket;
    private static BufferedReader in;
    private static PrintWriter out;
    private ArrayList<ClientHandler> clients;

    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) {

        connectServer(clientSocket,clients);
    }


    // 서버와 연결
    public void connectServer(Socket clientSocket, ArrayList<ClientHandler> clients) {

        this.clientSocket = clientSocket;
        this.clients = clients;

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

            testConnect(); // 테스트

            // 클라이언트와 통신 처리
            String request;
            while ((request = in.readLine()) != null) {

                String response = handleRequest(request);
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
            clients.remove(this); // 클라이언트 목록에서 제거
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error during cleanup: " + e.getMessage());
        }
    }


    // 클라이언트 요청을 처리하는 메서드
    private String handleRequest(String request) {
        try {
            JSONObject jsonRequest = new JSONObject(request);
            String command = jsonRequest.getString("command");

            switch (command) {
                case "GET_GAME_STATE":
                    //return getGameStateAsJson();
                case "PLAY_CARD":
                    String card = jsonRequest.getString("card");
                    //return playCard(card);
                default:
                    return "Unknown command";
            }
        } catch (Exception e) {
            return "Invalid request format";
        }
    }

    public void testConnect() {

        String request = null;
        try {
            request = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\"" + request + "\"" + " from Client!");

        out.println("\"Hello Client\" from Server!");
    }
}
