package loans.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        description = "error response loan dto"
)
public class ErrorResponseDto {

    @Schema(
            description = "path of the URL"
    )
    private String apiPath;

    private HttpStatus status;

    private String errorMessage;
    private LocalDateTime errorTime;
}
