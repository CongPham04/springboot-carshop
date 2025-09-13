package com.carshop.oto_shop.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // chỉ hiển thị khi khác null (tức là khi data = null thì nó sẽ không hien thi data nữa!)
public class ApiResponse<T> {
    private  int code;
    private String message;
    private T data;

    public ApiResponse(){

    }
    public ApiResponse(int code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<T>(200, message, data);
    }
    public static <T> ApiResponse<T> success(String message){
        return new ApiResponse<T>(200, message, null);
    }
    public static <T> ApiResponse<T> error(String message, T data){
        return new ApiResponse<T>(500, message, data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
