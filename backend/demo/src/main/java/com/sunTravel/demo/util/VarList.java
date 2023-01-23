package com.sunTravel.demo.util;

public enum VarList
{
    RSP_SUCCESS("00"),
    RSP_NO_DATA_FOUND("01"),
    RSP_NOT_AUTHORISED("02"),
    RSP_TOKEN_EXPIRED("03"),
    RSP_TOKEN_INVALID("04"),
    RSP_ERROR("05"),
    RSP_DUPLICATED("06"),
    RSP_BAD_DATA("07"),
    RSP_FAIL("10");
    private String code;
    VarList(String code){
        this.code=code;
    }

    public String getCode()
    {
        return code;
    }
}
