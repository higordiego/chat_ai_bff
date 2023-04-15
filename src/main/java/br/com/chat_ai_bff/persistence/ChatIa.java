package br.com.chat_ai_bff.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
public class ChatIa extends PanacheEntity {

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name="identify")
    public String identify;
    @Lob
    @Column(name="search")
    @Type(type = "org.hibernate.type.TextType")
    @NotBlank(message="search no be blank")
    public String search;
    @Lob
    @Column(name="response_text")
    @Type(type = "org.hibernate.type.TextType")
    public String responseText;

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    @CreationTimestamp
    @Column(name = "createdAt")
    public ZonedDateTime createdAt;

}