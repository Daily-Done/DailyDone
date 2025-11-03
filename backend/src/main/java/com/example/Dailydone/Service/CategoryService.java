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
            // check null or empty name
            if (category.getName() == null || category.getName().trim().isEmpty()) continue;

            // Check only by name — ID will be auto-generated
            boolean exists = categoryRepo.findByName(category.getName().trim()).isPresent();

            if (!exists) {
                Category newCategory = new Category();
                newCategory.setName(category.getName().trim());
                categoryRepo.save(newCategory);
            }
        }
        return "✅ Successfully stored categories";
    }
}
