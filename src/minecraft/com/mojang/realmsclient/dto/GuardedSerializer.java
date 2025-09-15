package com.mojang.realmsclient.dto;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import javax.annotation.Nullable;

public class GuardedSerializer {
    private final Gson gson = new Gson();

    public String toJson(ReflectionBasedSerialization pReflectionBasedSerialization) {
        return this.gson.toJson(pReflectionBasedSerialization);
    }

    public String toJson(JsonElement pJson) {
        return this.gson.toJson(pJson);
    }

    @Nullable
    public <T extends ReflectionBasedSerialization> T fromJson(String pJson, Class<T> pClassOfT) {
        return this.gson.fromJson(pJson, pClassOfT);
    }
}