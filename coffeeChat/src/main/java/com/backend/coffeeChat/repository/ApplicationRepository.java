package com.backend.coffeeChat.repository;

import com.backend.coffeeChat.dto.ApplyRequest;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ApplicationRepository {
    private final Map<Long, ApplyRequest> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ApplyRequest save(ApplyRequest request) {
        long id = idGenerator.getAndIncrement();
        request.setId(id);
        store.put(id, request);
        return request;
    }

    public ApplyRequest findById(Long id) {
        return store.get(id);
    }
}
