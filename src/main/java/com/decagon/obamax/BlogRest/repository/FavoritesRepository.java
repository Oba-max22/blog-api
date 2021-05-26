package com.decagon.obamax.BlogRest.repository;

import com.decagon.obamax.BlogRest.model.Favorites;
import com.decagon.obamax.BlogRest.model.Post;
import com.decagon.obamax.BlogRest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    List<Favorites> findAllByUser(User user);
}
