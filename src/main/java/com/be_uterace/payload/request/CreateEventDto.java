package com.be_uterace.payload.request;

import com.be_uterace.payload.response.RunningCategoryResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDto {
    @NotBlank(message = "The name is required.")
    @Size(max = 100, message = "The name must not exceed 100 characters.")
    private String name;
    private String image;
    private String description;
    private Date from_date;
    private Date to_date;
    private List<RunningCategoryResponse> distance;
    private Double min_pace;
    private Double max_pace;
    private String details;
    private String regulations;
    private String prize;
}
