package ru.explorewithme.compilation.dto;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
public class NewCompilationDto {
    private Set<Long> events;
    @Value("false")
    private Boolean pinned;
    @NotBlank(message = "title can't be blank")
    private String title;

    @Override
    public String toString() {
        return "NewCompilationDto{" +
                "events=" + events +
                ", pinned=" + pinned +
                ", title='" + title + '\'' +
                '}';
    }
}
