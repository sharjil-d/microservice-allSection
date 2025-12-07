package com.microservice.demo.controller;

import com.microservice.demo.constants.AccountConstants;
import com.microservice.demo.dto.CustomerDto;
import com.microservice.demo.dto.ResponseDto;
import com.microservice.demo.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping(path="/api",produces={MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD ReST APIS for eaxy bank",
        description = "EAZY BANK"
)
public class AccountsController {


    private IAccountsService iAccountsService;

    @Operation(
            summary = "create account REST API"
    )
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "HTTP STands OK"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "HTTP status internal server error"
                    )
            }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto){
        iAccountsService.createAccount(customerDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(AccountConstants.STATUS_201,AccountConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                               @Pattern(regexp = "(^$|[0-9]{10})",message = "10 digits")
                                                               String mobileNumber){
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateAccount(@RequestBody CustomerDto customerDto){
        iAccountsService.updateAccount(customerDto);
        return ResponseEntity.status(HttpStatus.OK).body(true);

    }
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteAccount(@RequestParam String mobileNumber){
        iAccountsService.deleteAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
