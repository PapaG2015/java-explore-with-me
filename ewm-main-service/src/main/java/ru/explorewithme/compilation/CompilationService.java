package ru.explorewithme.compilation;
;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.explorewithme.IdService;
import ru.explorewithme.client.StatGetClient;
import ru.explorewithme.client.ViewStats;
import ru.explorewithme.compilation.dto.CompilationDto;
import ru.explorewithme.compilation.dto.NewCompilationDto;
import ru.explorewithme.compilation.model.Compilation;
import ru.explorewithme.event.EventMapper;
import ru.explorewithme.event.EventRepository;
import ru.explorewithme.event.dto.EventShortDto;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.request.RequestRepository;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationService {
    private static final String API_PREFIX = "/events/";

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final IdService idService;

    private final RequestRepository requestRepository;

    private final StatGetClient statGetClient;

    @Value("${main-server.url}" + API_PREFIX)
    private String url;

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.getEventsAtIds(newCompilationDto.getEvents());

        Compilation compilation = compilationRepository.save(
                CompilationMapper.toCompilation(newCompilationDto, events));

        List<EventShortDto> shortEvents = getShortEventByComp(compilation);

        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation, shortEvents);
        log.info("Added compilation: {}", compilationDto);
        return compilationDto;
    }

    public void deleteEventFromCompilation(Long complId, Long eventId) {
        Compilation compilation = idService.getCompilationById(complId);

        compilation.getEvents().removeIf(event -> event.getId() == eventId);
        log.info("Deleted event with id={} from compilation with id={}", eventId, complId);
        compilationRepository.save(compilation);
    }

    public List<CompilationDto> getCompilations(Integer from, Integer size, Boolean pinned) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Compilation> compilations = compilationRepository.findByPinned(pinned, pageable);

        List<CompilationDto> compilationDtos = compilations
                .stream()
                .map(compilation -> {
                    return CompilationMapper.toCompilationDto(compilation, getShortEventByComp(compilation));
                })
                .collect(Collectors.toList());
        log.info("Getting with pinned={}, from={}, size={} compilations:{}", pinned, from, size, compilationDtos);
        return compilationDtos;
    }

    public CompilationDto getCompilation(Long complId) {
        Compilation compilation = idService.getCompilationById(complId);

        log.info("Getted compilation: {}", compilation);
        return CompilationMapper.toCompilationDto(compilation, getShortEventByComp(compilation));
    }

    public void addEventToCompilation(Long complId, Long eventId) {
        Compilation compilation = idService.getCompilationById(complId);
        Event event = idService.getEventById(eventId);

        compilation.getEvents().add(event);
        log.info("Adding event with id={} to compilation with id={}", eventId, complId);
        compilationRepository.save(compilation);
    }

    public void unpinCompilation(Long complId) {
        Compilation compilation = idService.getCompilationById(complId);
        compilation.setPinned(false);
        log.info("Unpinned compiltaion with id={}", complId);
        compilationRepository.save(compilation);
    }

    public void pinCompilation(Long complId) {
        Compilation compilation = idService.getCompilationById(complId);
        compilation.setPinned(true);
        log.info("Pinned compiltaion with id={}", complId);
        compilationRepository.save(compilation);
    }

    public void deleteCompilation(Long complId) {
        compilationRepository.deleteById(complId);
        log.info("Deleted compiltaion with id={}", complId);
    }

    private List<EventShortDto> getShortEventByComp(Compilation compilation) {
        return compilation.getEvents().stream().map(event -> {
            Long confirmedRequest = requestRepository.countConfirmedRequests(event.getId());
            //Получение статистики
            LocalDateTime start = LocalDateTime.now().minusYears(1);
            LocalDateTime end = LocalDateTime.now().plusYears(1);
            List<ViewStats> viewStats = statGetClient.getEndPoint(start, end, List.of(url + event.getId()));
            Long hits = viewStats.get(0).getHits();
            return EventMapper.toEventShortDto(event, confirmedRequest, hits);
        }).collect(Collectors.toList());
    }
}
