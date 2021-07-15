package com.wooteco.nolto.tech.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechRepository extends JpaRepository<Tech, Long> {
    List<Tech> findByNameStartsWithIgnoreCase(String name);
}
