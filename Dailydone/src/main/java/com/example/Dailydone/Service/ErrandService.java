package com.example.Dailydone.Service;

import com.example.Dailydone.DTO.ErrandDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Mapper.ErrandMapper;
import com.example.Dailydone.Repository.ErrandRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ErrandService {
    @Autowired
    private ErrandRepo errandRepo;
    @Autowired
    private ErrandMapper errandMapper;
    public ErrandDTO CreateErrand(ErrandDTO errandDTO){
         Errand errand = errandMapper.toEntity(errandDTO);
         errand = errandRepo.save(errand);
         return errandMapper.toDTO(errand);
    }

    public List<ErrandDTO> showErrand(Long id){
        return errandRepo.findAllById(Collections.singleton(id))
                .stream().map(errand -> errandMapper.toDTO(errand)).toList();
    }
}
