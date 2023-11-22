package com.be_uterace.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDto {
    @NotBlank(message = "The title is required.")
    @Size(max = 100, message = "The title must not exceed 100 characters.")
    private String title;

    @Size(max = 500, message = "The description must not exceed 500 characters.")
    private String description;
    private String image;
    private String content;
    private Integer club_id;
}
