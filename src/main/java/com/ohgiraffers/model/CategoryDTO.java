package com.ohgiraffers.model;

public class CategoryDTO {
    private int categoryCode;
    private String categoryName;
    private int refCategoryCode;

    public CategoryDTO(int categoryCode, String categoryName, int refCategoryCode) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.refCategoryCode = refCategoryCode;
    }
    public CategoryDTO() {
    }

    @Override
    public String toString() {
        return categoryCode +
                ", " + categoryName +
                ", " + refCategoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setRefCategoryCode(int refCategoryCode) {
        this.refCategoryCode = refCategoryCode;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getRefCategoryCode() {
        return refCategoryCode;
    }
}
