package com.example.bake_boss_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistProductDto {
   
  private LocalDate date;
  private LocalTime time;
  private String customer;
  private String category;
  private String productName;
  private String invoiceNo;
  private Double dpRate;
  private Double costPrice;
  private Double productQty;
  private Double saleRate;
}
