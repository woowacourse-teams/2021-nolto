package com.wooteco.nolto.user.domain;

import com.wooteco.nolto.auth.domain.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    boolean existsByNickName(String nickName);
}
