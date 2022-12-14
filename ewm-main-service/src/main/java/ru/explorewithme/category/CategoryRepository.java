package ru.explorewithme.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.explorewithme.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
