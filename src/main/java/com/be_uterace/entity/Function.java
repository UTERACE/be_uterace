package com.be_uterace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FUNCTION")
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FUNC_ID")
    private Long funcId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "FUNC_PARENT_ID", referencedColumnName = "FUNC_ID")
    private Function parentFunction;

    @Column(name = "FUNC_NAME")
    private String funcName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "API_PATH")
    private String apiPath;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "ICON")
    private String icon;

    // Getters and setters
}

