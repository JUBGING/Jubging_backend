package Capstone_team1.Jubging.controller;

import Capstone_team1.Jubging.dto.jubjubi.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import Capstone_team1.Jubging.service.JubjubiService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @PatchMapping("/jubging-data/tongs")
    @ResponseStatus(value = HttpStatus.OK)
    public EndJubgingResponseDto endJubging(@RequestBody EndJubgingRequestDto endJubgingRequestDto){
        return jubJubiService.endJubging(endJubgingRequestDto);
    }

    @PostMapping("/jubging-data/img")
    @ResponseStatus(value = HttpStatus.OK)
    public SendImageResponseDto sendImage(@ModelAttribute("image") MultipartFile image,
                                          @ModelAttribute("weight") String weight){
        return jubJubiService.sendImage(image, weight);
    }
}
