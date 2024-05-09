package com.be_uterace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REACTION_COMMENT")
public class ReactionComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REACTION_ID")
    private Integer reactionId;

    @Column(name = "REACTION_TYPE")
    private String reactionType = "like";

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID", referencedColumnName = "COMMENT_ID")
    private CommentPost comment;

    public ReactionComment(String reactionType) {
        this.reactionType = reactionType;
    }
}
