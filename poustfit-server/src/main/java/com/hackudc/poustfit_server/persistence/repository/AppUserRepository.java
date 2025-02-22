package com.hackudc.poustfit_server.persistence.repository;

import com.hackudc.poustfit_server.persistence.entity.post.Post;
import com.hackudc.poustfit_server.persistence.entity.user.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByCorreo(String correo);

    Optional<AppUser> findByJwtToken_Token(String jwtToken);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM likes WHERE user_id = :userId AND post_id = :postId)",
            nativeQuery = true)
    boolean likesPost(Long userId, Long postId);

    @Query("SELECT p FROM AppUser u JOIN u.postsLiked p WHERE u.id = :userId")
    Page<Post> findPostsLikedByUser(Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.autor.id = :userId")
    Page<Post> findPostsByUser(Long userId, Pageable pageable);
}
