package com.example.demo.item;

import org.springframework.data.repository.CrudRepository;

// first generic is the type of elements in the repository (db)
// second generic is the data type of the primary key of each element
// all repository related methods for doing CRUD operation is provided to me out-of-the-box
public interface InMemoryItemRepository extends CrudRepository<Item,Long> {
}
