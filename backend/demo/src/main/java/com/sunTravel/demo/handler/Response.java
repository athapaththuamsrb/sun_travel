package com.sunTravel.demo.handler;

import com.sunTravel.demo.util.VarList;
import lombok.Data;

@Data
public class Response<T>
{
    private String code;
    private Boolean isSuccess;
    private String description;
    private T data;
    public Response( String code, Boolean isSuccess )
    {
        this.code = code;
        this.isSuccess = isSuccess;
    }

    public Response(String code, Boolean isSuccess, T data )
    {
        this.code = code;
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public Response( String code, Boolean isSuccess, String description )
    {
        this.code = code;
        this.isSuccess = isSuccess;
        this.description = description;
    }
}
