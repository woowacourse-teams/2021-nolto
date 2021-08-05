import React, { useState } from 'react';

import zigIcon from 'assets/team/zig.png';
import Camera from 'assets/camera.svg';
import NoteEdit from 'assets/noteEdit.svg';
import Styled from './Intro.styles';

const Intro = () => {
  const [isEditing, setIsEditing] = useState(false);

  return (
    <Styled.Root>
      <Styled.ImageWrapper>
        <Styled.UserImage src={zigIcon} />
        {isEditing && (
          <Styled.CameraLabel>
            <input type="file" />
            <Camera width="14px" />
          </Styled.CameraLabel>
        )}
      </Styled.ImageWrapper>

      <Styled.Content>
        <Styled.TopContainer>
          <Styled.Name contentEditable={isEditing} spellCheck={false} isEditing={isEditing}>
            Jieun Song
          </Styled.Name>
          {isEditing ? (
            <Styled.EditButton type="button" onClick={() => setIsEditing(false)}>
              <span>수정</span>
            </Styled.EditButton>
          ) : (
            <Styled.EditButton type="button" onClick={() => setIsEditing(true)}>
              <NoteEdit width="18px" />
            </Styled.EditButton>
          )}
        </Styled.TopContainer>
        {isEditing && (
          <Styled.ValidationMessage isValid={false}>
            이미 사용중인 닉네임입니다.
          </Styled.ValidationMessage>
        )}
        <Styled.DetailContainer>
          <Styled.DetailText>Joined at 2021.07.22</Styled.DetailText>
          <Styled.DetailText contentEditable={isEditing} spellCheck={false} isEditing={isEditing}>
            안녕하세요 지그입니다
          </Styled.DetailText>
        </Styled.DetailContainer>
      </Styled.Content>
    </Styled.Root>
  );
};

export default Intro;
