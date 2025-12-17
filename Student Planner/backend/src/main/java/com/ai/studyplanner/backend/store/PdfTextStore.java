package com.ai.studyplanner.backend.store;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PdfTextStore {

    private final Map<String, String> store = new ConcurrentHashMap<>();

    public String save(String text) {
        String id = UUID.randomUUID().toString();
        store.put(id, text);
        return id;
    }

    public String get(String id) {
        return store.get(id);
    }
}
