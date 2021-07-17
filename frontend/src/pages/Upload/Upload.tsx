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
import Styled, { ContentTextArea, Form, StyledButton } from './Upload.styles';
import { ButtonStyle, FeedStatus, Tech, FeedToUpload } from 'types';
import ErrorMessage from 'components/@common/ErrorMessage/ErrorMessage';
import { useHistory } from 'react-router-dom';
import ROUTE from 'constants/routes';

type FeedToUploadPartial = Omit<FeedToUpload, 'techs'>;

const Upload = () => {
  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm<FeedToUploadPartial>({
    shouldUnregister: true,
  });
  const [techs, setTechs] = useState<Tech[]>([]);
  const watchThumbnailImage = watch('thumbnailImage');
  const watchStep = watch('step');
  const uploadMutation = useUploadFeed();
  const history = useHistory();

  const uploadFeed = (data: FeedToUploadPartial) => {
    const formData = new FormData();

    Object.keys(data).forEach((key: keyof typeof data) => {
      if (!data[key]) {
        return;
      }

      if (key === 'thumbnailImage') {
        formData.append(key, data[key]);
      } else {
        formData.append(key, String(data[key]));
      }
    });

    techs.forEach((tech) => {
      formData.append('techs', String(tech.id));
    });

    uploadMutation.mutate(formData, {
      onSuccess: () => {
        alert('🎉 토이 프로젝트 등록에 성공했습니다!');
        history.push(ROUTE.HOME);
      },
    });
  };

  const handleCancelUpload = () => {
    if (!confirm('정말로 페이지를 떠나시겠습니까? 작성 중인 정보는 사라집니다.')) {
      return;
    }

    history.goBack();
  };

  return (
    <>
      <Header />
      <Styled.Root>
        <Styled.TitleWrapper>
          <HighLightedText fontSize="1.75rem">🦄 Upload Your Toy</HighLightedText>
        </Styled.TitleWrapper>

        <Form onSubmit={handleSubmit(uploadFeed)}>
          <Styled.VerticalWrapper>
            <Label text="제목" required={true} />
            <FormInput
              {...register('title', {
                required: '😭 프로젝트 이름을 알려주세요!',
              })}
            />
            <ErrorMessage targetError={errors.title} />
          </Styled.VerticalWrapper>

          <Styled.VerticalWrapper>
            <Label text="사용 스택" />
            <TechInput onUpdateTechs={(techs: Tech[]) => setTechs(techs)} />
          </Styled.VerticalWrapper>

          <Styled.VerticalWrapper>
            <Label text="내용" required={true} />
            <ContentTextArea
              {...register('content', { required: '😁 프로젝트를 소개해주세요!' })}
            />
            <ErrorMessage targetError={errors.content} />
          </Styled.VerticalWrapper>

          <div>
            <Styled.InputsContainer>
              <Styled.levelWrapper>
                <Label className="stretch-label" text="레벨" required={true} />
                <FlexContainer>
                  <RadioButton
                    name="step"
                    labelText="🧩 조립중"
                    value={FeedStatus.PROGRESS}
                    {...register('step', { required: '🙋‍♂️ 프로젝트의 완성도는 어느 정도인가요?' })}
                  />
                  <RadioButton
                    name="step"
                    labelText="🦄 전시중"
                    value={FeedStatus.COMPLETE}
                    {...register('step')}
                  />
                </FlexContainer>
              </Styled.levelWrapper>

              <Toggle labelText="🚨 SOS" {...register('sos')} />
            </Styled.InputsContainer>
            <ErrorMessage targetError={errors.step} />
          </div>

          {watchStep === FeedStatus.COMPLETE && (
            <div>
              <Styled.StretchWrapper>
                <Label className="stretch-label" text="배포 URL" required={true} />
                <FormInput
                  {...register('deployedUrl', {
                    required: '😎 전시중 프로젝트는 배포 URL이 필수예요!',
                    pattern: {
                      value:
                        /https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)/,
                      message: '🧡 올바른 url 형식을 사용해주세요!',
                    },
                  })}
                />
              </Styled.StretchWrapper>
              <ErrorMessage targetError={errors.deployedUrl} />
            </div>
          )}
          <div>
            <Styled.StretchWrapper>
              <Label className="stretch-label" text="github URL" />
              <FormInput
                {...register('storageUrl', {
                  pattern: {
                    value:
                      /https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)/,
                    message: '🧡 올바른 url 형식을 사용해주세요!',
                  },
                })}
              />
            </Styled.StretchWrapper>
            <ErrorMessage targetError={errors.storageUrl} />
          </div>

          <Styled.StretchWrapper>
            <Label className="stretch-label" text="대표 이미지" />
            <FileInput
              fileName={watchThumbnailImage?.name}
              onChange={(event) => setValue('thumbnailImage', event.currentTarget.files[0])}
            />
          </Styled.StretchWrapper>

          <Styled.ButtonsWrapper>
            <StyledButton buttonStyle={ButtonStyle.SOLID}>등록</StyledButton>
            <StyledButton
              onClick={handleCancelUpload}
              type="button"
              buttonStyle={ButtonStyle.OUTLINE}
            >
              취소
            </StyledButton>
          </Styled.ButtonsWrapper>
        </Form>
      </Styled.Root>
    </>
  );
};

export default Upload;
