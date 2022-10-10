package Capstone_team1.Jubging.controller;

import Capstone_team1.Jubging.dto.jubjubi.JubjubiResponseDto;
import Capstone_team1.Jubging.dto.jubjubi.StartJubgingRequestDto;
import Capstone_team1.Jubging.dto.jubjubi.StartJubgingResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import Capstone_team1.Jubging.service.JubjubiService;
import java.util.List;

import static Capstone_team1.Jubging.config.utils.SecurityUtil.getCurrentUserEmail;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/jubjubi")
public class JubjubiController {

    private final JubjubiService jubJubiService;

    @GetMapping("/info/{userPosition}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<JubjubiResponseDto> findByUserPosition(@PathVariable String userPosition){
        return jubJubiService.findByUserPosition(userPosition);
    }

    @PostMapping("/jubging-data")
    @ResponseStatus(value = HttpStatus.OK)
    public StartJubgingResponseDto startJubging(@RequestBody StartJubgingRequestDto startJubgingRequestDto){
        return jubJubiService.startJubging(startJubgingRequestDto);
    }

}
