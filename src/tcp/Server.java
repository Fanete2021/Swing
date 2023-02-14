package src.tcp;

import src.entity.transport.Transport;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static Map<Integer, ClientHandler> clients;
    private static List<Integer> ids;
    private static int id;

    public static void main(String[] args) throws IOException {
        clients = new HashMap<>();
        ids = new ArrayList<>();
        id = 1;
        int port  = 9090;
        ServerSocket server = new ServerSocket(port);

        try {
            while (true) {
                Socket client = server.accept();
                ClientHandler clientThread = new ClientHandler(client, id);

                ids.add(id);
                clients.put(id, clientThread);
                id++;
                clientThread.start();
                sendClients(ids.size() - 1);
            }
        }
        finally {
            server.close();
        }
    }

    public static void sendClients(int count) {
        for (int i = 0; i < count; ++i) {
            clients.get(ids.get(i)).sendClientIds();
        }
    }

    private static class ClientHandler extends Thread {
        public Socket client;
        public int id;
        public String command;
        public ObjectOutputStream oos;
        public ObjectInputStream ois;
        public boolean isConnected = true;

        public ClientHandler(Socket client, int id) {
            this.client = client;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                oos = new ObjectOutputStream(client.getOutputStream());
                ois = new ObjectInputStream(client.getInputStream());

                oos.writeObject(ids);
                oos.writeInt(id);
                oos.flush();

                while (isConnected) {
                    command = ois.readUTF();
                    switch (command) {
                        case "connect":
                            int idToSwap = ois.readInt();
                            ArrayList<Transport> incomingList = (ArrayList) ois.readObject();

                            ClientHandler otherClient = clients.get(idToSwap);
                            otherClient.oos.writeUTF("swap");
                            otherClient.oos.writeObject(incomingList);
                            otherClient.oos.writeInt(id);
                            otherClient.oos.flush();
                            break;
                        case "swap":
                            List<Transport> transports = (ArrayList) ois.readObject();
                            int id = ois.readInt();
                            ClientHandler client = clients.get(id);
                            client.oos.writeUTF("get");
                            client.oos.writeObject(transports);
                            client.oos.flush();
                            break;
                        case "disconnect":
                            disconnect();
                            break;
                        default:
                            break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }


        public void sendClientIds() {
            try {
                oos.writeUTF("otherClients");
                ArrayList<Integer> arr = new ArrayList<>(ids);
                oos.writeObject(arr);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void disconnect() {
            try {
                isConnected = false;
                ois.close();
                oos.close();
                client.close();
                clients.remove(id);
                for (int i = 0; i < ids.size(); ++i) {
                    if (ids.get(i) == id) {
                        ids.remove(i);
                        break;
                    }
                }
                sendClients(ids.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
