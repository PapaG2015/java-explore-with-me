package ru.explorewithme.compilation.dto;

import lombok.Builder;
import lombok.Getter;
import ru.explorewithme.event.dto.EventShortDto;

import java.util.List;

@Builder
@Getter
public class CompilationDto {
    private List<EventShortDto> events;
    private Long id;
    private  Boolean pinned;
    private String title;

    @Override
    public String toString() {
        return "CompilationDto{" +
                "events=" + events +
                ", id=" + id +
                ", pinned=" + pinned +
                ", title='" + title + '\'' +
                '}';
    }
}
