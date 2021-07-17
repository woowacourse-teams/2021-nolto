import React, { useState } from 'react';
import { useForm } from 'react-hook-form';

import FormInput from 'components/@common/FormInput/FormInput';
import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import Label from 'components/@common/Label/Label';
import FileInput from 'components/@common/FileInput/FileInput';
import Toggle from 'components/@common/Toggle/Toggle';
import RadioButton from 'components/@common/RadioButton/RadioButton';
import Header from 'components/Header/Header';
import TechInput from 'components/TechInput/TechInput';
import useUploadFeed from 'hooks/queries/useUploadFeed';
import { FlexContainer } from 'commonStyles';
import Styled, { ContentTextArea, StyledButton } from './Upload.styles';
import { ButtonStyle, FeedStatus, Tech, FeedToUpload } from 'types';

type FeedToUploadPartial = Omit<FeedToUpload, 'techs'>;

const Upload = () => {
  const { register, handleSubmit, setValue, watch } = useForm<FeedToUploadPartial>();
  const [techs, setTechs] = useState<Tech[]>([]);
  const watchThumbnailImage = watch('thumbnailImage');
  const mutation = useUploadFeed();

  const uploadFeed = (data: FeedToUploadPartial) => {
    const formData = new FormData();

    Object.keys(data).forEach((key) => {
      if (key === 'thumbnailImage') {
        formData.append(key, data[key]);
      } else {
        formData.append(key, String(data[key as keyof typeof data]));
      }
    });

    techs.forEach((tech) => {
      formData.append('techs', String(tech.id));
    });

    mutation.mutate(formData);
  };

  return (
    <>
      <Header />
      <Styled.Root>
        <Styled.TitleWrapper>
          <HighLightedText fontSize="1.75rem">🦄 Upload Your Toy</HighLightedText>
        </Styled.TitleWrapper>

        <form onSubmit={handleSubmit(uploadFeed)}>
          <Styled.VerticalWrapper>
            <Label text="제목" required={true} />
            <FormInput {...register('title', { required: true })} />
          </Styled.VerticalWrapper>

          <Styled.VerticalWrapper>
            <Label text="사용 스택" />
            <TechInput onUpdateTechs={(techs: Tech[]) => setTechs(techs)} />
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
                  labelText="🧩 조립중"
                  value={FeedStatus.PROGRESS}
                  {...register('step', { required: true })}
                />
                <RadioButton
                  name="step"
                  labelText="🦄 전시중"
                  value={FeedStatus.COMPLETE}
                  {...register('step', { required: true })}
                />
              </FlexContainer>
            </Styled.StretchWrapper>

            <Toggle labelText="🚨 SOS" {...register('sos')} />
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
