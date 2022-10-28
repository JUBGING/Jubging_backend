package Capstone_team1.Jubging.repository;


import Capstone_team1.Jubging.domain.Product;
import Capstone_team1.Jubging.dto.products.ProductsResponseInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductsRepository {
    private final JpaProductsRepository jpaProductsRepository;

    public List<ProductsResponseInfo> getProductsInfo()
    {
        return this.jpaProductsRepository.findAll().stream().map(ProductsResponseInfo::of).collect(Collectors.toList());
    }

    public Optional<Product> findById(String id) {
        return this.jpaProductsRepository.findById(id);
    }

}
