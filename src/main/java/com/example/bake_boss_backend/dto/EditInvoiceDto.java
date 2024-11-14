package com.example.bake_boss_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditInvoiceDto {
    private String invoiceNo;
    private String customer;
}
