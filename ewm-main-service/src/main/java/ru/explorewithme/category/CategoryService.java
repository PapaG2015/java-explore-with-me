package ru.explorewithme.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.explorewithme.IdService;
import ru.explorewithme.category.dto.CategoryDto;
import ru.explorewithme.category.dto.NewCategoryDto;
import ru.explorewithme.category.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {
    private CategoryRepository categoryRepository;

    private IdService idService;

    public CategoryService(CategoryRepository categoryRepository, IdService idService) {
        this.categoryRepository = categoryRepository;
        this.idService = idService;
    }

    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);

        Category categoryDB = categoryRepository.save(category);
        log.info("Added category: {}", categoryDB);
        return CategoryMapper.toCategoryDto(categoryDB);
    }

    public void deleteCategory(Long catId) {
        idService.getCategoryById(catId);

        log.info("Deleted category with id={}", catId);
        categoryRepository.deleteById(catId);
    }

    public CategoryDto changeCategory(CategoryDto categoryDto) {
        idService.getCategoryById(categoryDto.getId());

        Category category = CategoryMapper.toCategory(categoryDto);
        categoryRepository.save(category);

        log.info("Changed category: {}", category);
        return CategoryMapper.toCategoryDto(
                categoryRepository.findById(categoryDto.getId()).get());
    }

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Category> categories = categoryRepository.findAll(pageable);

        List<CategoryDto> categoryDtos =
                categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
        log.info("Getted from={}, size={} categories:", from, size, categoryDtos);
        return  categoryDtos;
    }

    public CategoryDto getCategory(Long catId) {
        Category category = idService.getCategoryById(catId);

        log.info("Getted category: {}", category);
        return CategoryMapper.toCategoryDto(category);
    }
}
