package Capstone_team1.Jubging.dto.jubjubi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendImageRequestDto {
    private MultipartFile image;
}
