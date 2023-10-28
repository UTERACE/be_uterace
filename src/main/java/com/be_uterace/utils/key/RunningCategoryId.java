package com.be_uterace.utils.key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RunningCategoryId implements Serializable {
    private Integer runningCategoryID;
    private Integer event;
}
