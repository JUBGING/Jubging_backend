package Capstone_team1.Jubging.service;

import Capstone_team1.Jubging.dto.products.ProductsResponseInfo;
import Capstone_team1.Jubging.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductsRepository productsRepository;

    @Transactional(readOnly = true)
    public List<ProductsResponseInfo> getProductsInfo()
    {
        return this.productsRepository.getProductsInfo();
    }
}
