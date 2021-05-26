package com.decagon.obamax.BlogRest.service.serviceImpl;

import com.decagon.obamax.BlogRest.exception.ResourceNotFoundException;
import com.decagon.obamax.BlogRest.model.Comment;
import com.decagon.obamax.BlogRest.model.CommentLikes;
import com.decagon.obamax.BlogRest.model.Post;
import com.decagon.obamax.BlogRest.repository.CommentLikesRepository;
import com.decagon.obamax.BlogRest.repository.CommentRepository;
import com.decagon.obamax.BlogRest.repository.PostRepository;
import com.decagon.obamax.BlogRest.response.ApiResponse;
import com.decagon.obamax.BlogRest.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikesRepository commentLikesRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, CommentLikesRepository commentLikesRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentLikesRepository = commentLikesRepository;
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comment createComment(Long postId, Comment comment) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            comment.setUser(post.get().getUser());
            comment.setPost(post.get());
            return commentRepository.save(comment);
        } else {
            throw new ResourceNotFoundException("Post not found with id " + postId);
        }
    }

    @Override
    public Comment editComment(Long commentId, Comment commentRequest) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isPresent()) {
            comment.get().setComment_body(commentRequest.getComment_body());
            return commentRepository.save(comment.get());
        } else {
            throw new ResourceNotFoundException("Comment not found with id " + commentId);
        }
    }

    @Override
    public ResponseEntity<?> deleteComment(Long commentId) {

        Optional<Comment> comment =  commentRepository.findById(commentId);
        if(comment.isPresent()) {
            commentRepository.delete(comment.get());
            return new ResponseEntity<>(new ApiResponse(true, "Comment has been deleted."), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Comment not found with id " + commentId);
        }
    }

    @Override
    public List<CommentLikes> getCommentLikes(Long commentId) {
        return commentLikesRepository.findAllByCommentId(commentId);
    }

    @Override
    public CommentLikes likeComment(Long commentId, CommentLikes commentLike) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isPresent()) {
            commentLike.setUser(comment.get().getUser());
            commentLike.setPost(comment.get().getPost());
            commentLike.setComment(comment.get());
            return commentLikesRepository.save(commentLike);
        } else {
            throw new ResourceNotFoundException("Comment not found with id " + commentId);
        }
    }

    @Override
    public ResponseEntity<?> dislikeComment(Long commentLikeId) {
        Optional<CommentLikes> commentLikes = commentLikesRepository.findById(commentLikeId);
        if (commentLikes.isPresent()) {
            commentLikesRepository.delete(commentLikes.get());
            return new ResponseEntity<>(new ApiResponse(true, "Comment has been disliked."), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("CommentLikeId not found with id " + commentLikeId);
        }
    }

    public void checkCommentExists (Long commentId) {
        if(!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment not found with id " + commentId);
        }
    }
}
