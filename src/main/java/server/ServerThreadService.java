/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import caro.Client;
import caro.player.Player;
import service.PlayerService;
import service.DB;

public class ServerThreadService {
    private List<ServerThread> listServerThreads;
    private List<ServerThread> listClientWaitPlay;

    public List<ServerThread> getListServerThreads() {
        return listServerThreads;
    }

    public ServerThreadService() {
        listServerThreads = new ArrayList<>();
    }

    public void add(ServerThread serverThread) {
        listServerThreads.add(serverThread);
    }

    public void mutilCastSend(String message) { // like sockets.emit in socket.io
        for (ServerThread serverThread : Server.serverThreadService.getListServerThreads()) {
            try {
                serverThread.write(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void boardCast(int id, String message) {
        for (ServerThread serverThread : Server.serverThreadService.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                continue;
            } else {
                try {
                    serverThread.write(message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public int getLength() {
        return listServerThreads.size();
    }

    public void sendOnlineList() {
        String res = "";
        List<ServerThread> threadbus = Server.serverThreadService.getListServerThreads();
        for (Player item : Server.listClientOnline) {
            res += "update-online-list," + item.getId() + "," + item + ";";
        }
        Server.serverThreadService.mutilCastSend(res);
    }

    public void sendRankingList() {
        String res = "";
        int stt = 1;
        List<ServerThread> threadbus = Server.serverThreadService.getListServerThreads();
        for (Player item : Server.listRanking) {
            res += "ranking-list," + stt + "," + item + ";";
            stt++;
        }
        Server.serverThreadService.mutilCastSend(res);
    }

    public void sendMessageToPerson(int id, String headerStatus, String message) {
        for (ServerThread serverThread : Server.serverThreadService.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                try {
                    serverThread.write(headerStatus + "," + message);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void sendMessageToPerson(int id, String message) {
        for (ServerThread serverThread : Server.serverThreadService.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                try {
                    serverThread.write(message);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void sendMoveToPerson(int id, String header, String message) {
        for (ServerThread serverThread : Server.serverThreadService.getListServerThreads()) {
            if (serverThread.getClientNumber() == id) {
                try {
                    serverThread.write(header + "," + message);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void remove(int id) {
        for (int i = 0; i < Server.serverThreadService.getLength(); i++) {
            if (Server.serverThreadService.getListServerThreads().get(i).getClientNumber() == id) {
                Server.serverThreadService.listServerThreads.remove(i);
            }
        }
    }

    public List<ServerThread> getListClientWaitPlay() {
        return listClientWaitPlay;
    }

    public void setListClientWaitPlay(List<ServerThread> listClientWaitPlay) {
        this.listClientWaitPlay = listClientWaitPlay;
    }

    public void pushListClientWaitPlay(ServerThread listClientWaitPlay) {
        this.listClientWaitPlay.add(listClientWaitPlay);
    }

    public void deleteListClientWaitPlay(ServerThread clientCancelPlay) {
        this.listClientWaitPlay.stream()
                .filter(client -> client.getClientNumber() != clientCancelPlay.getClientNumber());
    }

    public void mutilCastSendOnlineList(String header, Player user, int clientNumber) {
        // TODO Auto-generated method stub
        for (ServerThread serverThread : Server.serverThreadService.getListServerThreads()) {
            try {
                if (serverThread.getClientNumber() == clientNumber) {
                    continue;
                }
                serverThread.write(header + clientNumber + "," + user.getEmail() + "," + user.getUsername() + ","
                        + user.getNumberOfGame() + "," + user.getNumberOfWin() + "," + user.getNumberOfDraw() + ","
                        + user.getTotalScore());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}