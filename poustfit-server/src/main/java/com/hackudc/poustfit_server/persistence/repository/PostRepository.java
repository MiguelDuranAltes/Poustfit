package com.hackudc.poustfit_server.persistence.repository;

import com.hackudc.poustfit_server.persistence.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
}
