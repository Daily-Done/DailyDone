package com.example.Dailydone.Mapper;

import com.example.Dailydone.DTO.PaymentDTO;
import com.example.Dailydone.Entity.Errand;
import com.example.Dailydone.Entity.Payment;

public class PaymentMapper {
    public static PaymentDTO toDTO(Payment payment) {
        if (payment == null) return null;

        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setTransactionId(payment.getTransactionId());
        dto.setPaidAt(payment.getPaidAt());

        if (payment.getErrand() != null) {
            dto.setErrandId(payment.getErrand().getId());
        }

        return dto;
    }

    // Convert DTO → Entity
    public static Payment toEntity(PaymentDTO dto, Errand errand) {
        if (dto == null) return null;

        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setTransactionId(dto.getTransactionId());
        payment.setPaidAt(dto.getPaidAt());
        payment.setErrand(errand); // You must fetch Errand before calling this

        return payment;
    }

}
