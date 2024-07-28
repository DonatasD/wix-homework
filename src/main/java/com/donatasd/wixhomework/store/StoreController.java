package com.donatasd.wixhomework.store;

import com.donatasd.wixhomework.query.jpa.QuerySpecification;
import com.donatasd.wixhomework.query.operator.IOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping()
    public List<Store> findAllByQuery(@RequestParam(name = "query", required = false) IOperator operator) {
        Specification<Store> specification = new QuerySpecification<>(operator);
        return this.storeService.findAll(specification);
    }

    // TODO might be worth having a separate object like StoreSaveDTO to avoid using Entity object and having clear save payload definition
    @PostMapping()
    public Store createOrUpdate(@RequestBody Store store) {
        return this.storeService.createOrUpdate(store);
    }
}
