package com.decagon.obamax.BlogRest.controller;


import com.decagon.obamax.BlogRest.exception.ResourceNotFoundException;
import com.decagon.obamax.BlogRest.model.Comment;
import com.decagon.obamax.BlogRest.model.CommentLikes;
import com.decagon.obamax.BlogRest.model.Post;
import com.decagon.obamax.BlogRest.model.PostLikes;
import com.decagon.obamax.BlogRest.repository.CommentLikesRepository;
import com.decagon.obamax.BlogRest.repository.CommentRepository;
import com.decagon.obamax.BlogRest.repository.PostRepository;
import com.decagon.obamax.BlogRest.repository.UserRepository;
import com.decagon.obamax.BlogRest.response.ApiResponse;
import com.decagon.obamax.BlogRest.service.serviceImpl.CommentServiceImpl;
import com.decagon.obamax.BlogRest.service.serviceImpl.PostServiceImpl;
import com.decagon.obamax.BlogRest.service.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {
    @Autowired
    CommentServiceImpl commentService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    PostServiceImpl postService;

    @GetMapping("/users/{userId}/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId,
                                             @PathVariable Long userId) {
        List<Comment> commentList = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @GetMapping("/comments/")
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> AllComments = commentService.getAllComments();
        return new ResponseEntity<>(AllComments, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/posts/{postId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long userId, @PathVariable Long postId,
                        @Valid @RequestBody Comment comment) {

        Comment createdComment = commentService.createComment(postId, comment);
        return new ResponseEntity<>(createdComment, HttpStatus.OK);

    }

    @PutMapping("/users/{userId}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long userId,
                                           @PathVariable Long postId, @PathVariable Long commentId,
                                           @Valid @RequestBody Comment commentRequest, HttpSession session) {
        userService.checkUserExists(userId);
        postService.checkPostExists(postId);
        Comment updatedComment = commentService.editComment(commentId, commentRequest);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deletePost(@PathVariable Long userId,
                                        @PathVariable Long postId,
                                        @PathVariable Long commentId, HttpSession session) {
        userService.checkUserExists(userId);
        postService.checkPostExists(postId);
        return commentService.deleteComment(commentId);
    }

    @GetMapping("/users/{userId}/posts/{postId}/comments/{commentId}/commentLikes")
    public ResponseEntity<List<CommentLikes>> getCommentLikesByCommentId(@PathVariable Long postId,
                                                          @PathVariable Long userId,
                                                          @PathVariable Long commentId) {
        List<CommentLikes> commentLikesList = commentService.getCommentLikes(commentId);
        return new ResponseEntity<>(commentLikesList, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/posts/{postId}/comments/{commentId}/commentLikes")
    public ResponseEntity<CommentLikes> likeComment(@PathVariable Long userId,
                                 @PathVariable Long postId,
                                 @Valid @RequestBody CommentLikes commentLike,
                                 @PathVariable Long commentId){

        CommentLikes commentLikes = commentService.likeComment(commentId, commentLike);
        return new ResponseEntity<>(commentLikes, HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/posts/{postId}/comments/{commentId}/commentLikes/{commentLikeId}")
    public ResponseEntity<?> dislikeComment(@PathVariable Long postId,
                                        @PathVariable Long commentLikeId,
                                        @PathVariable Long userId,
                                        @PathVariable Long commentId){
        userService.checkUserExists(userId);
        postService.checkPostExists(postId);
        commentService.checkCommentExists(commentId);
        return commentService.dislikeComment(commentLikeId);
    }

}