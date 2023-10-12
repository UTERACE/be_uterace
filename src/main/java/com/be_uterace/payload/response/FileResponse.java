package com.be_uterace.payload.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    String fileName;
    String message;

}
