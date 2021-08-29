package ccs.education.login.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "Token")
@Entity
@Getter
@Setter
public class Token {
    @Id
    @Column(name="id")
    private String id;
    @Column(name="refreshToken")
    private String refreshToken;
    @Column(name="IssueDateTime")
    private Instant IssueDateTime;
}