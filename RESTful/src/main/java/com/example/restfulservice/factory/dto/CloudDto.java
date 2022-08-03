package com.example.restfulservice.factory.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CloudDto {
    private String fileName;
    private String fileUrl;
    private Date updateDate = new Date();

}
