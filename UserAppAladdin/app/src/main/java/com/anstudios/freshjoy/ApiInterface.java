package com.anstudios.freshjoy;

import com.anstudios.freshjoy.models.Buyers;
import com.anstudios.freshjoy.models.Categories;
import com.anstudios.freshjoy.models.Order;
import com.anstudios.freshjoy.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;

public interface ApiInterface {
//    @GET("/product/all")
    @HTTP(method = "GET", path = "/product/all")
    Call<List<Product>> getAllProducts();

    @GET("/categories/all")
    Call<List<Categories>> getAllCategories();

    @HTTP(method = "POST", path = "buyers/update", hasBody = true)
    Call<Buyers> postToken(@Body Buyers buyers);

    @HTTP(method = "POST", path = "buyers/update", hasBody = true)
    Call<Buyers> updateBuyer(@Body Buyers buyers);

    @HTTP(method = "POST", path = "buyers/add", hasBody = true)
    Call<Buyers> createBuyer(@Body Buyers buyers);


    @HTTP(method = "POST", path = "order/add", hasBody = true)
    Call<Order> addOrder(@Body Order buyers);

}
