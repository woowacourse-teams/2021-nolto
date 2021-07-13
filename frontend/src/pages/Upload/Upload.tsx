import React from 'react';

import FormInput from 'components/@common/FormInput/FormInput';
import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import Label from 'components/@common/Label/Label';
import { ButtonStyle } from 'types';
import Styled, { ContentTextArea, StyledButton } from './Upload.styles';
import FileInput from 'components/@common/FileInput/FileInput';
import { FlexContainer } from 'commonStyles';
import Toggle from 'components/@common/Toggle/Toggle';
import Checkbox from 'components/@common/Checkbox/Checkbox';
import TextButton from 'components/@common/TextButton/TextButton';

const Upload = () => {
  return (
    <Styled.Root>
      <Styled.TitleWrapper>
        <HighLightedText fontSize="2.25rem">🦄 Upload Your Toy</HighLightedText>
      </Styled.TitleWrapper>

      <Styled.VerticalWrapper>
        <Label text="제목" required={true} />
        <FormInput />
      </Styled.VerticalWrapper>

      <Styled.VerticalWrapper>
        <Label text="사용 스택" required={true} />
        <FormInput />
      </Styled.VerticalWrapper>

      <Styled.VerticalWrapper>
        <Label text="내용" required={true} />
        <ContentTextArea />
      </Styled.VerticalWrapper>

      <Styled.InputsContainer>
        <Styled.StretchWrapper>
          <Label className="stretch-label" text="레벨" required={true} />
          <FlexContainer width="100%">
            <Checkbox labelText="조립중" onChange={() => {}} />
            <Checkbox labelText="전시중" onChange={() => {}} />
          </FlexContainer>
        </Styled.StretchWrapper>
        <FlexContainer>
          <Label text="SOS" />
          <Toggle onChange={() => {}} />
        </FlexContainer>
      </Styled.InputsContainer>

      <Styled.StretchWrapper>
        <Label className="stretch-label" text="github" required={true} />
        <FormInput />
      </Styled.StretchWrapper>

      <Styled.StretchWrapper>
        <Label className="stretch-label" text="배포 URL" required={true} />
        <FormInput />
      </Styled.StretchWrapper>

      <Styled.StretchWrapper>
        <Label className="stretch-label" text="대표 이미지" />
        <FileInput />
      </Styled.StretchWrapper>

      <Styled.ButtonsWrapper>
        <StyledButton buttonStyle={ButtonStyle.SOLID}>등록</StyledButton>
        <StyledButton buttonStyle={ButtonStyle.OUTLINE}>취소</StyledButton>
      </Styled.ButtonsWrapper>
    </Styled.Root>
  );
};

export default Upload;
