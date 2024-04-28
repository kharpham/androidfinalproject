package com.phamnguyenkha.models;

public class Category {
    int Id;
    String CategoryName;
    int ImagePath;

    public Category(int id, String categoryName, int imagePath) {
        Id = id;
        CategoryName = categoryName;
        ImagePath = imagePath;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getImagePath() {
        return ImagePath;
    }

    public void setImagePath(int imagePath) {
        ImagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Category{" +
                "Id=" + Id +
                ", CategoryName='" + CategoryName + '\'' +
                ", ImagePath=" + ImagePath +
                '}';
    }
}
