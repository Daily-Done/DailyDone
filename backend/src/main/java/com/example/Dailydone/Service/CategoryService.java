package com.example.Dailydone.Service;

import com.example.Dailydone.Entity.Category;
import com.example.Dailydone.Repository.CategoryRepo;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Cacheable("categories")
    public List<Category> getCategories(){
        return categoryRepo.findAll();
    }
    @CacheEvict(value = "categories", allEntries = true)
    public Category addCategories(Category category){
        Optional<Category> category1 = categoryRepo.findByName(category.getName());
        if(category1.isPresent()){
            throw new RuntimeException("Category already present");
        }
        return categoryRepo.save(category);
    }
}
