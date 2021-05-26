package com.decagon.obamax.BlogRest.service.serviceImpl;

import com.decagon.obamax.BlogRest.exception.ResourceNotFoundException;
import com.decagon.obamax.BlogRest.model.Favorites;
import com.decagon.obamax.BlogRest.model.Post;
import com.decagon.obamax.BlogRest.model.PostLikes;
import com.decagon.obamax.BlogRest.model.User;
import com.decagon.obamax.BlogRest.repository.FavoritesRepository;
import com.decagon.obamax.BlogRest.repository.PostLikesRepository;
import com.decagon.obamax.BlogRest.repository.PostRepository;
import com.decagon.obamax.BlogRest.repository.UserRepository;
import com.decagon.obamax.BlogRest.response.ApiResponse;
import com.decagon.obamax.BlogRest.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikesRepository postLikesRepository;
    private final FavoritesRepository favoritesRepository;

    @Autowired
    public PostServiceImpl (PostRepository postRepository, UserRepository userRepository, PostLikesRepository postLikesRepository, FavoritesRepository favoritesRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postLikesRepository = postLikesRepository;
        this.favoritesRepository = favoritesRepository;
    }

    @Override
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Post createPost(Long userId, Post postRequest) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            postRequest.setUser(user.get());
            return postRepository.save(postRequest);
        } else {
           throw new ResourceNotFoundException("User not found with id " + userId);
        }
    }

    @Override
    public Post editPost(Long postId, Post postRequest) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            post.get().setTitle(postRequest.getTitle());
            post.get().setBody(postRequest.getBody());
            return postRepository.save(post.get());
        } else {
            throw new ResourceNotFoundException("Post not found with id " + postId);
        }
    }

    @Override
    public ResponseEntity<?>  deletePost(Long userId, Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            postRepository.delete(post.get());
            return new ResponseEntity<>(new ApiResponse(true, "Post has been deleted."), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Post not found with id " + postId);
        }
    }

    @Override
    public List<PostLikes> getAllPostLikes(Long userId, long postId) {
        return postLikesRepository.findByUserIdAndPostId(userId, postId);
    }

    @Override
    public PostLikes likePost(Long userId, Long postId, PostLikes postLike) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            postLike.setUser(userRepository.getById(userId));
            postLike.setPost(post.get());
            return postLikesRepository.save(postLike);
        } else {
            throw new ResourceNotFoundException("Post not found with id " + postId);
        }
    }

    @Override
    public ResponseEntity<?> disLikePost(Long postLikeId) {
        Optional<PostLikes> like = postLikesRepository.findById(postLikeId);
        if (like.isPresent()) {
            postLikesRepository.delete(like.get());
            return new ResponseEntity<>(new ApiResponse(true, "Post has been disliked."), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("PostLike not found with id " + postLikeId);
        }
    }

    @Override
    public Page<Favorites> getAllFavorites(Pageable pageable) {
        return favoritesRepository.findAll(pageable);
    }

    @Override
    public List<Favorites> getUserFavorites(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return favoritesRepository.findAllByUser(user.get());
        } else {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
    }

    @Override
    public Favorites addToFavorites(Long postId, Favorites favoriteRequest) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            favoriteRequest.setTitle(post.get().getTitle());
            favoriteRequest.setBody(post.get().getBody());
            favoriteRequest.setUser(post.get().getUser());
            favoriteRequest.setPost(post.get());
            return favoritesRepository.save(favoriteRequest);
        } else {
            throw new ResourceNotFoundException("Post not found with id "+ postId);
        }
    }

    @Override
    public ResponseEntity<?> removeFromFavorites(Long favoriteID) {
        Optional<Favorites> favorite = favoritesRepository.findById(favoriteID);
        if (favorite.isPresent()) {
            favoritesRepository.delete(favorite.get());
            return new ResponseEntity<>(new ApiResponse(true, "Post has been disliked."), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Favorite not found with id " + favoriteID);
        }
    }

    public void checkPostExists (Long postId) {
        if(!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id " + postId);
        }
    }
}
