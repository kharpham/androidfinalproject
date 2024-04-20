package com.phamnguyenkha.models;

import java.io.Serializable;

public class Product implements Serializable {
    int Id;
    String ProductName;
    double ProductPrice;
    int BestGame;
    String Description;
    int ImagePath;
    int CategoryId;
    int Star;

    public Product(int id, String productName, double productPrice, int bestGame, String description, int imagePath, int categoryId, int star) {
        Id = id;
        ProductName = productName;
        ProductPrice = productPrice;
        BestGame = bestGame;
        Description = description;
        ImagePath = imagePath;
        CategoryId = categoryId;
        Star = star;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }

    public int getBestGame() {
        return BestGame;
    }

    public void setBestGame(int bestGame) {
        BestGame = bestGame;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getImagePath() {
        return ImagePath;
    }

    public void setImagePath(int imagePath) {
        ImagePath = imagePath;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getStar() {
        return Star;
    }

    public void setStar(int star) {
        Star = star;
    }

    @Override
    public String toString() {
        return "Product{" +
                "Id=" + Id +
                ", ProductName='" + ProductName + '\'' +
                ", ProductPrice=" + ProductPrice +
                ", BestGame=" + BestGame +
                ", Description='" + Description + '\'' +
                ", ImagePath=" + ImagePath +
                ", CategoryId=" + CategoryId +
                ", Star=" + Star +
                '}';
    }
}
