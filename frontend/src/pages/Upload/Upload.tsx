import React from 'react';
import { useForm } from 'react-hook-form';

import FormInput from 'components/@common/FormInput/FormInput';
import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import Label from 'components/@common/Label/Label';
import FileInput from 'components/@common/FileInput/FileInput';
import Toggle from 'components/@common/Toggle/Toggle';
import RadioButton from 'components/@common/RadioButton/RadioButton';
import Header from 'components/Header/Header';
import { FlexContainer } from 'commonStyles';
import Styled, { ContentTextArea, StyledButton } from './Upload.styles';
import { ButtonStyle, FeedStatus, FeedToUpload } from 'types';
import useUploadFeeds from 'hooks/queries/useUploadFeed';

const Upload = () => {
  const { register, handleSubmit, setValue, watch } = useForm<FeedToUpload>();
  const watchThumbnailImage = watch('thumbnailImage');
  const mutation = useUploadFeeds();

  const uploadFeed = (data: FeedToUpload) => {
    const formData = new FormData();

    Object.keys(data).forEach((key) => {
      if (key === 'thumbnailImage') {
        formData.append(key, data[key]);
      } else {
        formData.append(key, String(data[key]));
      }
    });

    mutation.mutate(formData);
  };

  return (
    <>
      <Header />
      <Styled.Root>
        <Styled.TitleWrapper>
          <HighLightedText fontSize="2.25rem">🦄 Upload Your Toy</HighLightedText>
        </Styled.TitleWrapper>

        <form onSubmit={handleSubmit(uploadFeed)}>
          <Styled.VerticalWrapper>
            <Label text="제목" required={true} />
            <FormInput {...register('title', { required: true })} />
          </Styled.VerticalWrapper>

          <Styled.VerticalWrapper>
            <Label text="사용 스택" required={true} />
            <FormInput {...register('techs')} />
          </Styled.VerticalWrapper>

          <Styled.VerticalWrapper>
            <Label text="내용" required={true} />
            <ContentTextArea {...register('content', { required: true })} />
          </Styled.VerticalWrapper>

          <Styled.InputsContainer>
            <Styled.StretchWrapper>
              <Label className="stretch-label" text="레벨" required={true} />
              <FlexContainer width="100%">
                <RadioButton
                  name="step"
                  labelText="조립중"
                  value={FeedStatus.PROGRESS}
                  {...register('step', { required: true })}
                />
                <RadioButton
                  name="step"
                  labelText="전시중"
                  value={FeedStatus.COMPLETE}
                  {...register('step', { required: true })}
                />
              </FlexContainer>
            </Styled.StretchWrapper>
            <FlexContainer>
              <Toggle labelText="SOS" {...register('sos')} />
            </FlexContainer>
          </Styled.InputsContainer>

          <Styled.StretchWrapper>
            <Label className="stretch-label" text="github" required={true} />
            <FormInput {...register('storageUrl', { required: true })} />
          </Styled.StretchWrapper>

          <Styled.StretchWrapper>
            <Label className="stretch-label" text="배포 URL" required={true} />
            <FormInput {...register('deployedUrl', { required: true })} />
          </Styled.StretchWrapper>

          <Styled.StretchWrapper>
            <Label className="stretch-label" text="대표 이미지" />
            <FileInput
              fileName={watchThumbnailImage?.name}
              onChange={(event) => setValue('thumbnailImage', event.currentTarget.files[0])}
            />
          </Styled.StretchWrapper>

          <Styled.ButtonsWrapper>
            <StyledButton buttonStyle={ButtonStyle.SOLID}>등록</StyledButton>
            <StyledButton type="button" buttonStyle={ButtonStyle.OUTLINE}>
              취소
            </StyledButton>
          </Styled.ButtonsWrapper>
        </form>
      </Styled.Root>
    </>
  );
};

export default Upload;
