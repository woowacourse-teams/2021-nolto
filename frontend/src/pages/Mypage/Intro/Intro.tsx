import React from 'react';

import Styled from './Intro.styles';
import zigIcon from 'assets/team/zig.png';
import Camera from 'assets/camera.svg';
import NoteEdit from 'assets/noteEdit.svg';

const Intro = () => {
  return (
    <Styled.Root>
      <Styled.ImageWrapper>
        <Styled.UserImage src={zigIcon} />
        <Styled.CameraLabel>
          <input type="file" />
          <Camera width="14px" />
        </Styled.CameraLabel>
      </Styled.ImageWrapper>

      <Styled.Content>
        <Styled.TopContainer>
          <Styled.Name>Jieun Song</Styled.Name>
          <Styled.EditButton type="button">
            <NoteEdit width="14px" />
          </Styled.EditButton>
        </Styled.TopContainer>
        <Styled.ValidationMessage isValid={false}>
          이미 사용중인 닉네임입니다.
        </Styled.ValidationMessage>
        <Styled.DetailContainer>
          <Styled.DetailText>Joined at 2021.07.22</Styled.DetailText>
          <Styled.DetailText>안녕하세요 지그입니다</Styled.DetailText>
        </Styled.DetailContainer>
      </Styled.Content>
    </Styled.Root>
  );
};

export default Intro;
