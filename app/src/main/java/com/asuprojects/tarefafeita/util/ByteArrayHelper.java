package com.asuprojects.tarefafeita.util;

import com.asuprojects.tarefafeita.domain.Tarefa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByteArrayHelper {

    public static byte[] toByteArray(Object obj){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
        byte[] byteArray = null;
        try {
            os = new ObjectOutputStream(out);
            os.writeObject(obj);
            byteArray = out.toByteArray();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return byteArray;
    }

    public static Object toObject(byte[] byteArray){
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        ObjectInputStream is = null;
        Object obj = null;
        try {
            is = new ObjectInputStream(in);
            obj = (Tarefa) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
