package com.anstudios.sellerapp;

import com.anstudios.sellerapp.models.Password;
import com.anstudios.sellerapp.models.Product;
import com.anstudios.sellerapp.models.Seller;
import com.anstudios.sellerapp.models.Transaction;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

public interface ApiInterface {
//    @HTTP(method = "GET", path = "/product/all")
//    Call<List<Product>> getAllProducts();
//
//    @GET("/categories/all")
//    Call<List<Categories>> getAllCategories();
//
//    @HTTP(method = "POST", path = "/buyers/update", hasBody = true)
//    Call<Buyers> postToken(@Body Buyers buyers);
//
//    @GET("/buyers/add")s
//    Call<Buyers> createBuyer(@Body Buyers buyers);

//    @HTTP(method = "POST", path = "/sellers/login", hasBody = true)
//    Call<Seller> getSeller(@Body String email, String password);

    @HTTP(method = "POST", path = "sellers/login", hasBody = true)
    Call<Seller> getSeller(@Body Seller seller);

    @HTTP(method = "POST", path = "sellers/update", hasBody = true)
    Call<Seller> updateSeller(@Body Seller seller);

    @HTTP(method = "POST", path = "sellers/add", hasBody = true)
    Call<Seller> createSeller(@Body Seller seller);

    @HTTP(method = "POST", path = "product/add", hasBody = true)
    Call<Product> addProduct(@Body Product product);

    @HTTP(method = "POST", path = "product/update", hasBody = true)
    Call<Product> updateProduct(@Body Product product);

    @HTTP(method = "POST", path = "sellers/changePassword", hasBody = true)
    Call<Product> updatePassword(@Body Password password);

    @HTTP(method = "POST", path = "transaction/withdrawal/initiate", hasBody = true)
    Call<String> initiateWithdrwalRequest(@Body Transaction password);

}
