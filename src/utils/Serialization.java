package src.utils;

import java.io.*;
import java.util.ArrayList;

public class Serialization {
    private Serialization() {

    }

    public static <T> void serializationObjects(ArrayList<T> list, String fileName) {
        try (FileOutputStream fos = new FileOutputStream("src/data/" + fileName + ".ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (int i = 0; i < list.size(); ++i) {
                oos.writeObject(list.get(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static <T> ArrayList<T> deserializationObjects(String fileName) {
        ArrayList<T> list = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/data/" + fileName + ".ser"))) {
            while (true) {
                T obj = (T) ois.readObject();
                if (obj == null) {
                    break;
                }
                list.add(obj);
            }
        } catch (EOFException e) {

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }
}
