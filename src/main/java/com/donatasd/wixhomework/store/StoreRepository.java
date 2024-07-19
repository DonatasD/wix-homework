package com.donatasd.wixhomework.store;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StoreRepository {

    private EntityManager em;

    public List<Store> findAllByQuery(Object query) {
        throw new UnsupportedOperationException();
    }
}
