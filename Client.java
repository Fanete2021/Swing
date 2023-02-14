import src.config.Configuration;
import src.config.Keys;
import src.core.Emitter.ActionControl;
import src.core.Emitter.Actions;
import src.core.Emitter.Emitter;
import src.core.Emitter.Events;
import src.core.Habitat;
import src.entity.transport.Bike;
import src.entity.transport.Car;
import src.entity.transport.Transport;
import src.utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable {

    public static void main(String[] args) {
        new Client();
    }

    private Socket server = null;
    private int id;
    private int idToSwap;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String command;
    private boolean isConnected = true;
    private Emitter emitter;

    public Client() {
        emitter = new Emitter();
        emitter.subscribe(Events.CONTROL, this::triggerAction);

        Configuration config = new Configuration();
        initSetup(config);

        new Thread(this).start();
        Habitat.createInstance(1200, 700, 50, config, emitter);
    }

    public void initSetup(Configuration config) {
        Car.setImage("src/resources/car.png");
        Bike.setImage("src/resources/bike.png");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                config.save();
                command = "disconnect";
            }
        });

        Car.frequency = Float.parseFloat(config.getProperty(Keys.CHANCE_GENERATION_CAR));
        Car.generationTime = Float.parseFloat(config.getProperty(Keys.GENERATION_TIME_CAR));
        Car.isMoving = Boolean.parseBoolean(config.getProperty(Keys.IS_MOVING_CAR));

        Bike.frequency = Float.parseFloat(config.getProperty(Keys.CHANCE_GENERATION_BIKE));
        Bike.generationTime = Float.parseFloat(config.getProperty(Keys.GENERATION_TIME_BIKE));
        Bike.isMoving = Boolean.parseBoolean(config.getProperty(Keys.IS_MOVING_BIKE));
    }

    private void triggerAction(ActionControl actionControl) {
        switch (actionControl.action) {
            case SELECTED_ID:
                idToSwap = (int)actionControl.state;
                command = "swap";
                break;
            default: break;
        }
    }

    public void disconnect() {
        command = "disconnect";
    }

    public synchronized void connectToServer() {
        int port = 9090;
        try {
            server = new Socket("127.0.0.1", port);
            isConnected = true;

            oos = new ObjectOutputStream(server.getOutputStream());
            ois = new ObjectInputStream(server.getInputStream());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        connectToServer();

        try {
            Thread.sleep(3000);
            ArrayList clientIds = (ArrayList) ois.readObject();
            id = ois.readInt();
            Utils.deleteValueInList(clientIds, id);
            String idsStr = Utils.join(clientIds);
            emitter.notify(Events.CLIENT, Actions.UPDATE_IDS, idsStr);

            while(isConnected) {
                if (ois.available() == 0) {
                    if (command != null) {
                        switch (command) {
                            case "swap":
                                oos.writeUTF("connect");
                                oos.writeInt(idToSwap);
                                oos.writeObject(Habitat.getInstance().cloneTransports());
                                oos.flush();
                                break;
                            case "disconnect":
                                oos.writeUTF("disconnect");
                                oos.flush();
                                break;
                            default:
                                break;
                        }

                        command = null;
                    }
                } else {
                    String commandServer = ois.readUTF();
                    switch (commandServer) {
                        case "swap":
                            ArrayList<Transport> incomingList = (ArrayList) ois.readObject();
                            int idFrom = ois.readInt();
                            ArrayList<Transport> list = Habitat.getInstance().cloneTransports();
                            oos.writeUTF("swap");
                            oos.writeObject(list);
                            oos.writeInt(idFrom);
                            oos.flush();

                            Habitat.getInstance().loadTransports(incomingList);
                            break;
                        case "get":
                            ArrayList<Transport> transports = (ArrayList) ois.readObject();
                            Habitat.getInstance().loadTransports(transports);
                            break;
                        case "otherClients":
                            clientIds = (ArrayList) ois.readObject();
                            Utils.deleteValueInList(clientIds, id);
                            idsStr = Utils.join(clientIds);
                            emitter.notify(Events.CLIENT, Actions.UPDATE_IDS, idsStr);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
                oos.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
