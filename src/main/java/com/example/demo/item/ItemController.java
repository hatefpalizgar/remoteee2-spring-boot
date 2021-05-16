package com.example.demo.item;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/menu/items")
@CrossOrigin(origins = "https://dashboard.whatabyte.app")
public class ItemController {
    private final ItemService service;

    @Autowired  // optional. but I prefer to put it to denote the dependency injection(DI)
    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Item>> findAll() {
        List<Item> items = service.findAll();
        return ResponseEntity.ok().body(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> find(@PathVariable("id") Long id) {
        Optional<Item> item = service.find(id);
        return ResponseEntity.of(item);
    }

    @PostMapping
    public ResponseEntity<Item> create(@Valid @RequestBody Item item) {
        Item created = service.create(item);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> update(
            @PathVariable("id") Long id,
           @Valid @RequestBody Item updatedItem) {
        Optional<Item> updated = service.update(id, updatedItem);
        // if the old item ID is not found, create a new item and store in DB
        // if the old item ID is found, update the old item with the new one
        return updated
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> {
                    // below is exactly similar to ItemController.create() method
                    Item created = service.create(updatedItem);
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(created.getId())
                            .toUri();
                    return ResponseEntity.created(location).body(created);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Item> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    // This method returns a proper and informative response back to the user once any filed provided are invalid
    // and not following the validation rule we have specified above
    // This method will handle any exception of type MethodArgumentNotValidException as soon as it is thrown during runtime
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            // returns the field on which validation is violated
            String key = ((FieldError)error).getField();
            // returns the validation error message
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return ResponseEntity.badRequest().body(map);
    }
}
