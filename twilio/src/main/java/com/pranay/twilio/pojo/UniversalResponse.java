package com.pranay.twilio.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Created by Pranay on 6/16/2017.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UniversalResponse<T> {
    private List list;
    private T object;
    private Integer reqid;
}
