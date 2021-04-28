package Kartoha_Engine2D.utils;

import com.google.gson.Gson;

public interface JsonAble {
    default String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
