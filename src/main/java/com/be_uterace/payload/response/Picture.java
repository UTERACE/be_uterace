package com.be_uterace.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Picture {
    PictureData data;
    public Picture(@JsonProperty("data") PictureData data) {
        this.data = data;
    }
}
