package command;

import com.google.gson.Gson;

/**
 * Created by tjense25 on 1/18/18.
 */

public class ResultTransferObject {
    private static final Gson gson = new Gson();

    private String typeName;
    private String jsonString;

    public ResultTransferObject(String typeName, Object object) {
        this.typeName = typeName;
        this.jsonString = gson.toJson(object);
    }

    public Object getResult() {
        Class<?> klass = null;
        try {
            klass = Class.forName(this.typeName);
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: Could not find class" + this.typeName);
        }

        Object result = gson.fromJson(this.jsonString, klass);

        return result;
    }
}
