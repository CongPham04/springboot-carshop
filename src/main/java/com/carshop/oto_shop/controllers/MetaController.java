package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.enums.Brand;
import com.carshop.oto_shop.enums.Category;
import com.carshop.oto_shop.enums.Color;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meta")
public class MetaController {

    @GetMapping("/brands")
    public java.util.List<String> getBrands() {
        return Arrays.stream(Brand.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @GetMapping("/categories")
    public java.util.List<String> getCategories() {
        return Arrays.stream(Category.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @GetMapping("/colors")
    public java.util.List<String> getColors() {
        return Arrays.stream(Color.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
