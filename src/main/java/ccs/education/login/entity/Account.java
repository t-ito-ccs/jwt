package ccs.education.login.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "account")
@Entity
@Getter
@Setter
public class Account {
    @Id
    @Column(name="id")
    private String id;
    @Column(name="password")
    private String password;
}