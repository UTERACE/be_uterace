package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PictureData {
    private int height;
    private boolean is_silhouette;
    private String url;
    private int width;
    public PictureData(@JsonProperty("height") int height,
                       @JsonProperty("is_silhouette") boolean is_silhouette,
                       @JsonProperty("url") String url,
                       @JsonProperty("width") int width) {
        this.height = height;
        this.is_silhouette = is_silhouette;
        this.url = url;
        this.width = width;
    }
}
