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

    public List<Category> getCategories(){
        return categoryRepo.findAll();
    }

    public String saveAllCategories(List<Category> categories) {
        for (Category category : categories) {
            // Check if category already exists by ID or name
            boolean exists = categoryRepo.existsById(category.getId()) ||
                    categoryRepo.findByName(category.getName()).isPresent();

            if (!exists) {
                categoryRepo.save(category);
            }
        }
        return "successfully stored roles";
    }
}
