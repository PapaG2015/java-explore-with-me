package ru.explorewithme.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.explorewithme.client.StatGetClient;
import ru.explorewithme.client.ViewStats;
import ru.explorewithme.compilation.dto.CompilationDto;
import ru.explorewithme.compilation.dto.NewCompilationDto;
import ru.explorewithme.compilation.model.Compilation;
import ru.explorewithme.event.EventMapper;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.request.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    //@Autowired
    //private static RequestRepository requestRepository;
    //@Autowired
    //private  static StatGetClient statGetClient;
    //private static final String API_PREFIX = "/events/";
    //@Value("${main-server.url}")
    //private static String url;


    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation
                .builder()
                .id(null)
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation,
                                                  RequestRepository requestRepository,
                                                  StatGetClient statGetClient,
                                                  String url) {
        return CompilationDto
                .builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(
                        compilation.getEvents().stream().map(event -> {
                            Long confirmedRequest = requestRepository.countConfirmedRequests(event.getId());
                            //Получение статистики
                            LocalDateTime start = LocalDateTime.now().minusYears(1);
                            LocalDateTime end = LocalDateTime.now().plusYears(1);
                            List<ViewStats> viewStats = statGetClient.getEndPoint(start, end, List.of(url + event.getId()));
                            Long hits = viewStats.get(0).getHits();
                            return EventMapper.toEventShortDto(event, confirmedRequest, hits);
                        }).collect(Collectors.toList())
                )
                .build();
    }

}
