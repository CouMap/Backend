package backend.coumap.category.repository;

import backend.coumap.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByCode(String code);
}
