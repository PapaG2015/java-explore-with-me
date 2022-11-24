package ru.explorewithme.stats.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;

    @Override
    public String toString() {
        return "ViewStats{" +
                "app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", hits=" + hits +
                '}';
    }
}
