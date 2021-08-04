package com.wooteco.nolto.user.application;

import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import com.wooteco.nolto.user.ui.dto.NicknameValidationResponse;
import com.wooteco.nolto.user.ui.dto.ProfileRequest;
import com.wooteco.nolto.user.ui.dto.ProfileResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final ImageService imageService;
    private final UserRepository userRepository;

    public NicknameValidationResponse validateDuplicated(String nickname) {
        boolean isExistNickname = userRepository.existsByNickName(nickname);
        return new NicknameValidationResponse(!isExistNickname);
    }

    public ProfileResponse findProfile(User user) {
        // TODO user의 notifications 수 구하는 로직 필요, user도 가지고 있으면 Service 레이어까지 오지 않을 수 있음
        return ProfileResponse.of(user, 0);
    }

    public ProfileResponse updateProfile(User user, ProfileRequest request) {
        updateIfImageExist(request, user);
        user.updateProfile(request.getNickname(), request.getBio());
        return ProfileResponse.of(user, 0);
    }

    private void updateIfImageExist(ProfileRequest request, User user) {
        if (imageService.isEmpty(request.getImage())) return;

        String updateImageUrl = imageService.update(user.getImageUrl(), request.getImage(), ImageKind.USER);
        user.changeImageUrl(updateImageUrl);
    }
}
