package core;

import java.io.*;

public class PersistentUtils {
    private static PersistentUtils utils;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private PersistentUtils(String pathname){
        try {
            ois = new ObjectInputStream(new FileInputStream(new File(pathname)));
            oos = new ObjectOutputStream(new FileOutputStream(new File(pathname)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized PersistentUtils getInstance(String pathname){
        if(utils == null){
            utils = new PersistentUtils(pathname);
        }
        return utils;
    }

    public void writeObject(Object obj){
        try {
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readObject(){
        Object obj = null;
        try {
            obj = ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return obj;
    }

}
