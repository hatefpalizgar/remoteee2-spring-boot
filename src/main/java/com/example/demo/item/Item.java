package com.example.demo.item;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;

@Data // creates implicitly the getters + setters during compilation
public class Item {
    @Id
    private final Long id;

    @NotNull(message = "name is required")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "name must be a string")
    private String name;

    @NotNull(message = "price is required")
    @Positive(message = "price must be positive")
    private Long price;

    @NotNull(message = "description is required")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "description must be a string")
    private String description;

    @NotNull(message = "image is required")
    @URL(message = "image must be a URL")
    private String image;

    public Item(Long id, String name, Long price, String description, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    // utility method that helps with update operation on DB
    // This method helps to preserve the id of the item while updating it.
    // It favours immutability and making the code safer without changing internals of the object.
    public Item updateWith(Item item) {
        return new Item(
                this.id,
                item.name,
                item.price,
                item.description,
                item.image);
    }

}
