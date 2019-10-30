
package com.SteelBlog.SteelBlog.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.UUID;

@Document(indexName = "springboot2blog_Post", type = "Post")
@Data
public class Post {

    @Id
    private String id;

    private String title;

    private String link;

    private String summary;

    private String body;

    @Field(type = FieldType.Nested)
    private User author;

    @Field(type = FieldType.Date)
    private Date createdDate = new Date();

}
