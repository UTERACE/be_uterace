package com.be_uterace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMMENT_POST")
public class CommentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Integer commentId;

    @Column(name = "COMMENT_CONTENT", length = 1000)
    private String commentContent;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID", referencedColumnName = "POST_ID")
    private Post post;

    @Column(name = "CREATED_AT")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;

    @Column(name = "STATUS", length = 1)
    private String status = "0";

    @Column(name = "REASON")
    private String reason;

    @Column(name = "REPLY_TO")
    private Integer replyTo;

    public CommentPost(String content) {
        this.commentContent = content;
    }
}
