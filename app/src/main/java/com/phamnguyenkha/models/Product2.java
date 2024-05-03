package com.phamnguyenkha.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Product2 implements Serializable {
    @PropertyName("Idd")
    int idd;
    @PropertyName("ProductName")
    String productName;
    @PropertyName("ProductPrice")
    double productPrice;
    @PropertyName("BestGame")
    int bestGame;
    @PropertyName("Description")
    String description;
    @PropertyName("ImagePath")
    String imagePath;
    @PropertyName("CategoryId")
    int categoryId;
    @PropertyName("Star")
    int star;

    public  Product2(){

    }

    public Product2(int Idd, String ProductName, double ProductPrice, int BestGame, String Description, String ImagePath, int CategoryId, int Star) {
        idd = Idd;
        productName = ProductName;
        productPrice = ProductPrice;
        bestGame = BestGame;
        description = Description;
        imagePath = ImagePath;
        categoryId = CategoryId;
        star = Star;
    }

    public int getId() {
        return idd;
    }

    public void setId(int Idd) {
        idd = Idd;
    }

    public String getProductNamexx() {
        return productName;
    }

    public void setProductNamexx(String ProductName) {
        this.productName = ProductName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double ProductPrice) {
        this.productPrice = ProductPrice;
    }

    public int getBestGame() {
        return bestGame;
    }

    public void setBestGame(int BestGame) {
        this.bestGame = BestGame;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String Description) {
        this.description = Description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String ImagePath) {
        this.imagePath = ImagePath;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int CategoryId) {
        this.categoryId = CategoryId;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int Star) {
        this.star = Star;
    }

//    @Override
//    public String toString() {
//        return "Product{" +
//                "Id=" + idd +
//                ", ProductName='" + productName + '\'' +
//                ", ProductPrice=" + productPrice +
//                ", BestGame=" + bestGame +
//                ", Description='" + description + '\'' +
//                ", ImagePath='" + imagePath + '\'' +
//                ", CategoryId=" + categoryId +
//                ", Star=" + star +
//                '}';
//    }

}
