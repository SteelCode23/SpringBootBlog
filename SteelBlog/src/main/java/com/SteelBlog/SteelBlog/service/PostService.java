package com.SteelBlog.SteelBlog.service;
import com.SteelBlog.SteelBlog.model.Post;
import com.SteelBlog.SteelBlog.repo.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository PostRepository;

    public PostService(PostRepository PostRepository) {
        this.PostRepository = PostRepository;
    }

    public Post save(Post Post) {
        if (Post.getId() == null) {
            Post.setId(UUID.randomUUID().toString());
        }
        return PostRepository.save(Post);
    }

    public Page<Post> getAll(Pageable pageable) {
        return PostRepository.findAll(pageable);
    }

    public Optional<Post> getByLink(String link) {
        return PostRepository.findByLink(link);
    }

    public Optional<Post> getById(String id) {
        return PostRepository.findById(id);
    }

    public void deleteById(String id) {
        PostRepository.deleteById(id);
    }

    public Page<Post> search(String q, Pageable pageable) {
        return PostRepository.findByTitleContainingAndBodyContaining(q, q, pageable);
    }
}
