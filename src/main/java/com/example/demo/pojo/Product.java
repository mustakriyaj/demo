package com.example.demo.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
@Getter
@Setter
public class Product {

    private Integer id;

    private String productName;

    private String price;

    private String manufacturer;

    private byte[] image;
    private Category category;
}
