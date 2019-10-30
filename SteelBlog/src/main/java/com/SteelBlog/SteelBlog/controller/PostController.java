
package com.SteelBlog.SteelBlog.controller;

import com.SteelBlog.SteelBlog.exception.NotFoundException;
import com.SteelBlog.SteelBlog.model.Post;
import com.SteelBlog.SteelBlog.model.User;
import com.SteelBlog.SteelBlog.service.PostService;
import com.SteelBlog.SteelBlog.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/Post")
public class PostController {

    private final PostService PostService;
    private final UserService userService;

    public PostController(PostService PostService, UserService userService) {
        this.PostService = PostService;
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model,
                        @AuthenticationPrincipal UserDetails userDetails,
                        @RequestParam(required = false, value = "q") String q,
                        @RequestParam(required = false, value = "page") Integer page,
                        @RequestParam(required = false, value = "size") Integer size) {
        if (q == null) {
            model.addAttribute("Posts", PostService.getAll(getPageable(page, size)));
        } else {
            model.addAttribute("Posts", PostService.search(q, getPageable(page, size)));
        }

        return "Post/index";
    }

    @GetMapping("/show/{link}")
    public String getPost(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String link, Model model) {
        Optional<Post> Post = PostService.getByLink(link);
        if (Post.isPresent()) {
            model.addAttribute("Post", Post.get());
        } else {
            throwNotFoundException(link);
        }

        return "Post/show";
    }

    @GetMapping("/new")
    public String newPost() {
        return "Post/create";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String id, Model model) {
        Optional<Post> Post = PostService.getById(id);
        if (Post.isPresent()) {
            model.addAttribute("Post", Post.get());
        } else {
            return throwNotFoundException(id);
        }

        return "Post/create";
    }

    private String throwNotFoundException(@PathVariable String id) {
        throw new NotFoundException("Post Not Found for "+id);
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String id, Model model) {
        PostService.deleteById(id);

        model.addAttribute("message", "Post with id " + id + " deleted successfully!");
        model.addAttribute("Posts", PostService.getAll(new PageRequest(0, 10)));

        return "Post/index";
    }

    @PostMapping
    public String savePost(@AuthenticationPrincipal UserDetails userDetails, Post Post, Model model) {
        if (Post.getId() == null || Post.getId().length() == 0) {
            User user = userService.getByUsername(userDetails.getUsername());
            Post.setAuthor(user);
        } else {
            Optional<Post> optionalPost = PostService.getById(Post.getId());
            if (optionalPost.isPresent()) {
                Post.setAuthor(optionalPost.get().getAuthor());
            }
        }
        PostService.save(Post);

        return "redirect:/Post/show/"+Post.getLink();
    }

    @GetMapping("/rest")
    @ResponseBody
    public Page<Post> PostsRest(@RequestParam(required = false, value = "page") Integer page,
                                      @RequestParam(required = false, value = "size") Integer size) {
        return PostService.getAll(getPageable(page, size));
    }

    private Pageable getPageable(Integer page, Integer size) {
        if (page == null || size == null) {
            return PageRequest.of(0, 20);
        }

        return PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "createdDate"));
    }

}
