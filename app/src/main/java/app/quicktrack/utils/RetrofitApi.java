package app.quicktrack.utils;

import app.quicktrack.models.ContactUs;
import app.quicktrack.models.LoginData;
import app.quicktrack.models.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

/**
 * Created by rakhi on 11/24/2017.
 */

public interface RetrofitApi {
    @Multipart
    @FormUrlEncoded
    @POST("login")
    Call<ApiResponse> login(@Body String userModel);

    @FormUrlEncoded
    @POST("webservice/update_profile")
    Call<ApiResponse> updateProfile(@Field("company_name") String company_name,
                             @Field("full_name") String full_name,
                                  @Field("email") String email,
                                  @Field("phone") String phone,
                                  @Field("user_id") String user_id,
                                  @Field("profile_pic") String profile_pic);

    @FormUrlEncoded
    @POST("webservice/contact_us")
    Call<ApiResponse> getContactUs(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("webservice/change_pass")
    Call<ApiResponse> change_pass(
                                  @Field("oldpass") String oldpass,
                                  @Field("newpass") String newpass,
                                  @Field("userid") String userid);

    @FormUrlEncoded
    @POST("webservice/forgot_pass")
    Call<ApiResponse> forgot_pass(@Field("email") String email);

    @FormUrlEncoded
    @POST("webservice/profile")
    Call<ApiResponse> getProfile(@Body() String user_id);
}
