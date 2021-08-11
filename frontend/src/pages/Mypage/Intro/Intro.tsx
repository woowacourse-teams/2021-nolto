import React, { useState } from 'react';
import { useForm } from 'react-hook-form';

import useProfileLoad from 'hooks/queries/profile/useProfileLoad';
import useProfileEdit from 'hooks/queries/profile/useProfileEdit';
import useMember from 'hooks/queries/useMember';
import useNicknameCheck from 'hooks/queries/profile/useNicknameCheck';
import useQueryDebounce from 'hooks/@common/useQueryDebounce';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useDialog from 'contexts/dialog/useDialog';
import { ALERT_MSG } from 'constants/message';
import Camera from 'assets/camera.svg';
import NoteEdit from 'assets/noteEdit.svg';
import Styled from './Intro.styles';
import { THUMBNAIL_EXTENSION } from 'constants/common';

type ProfileToUpload = {
  nickname: string;
  bio?: string;
  image?: File;
};

const Intro = () => {
  const [isEditing, setIsEditing] = useState(false);

  const snackbar = useSnackbar();
  const dialog = useDialog();

  const { refetchMember } = useMember();

  const { data: profile, refetch: refetchProfile } = useProfileLoad({
    errorHandler: (error) => {
      snackbar.addSnackbar('error', error.message);
    },
  });

  const [previewImage, setPreviewImage] = useState(profile.imageUrl);

  const editMutation = useProfileEdit();

  const { nickname, bio } = profile;

  const { register, handleSubmit, setValue, watch } = useForm<ProfileToUpload>({
    defaultValues: { nickname, bio },
  });

  const debouncedNickname = useQueryDebounce(watch('nickname'), 200);
  const { data: nicknameCheck } = useNicknameCheck({
    nickname: debouncedNickname,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
  });

  const setFileInput = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.currentTarget.files[0];

    if (!THUMBNAIL_EXTENSION.includes(file.type)) {
      dialog.alert('잘못된 확장자입니다.');

      return;
    }

    const reader = new FileReader();

    reader.onloadend = (event) => {
      setPreviewImage(event.target.result as string);
    };

    if (file) {
      reader.readAsDataURL(file);
    }

    setValue('image', file);
  };

  const editProfile = (data: ProfileToUpload) => {
    const formData = new FormData();

    Object.keys(data).forEach((key: keyof typeof data) => {
      formData.append(key, data[key]);
    });

    editMutation.mutate(
      { formData },
      {
        onSuccess: () => {
          snackbar.addSnackbar('success', ALERT_MSG.SUCCESS_EDIT_PROFILE);
          refetchProfile({ throwOnError: true });
          refetchMember();
        },
        onError: () => snackbar.addSnackbar('error', ALERT_MSG.FAIL_EDIT_PROFILE),
      },
    );

    setIsEditing(false);
  };

  const nicknameValidation = () => {
    if (watch('nickname').length >= 10) {
      return {
        isValid: false,
        message: '닉네임을 10글자 미만으로 입력해주세요.',
      };
    }

    if (watch('nickname').length < 2) {
      return {
        isValid: false,
        message: '닉네임을 2글자 이상 입력해주세요.',
      };
    }

    if (nicknameCheck?.isUsable === false) {
      return {
        isValid: false,
        message: '이미 사용중인 닉네임입니다.',
      };
    }

    return {
      isValid: true,
      message: '사용 가능한 닉네임입니다',
    };
  };

  const createdAt = new Date(profile.createdAt);

  const joinedAt =
    createdAt.getFullYear() + '.' + (createdAt.getMonth() + 1) + '.' + createdAt.getDate();

  return (
    <Styled.Root onSubmit={handleSubmit(editProfile)}>
      <Styled.ImageWrapper>
        {isEditing ? (
          <>
            <Styled.UserImage src={previewImage} />
            <Styled.CameraLabel>
              <input type="file" onChange={setFileInput} accept={THUMBNAIL_EXTENSION.join(',')} />
              <Camera width="14px" />
            </Styled.CameraLabel>
          </>
        ) : (
          <Styled.UserImage src={profile.imageUrl} />
        )}
      </Styled.ImageWrapper>

      <Styled.Content>
        <Styled.TopContainer>
          {isEditing ? (
            <>
              <Styled.NameInput
                {...register('nickname', { minLength: 2, maxLength: 10 })}
                spellCheck={false}
              />
              <Styled.EditDoneButton>
                <span>수정</span>
              </Styled.EditDoneButton>
            </>
          ) : (
            <>
              <Styled.Name>{profile.nickname}</Styled.Name>
              <Styled.EditButton type="button" onClick={() => setIsEditing(true)}>
                <NoteEdit width="18px" />
              </Styled.EditButton>
            </>
          )}
        </Styled.TopContainer>
        {isEditing && nickname !== watch('nickname') && (
          <Styled.ValidationMessage isValid={nicknameValidation().isValid}>
            {nicknameValidation().message}
          </Styled.ValidationMessage>
        )}
        <Styled.DetailContainer>
          <Styled.DetailText>Joined at {joinedAt}</Styled.DetailText>
          {isEditing ? (
            <Styled.BioInput
              {...register('bio')}
              placeholder={watch('bio') || '소개를 입력해 주세요.'}
              spellCheck={false}
            />
          ) : (
            <Styled.DetailText>{profile.bio || '소개를 입력해 주세요.'}</Styled.DetailText>
          )}
        </Styled.DetailContainer>
      </Styled.Content>
    </Styled.Root>
  );
};

export default Intro;
