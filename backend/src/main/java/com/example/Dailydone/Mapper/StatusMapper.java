package com.example.Dailydone.Mapper;

import com.example.Dailydone.DTO.StatusHistoryDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.StatusHistory;

public class StatusMapper {
    public static StatusHistoryDTO toDTO(StatusHistory statusHistory) {
        if (statusHistory == null) return null;

        StatusHistoryDTO dto = new StatusHistoryDTO();
        dto.setId(statusHistory.getId());
        dto.setStatus(statusHistory.getStatus());
        dto.setChangedAt(statusHistory.getChangedAt());

        if (statusHistory.getErrand() != null) {
            dto.setErrandId(statusHistory.getErrand().getId());
        }

        return dto;
    }

    // DTO → Entity
    public static StatusHistory toEntity(StatusHistoryDTO dto, Errand errand) {
        if (dto == null) return null;

        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setId(dto.getId());
        statusHistory.setStatus(dto.getStatus());
        statusHistory.setChangedAt(dto.getChangedAt());
        statusHistory.setErrand(errand);

        return statusHistory;
    }

}
