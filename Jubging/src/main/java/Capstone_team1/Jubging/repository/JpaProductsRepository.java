package Capstone_team1.Jubging.repository;

import Capstone_team1.Jubging.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductsRepository extends JpaRepository<Product, String> {

}
