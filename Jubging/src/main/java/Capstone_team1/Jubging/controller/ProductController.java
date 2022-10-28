package Capstone_team1.Jubging.controller;


import Capstone_team1.Jubging.dto.products.ProductsResponseInfo;
import Capstone_team1.Jubging.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductsService productsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ProductsResponseInfo> getProductsInfo(){
        return this.productsService.getProductsInfo();
    }
}
