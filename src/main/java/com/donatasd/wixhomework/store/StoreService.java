package com.donatasd.wixhomework.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> findAllByQuery(Object query) {
        return this.storeRepository.findAllByQuery(query);
    }

    public Store createOrUpdate(Object store) {
        throw new UnsupportedOperationException();
    }
}
