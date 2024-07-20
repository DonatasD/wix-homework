package com.donatasd.wixhomework.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> findAll(Specification<Store> specification) {
        return this.storeRepository.findAll(specification);
    }

    public Store createOrUpdate(Store store) {
        return this.storeRepository.save(store);
    }
}
