
package com.SteelBlog.SteelBlog.repo;

import com.SteelBlog.SteelBlog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface PostRepository extends ElasticsearchRepository<Post, String> {

    Optional<Post> findByLink(String link);

    Page<Post> findByTitleContainingAndBodyContaining(String title, String body, Pageable pageable);
}
