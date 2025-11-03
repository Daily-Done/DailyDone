package com.example.Dailydone.Controller;

import com.example.Dailydone.Entity.Category;
import com.example.Dailydone.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping()
    public ResponseEntity<?> getCategory(){
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(categoryService.getCategories());
   }
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody List<Category> category){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.saveAllCategories(category));
    }
}
