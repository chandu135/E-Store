package com.company.e_store.dto;

public class CategoryRequestDTO {
    private String name;

    public CategoryRequestDTO() {}

    public CategoryRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
