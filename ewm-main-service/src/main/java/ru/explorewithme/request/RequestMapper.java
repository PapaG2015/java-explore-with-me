package ru.explorewithme.request;

import ru.explorewithme.request.dto.ParticipationRequestDto;
import ru.explorewithme.request.model.Request;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus().toString())
                .build();
    }

    public static List<ParticipationRequestDto> toParticipationRequestDto(List<Request> requests) {
        List<ParticipationRequestDto> participationRequestDtos =
                requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());

        return participationRequestDtos;
    }
}
