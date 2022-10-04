package Capstone_team1.Jubging.controller;

import Capstone_team1.Jubging.dto.jubjubi.JubjubiResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import Capstone_team1.Jubging.service.JubjubiService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/jubjubi")
public class JubjubiController {

    private final JubjubiService jubJubiService;

    @GetMapping("/info/{userPosition}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<JubjubiResponseDto> findByUserPosition(@RequestParam String userPosition){
        return jubJubiService.findByUserPosition(userPosition);
    }
}
