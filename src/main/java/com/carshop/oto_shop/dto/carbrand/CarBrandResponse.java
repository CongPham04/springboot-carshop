package com.carshop.oto_shop.dto.carbrand;

public class CarBrandResponse {
    private Long brandId;

    private String brandName;

    public CarBrandResponse() {}

    public CarBrandResponse(Long carBrandId, String brandName) {
        this.brandId = carBrandId;
        this.brandName = brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long carBrandId) {
        this.brandId = carBrandId;
    }

    public String getBrandName() {
        return brandName;
    }
}
