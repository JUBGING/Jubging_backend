package Capstone_team1.Jubging.dto.products;

import Capstone_team1.Jubging.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductsResponseInfo {
    private String id;
    private String name;
    private Integer price;
    private Integer cnt;
    private String contents;
    private String imgUrl;

    public static ProductsResponseInfo of(Product product)
    {
        return new ProductsResponseInfo(product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCnt(),
                product.getContents(),
                product.getImgUrl());
    }
}
