package com.donatasd.wixhomework.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/store")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    public List<Store> findAllByQuery(Object query) {
        return this.storeService.findAllByQuery(query);
    }

    public Store createOrUpdate(Object store) {
        return this.storeService.createOrUpdate(store);
    }
}
