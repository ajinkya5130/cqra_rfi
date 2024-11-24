package com.ob.rfi.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIInterface {
   /* @GET("/api/unknown")
    Call<String> doGetListResources();

    @POST("/api/users")
    Call<User> createUser(@Body User user);
*/
    @Headers({"Content-Type: application/xml; charset=utf-8"})
    @GET("getClientProjectWorkType?")
    Call<ResponseBody> getClientProjectWorkType(@Query("userID") String userId, @Query("userRole") String userRole);

    /*@FormUrlEncoded
    @POST("/api/users?")
    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);*/

}
