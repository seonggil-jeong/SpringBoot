package com.example.restfulservice.factory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class GCloudDto extends CloudDto {
    private String GCloudPassword;
    private String GCloudPassword2;

}
