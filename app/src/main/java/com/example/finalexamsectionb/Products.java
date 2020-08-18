package com.example.finalexamsectionb;

public class Products {
    private String id;
    private String productType;
    private String productPrice;
    private String productquantitiy;
    private String discount;
    private String image;
    private String locationXcordinate;
    private String locationYcordinate;
    public Products()
    {

    }
    public Products(String id,String type,String price,String quantity,String discount,String image,String x,String y)
    {
        this.id=id;
        productType=type;
        productPrice=price;
        productquantitiy=quantity;
        this.discount=discount;
        this.image=image;
        locationXcordinate=x;
        locationYcordinate=y;
    }

}
