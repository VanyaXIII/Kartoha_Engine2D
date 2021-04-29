package Kartoha_Engine2D.utils;

import com.google.gson.Gson;

import java.io.*;

public class JsonReader {

    private final String path;

    public JsonReader(String path) {
        this.path = path;
    }

    public Object read(Class type) throws IOException {
        InputStream gsonStream = new FileInputStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gsonStream));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        String jsonString = sb.toString();

        return new Gson().fromJson(jsonString, type);
    }

}
