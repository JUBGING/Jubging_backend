package Capstone_team1.Jubging.dto.user;

import Capstone_team1.Jubging.domain.Dibs;
import Capstone_team1.Jubging.domain.Product;
import Capstone_team1.Jubging.dto.products.ProductsResponseInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchShoppingBagResponseDto {

    private String product_id;
    private Integer cnt;
    private String name;
    private Integer price;
    private String imgUrl;
    private String contents;

    public static SearchShoppingBagResponseDto of(Dibs dibs)
    {
        return new SearchShoppingBagResponseDto(
                dibs.getProduct().getId(),
                dibs.getProduct().getCnt(),
                dibs.getProduct().getName(),
                dibs.getProduct().getPrice(),
                dibs.getProduct().getImgUrl(),
                dibs.getProduct().getContents());
    }
}
