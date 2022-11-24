package ru.explorewithme.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.explorewithme.stats.model.GetStatRequest;
import ru.explorewithme.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/stats")
@Slf4j
public class StatController {
    private StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @GetMapping
    public List<ViewStats> getStat(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) Set<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique) {

        log.info("Getting stats with start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return statService.getStat(GetStatRequest.of(start, end, uris, unique));
    }
}
