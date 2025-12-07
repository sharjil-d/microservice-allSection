package com.microservice.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Locale;

@Data
@Schema(
        name="Customer",
        description = "Schema to hold Customer and acc"
)
public class CustomerDto {

    @Schema(
            description = "Name of customer",example = "sharjil"
    )
    @NotEmpty(message = "name cannot be empty")
    @Size(min = 5,max = 30,message = "length")
    private String name;

    @NotEmpty
    private String email;
    private String mobileNumber;
    private AccountsDto accountsDto;
}
