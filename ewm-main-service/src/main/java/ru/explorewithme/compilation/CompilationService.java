package ru.explorewithme.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.explorewithme.IdService;
import ru.explorewithme.client.StatGetClient;
import ru.explorewithme.compilation.dto.CompilationDto;
import ru.explorewithme.compilation.dto.NewCompilationDto;
import ru.explorewithme.compilation.model.Compilation;
import ru.explorewithme.event.EventRepository;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.request.RequestRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompilationService {
    private static final String API_PREFIX = "/events/";
    private CompilationRepository compilationRepository;
    private EventRepository eventRepository;

    private IdService idService;

    private RequestRepository requestRepository;
    private StatGetClient statGetClient;
    private String url;

    public CompilationService(CompilationRepository compilationRepository,
                              EventRepository eventRepository,
                              IdService idService,
                              RequestRepository requestRepository,
                              StatGetClient statGetClient,
                              @Value("${main-server.url}") String url) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.idService = idService;
        this.requestRepository = requestRepository;
        this.statGetClient = statGetClient;
        this.url = url + API_PREFIX;
    }

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.getEventsAtIds(newCompilationDto.getEvents());

        Compilation compilation = compilationRepository.save(
                CompilationMapper.toCompilation(newCompilationDto, events));

        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilation, requestRepository, statGetClient, url);
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
                    return CompilationMapper.toCompilationDto(compilation, requestRepository, statGetClient, url);
                })
                .collect(Collectors.toList());
        log.info("Getting with pinned={}, from={}, size={} compilations:{}", pinned, from, size, compilationDtos);
        return compilationDtos;
    }

    public CompilationDto getCompilation(Long complId) {
        Compilation compilation = idService.getCompilationById(complId);

        log.info("Getted compilation: {}", compilation);
        return CompilationMapper.toCompilationDto(compilation, requestRepository, statGetClient, url);
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
}
