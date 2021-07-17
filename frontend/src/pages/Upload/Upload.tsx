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
import { ALERT_MSG, CONFIRM_MSG, UPLOAD_VALIDATION_MSG } from 'constants/message';

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
        alert(ALERT_MSG.SUCCESS_UPLOAD_FEED);
        history.push(ROUTE.HOME);
      },
    });
  };

  const handleCancelUpload = () => {
    if (!confirm(CONFIRM_MSG.LEAVE_UPLOAD_PAGE)) {
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
                required: UPLOAD_VALIDATION_MSG.TITLE_REQUIRED,
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
              {...register('content', { required: UPLOAD_VALIDATION_MSG.CONTENT_REQUIRED })}
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
                    {...register('step', { required: UPLOAD_VALIDATION_MSG.STEP_REQUIRED })}
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
                    required: UPLOAD_VALIDATION_MSG.DEPLOY_URL_REQUIRED,
                    pattern: {
                      value:
                        /https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)/,
                      message: UPLOAD_VALIDATION_MSG.INVALID_URL,
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
                    message: UPLOAD_VALIDATION_MSG.INVALID_URL,
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
