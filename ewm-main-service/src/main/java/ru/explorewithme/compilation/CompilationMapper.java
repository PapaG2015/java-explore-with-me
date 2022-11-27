package ru.explorewithme.compilation;

import ru.explorewithme.client.StatGetClient;
import ru.explorewithme.client.ViewStats;
import ru.explorewithme.compilation.dto.CompilationDto;
import ru.explorewithme.compilation.dto.NewCompilationDto;
import ru.explorewithme.compilation.model.Compilation;
import ru.explorewithme.event.EventMapper;
import ru.explorewithme.event.dto.EventShortDto;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.request.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation
                .builder()
                .id(null)
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto
                .builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(events)
                .build();
    }

}
