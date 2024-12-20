package server;

import org.json.JSONObject;

public class GameState {

    private ClientHandler client1Handler;
    private ClientHandler client2Handler;
    private String client1Character;
    private String client2Character;
    private boolean client1Ready = false;
    private boolean client2Ready = false;
    private String client1CardList;
    private String client2CardList;


    public GameState() {

    }


    public void checkAndNotifyClients(String command) {

        if (client1Ready && client2Ready) {

            switch (command) {

                case "CONNECT_FINISH":
                    JSONObject json = new JSONObject();
                    json.put("command", "NON_NULL");
                    client1Handler.sendJson(json.toString());
                    System.out.println("Send Client1 Check Connect");
                    client2Handler.sendJson(json.toString());
                    System.out.println("Send Client2 Check Connect");

                    break;
                case "SEND_CHARACTER":
                    json = new JSONObject();
                    json.put("command", "SEND_CHARACTER");
                    json.put("character", client2Character);
                    client1Handler.sendJson(json.toString());
                    System.out.println("Send Client2's Character");

                    json = new JSONObject();
                    json.put("command", "SEND_CHARACTER");
                    json.put("character", client1Character);
                    client2Handler.sendJson(json.toString());
                    System.out.println("Send Client1's Character");

                    break;
                case "SEND_CARD":
                    client1Handler.sendJson(client2CardList);
                    System.out.println("Send Client2's Card List");
                    client2Handler.sendJson(client1CardList);
                    System.out.println("Send Client1's Card List");
                    break;
            }

            client1Ready = false;
            client2Ready = false;
        }
    }


    public void setClient1Character(String client1Character) {
        this.client1Character = client1Character;
        client1Ready = true;
        checkAndNotifyClients("SEND_CHARACTER");
    }

    public void setClient2Character(String client2Character) {
        this.client2Character = client2Character;
        client2Ready = true;
        checkAndNotifyClients("SEND_CHARACTER");
    }

    public void setClient1CardList(String client1CardList) {
        this.client1CardList = client1CardList;
        client1Ready = true;
        checkAndNotifyClients("SEND_CARD");
    }

    public void setClient2CardList(String client2CardList) {
        this.client2CardList = client2CardList;
        client2Ready = true;
        checkAndNotifyClients("SEND_CARD");
    }


    public void setClientHandler(ClientHandler clientHandler, int clientNumber) {
        if (clientNumber == 1) client1Handler = clientHandler;
        else client2Handler = clientHandler;
    }


    public boolean isClient1Ready() {
        return client1Ready;
    }

    public void setClient1Ready(boolean client1Ready) {
        this.client1Ready = client1Ready;
    }

    public boolean isClient2Ready() {
        return client2Ready;
    }

    public void setClient2Ready(boolean client2Ready) {
        this.client2Ready = client2Ready;
    }
    
}
