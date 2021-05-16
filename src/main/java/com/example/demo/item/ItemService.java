package com.example.demo.item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@EnableMapRepositories
@Service
public class ItemService {
    private CrudRepository<Item, Long> repository;

    @Autowired
    public ItemService(CrudRepository<Item, Long> repository) {
        this.repository = repository;
        // load the DB with 3 default item on application startup
        this.repository.saveAll(List.of(
                new Item(1L, "Burger", 599L, "Tasty", "https://cdn.auth0.com/blog/whatabyte/burger-sm.png"),
                new Item(2L, "Pizza", 299L, "Cheesy", "https://cdn.auth0.com/blog/whatabyte/pizza-sm.png"),
                new Item(3L, "Tea", 199L, "Informative", "https://cdn.auth0.com/blog/whatabyte/tea-sm.png")
        ));
    }

    // ====================== CREATE ========================
    public Item create(Item item) {
        Item newItem = new Item(
                new Date().getTime(),
                item.getName(),
                item.getPrice(),
                item.getDescription(),
                item.getImage()
        );
        return repository.save(newItem);
    }

    // ==================== READ =========================
    public List<Item> findAll() {
        List<Item> list = new ArrayList<>();
        // get all items from the repository in Iterable form
        Iterable<Item> items = repository.findAll();
        // I have to convert Iterable to List in order to return it.
        items.forEach(i -> list.add(i));
        return list;
    }

    public Optional<Item> find(Long id) {
        return repository.findById(id);
    }


    // ====================== UPDATE =============================
    // The id filed should always remain the same
    public Optional<Item> update(Long id, Item newItem) {
        // only update if we have an old item with specified ID
        return repository.findById(id).map(oldItem -> {
            Item updatedItem = oldItem.updateWith(newItem);
            return repository.save(updatedItem);
        });
    }

    // ==================== DELETE ==========================
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
