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
public class UserClubId implements Serializable {
    private Long user;
    private Integer club;

    // Getter và Setter
}

