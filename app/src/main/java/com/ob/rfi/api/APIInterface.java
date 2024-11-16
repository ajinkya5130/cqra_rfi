package com.ob.rfi.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
   /* @GET("/api/unknown")
    Call<String> doGetListResources();

    @POST("/api/users")
    Call<User> createUser(@Body User user);
*/
    @GET("getClientProjectWorkType?")
    Call<String> getClientProjectWorkType(@Query("userID") String userId,@Query("userRole") String userRole);

    /*@FormUrlEncoded
    @POST("/api/users?")
    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);*/

}
