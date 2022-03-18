package com.example.restfulservice.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/jpa")
@Slf4j
public class PostJPAController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/posts")
    public List<Post> retrieveAllPosts() {

        List<Post> posts = postRepository.findAll();

        return posts;
    }

    @GetMapping("/posts/{id}")
    public Optional<Post> retrieveOnePost(@PathVariable int id) {
        Optional<Post> post = postRepository.findById(id);
        return post;
    }

    @PostMapping("/posts/{id}")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post) {
        Optional<User> user = userRepository.findById(id);

        post.setUser(user.get()); // user 생성
        Post savedPost = postRepository.save(post);

        return ResponseEntity.ok(savedPost);
    }

}
