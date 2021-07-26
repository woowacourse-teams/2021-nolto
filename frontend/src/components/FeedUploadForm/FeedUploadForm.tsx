import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useHistory } from 'react-router-dom';

import FormInput from 'components/@common/FormInput/FormInput';
import Label from 'components/@common/Label/Label';
import FileInput from 'components/@common/FileInput/FileInput';
import Toggle from 'components/@common/Toggle/Toggle';
import RadioButton from 'components/@common/RadioButton/RadioButton';
import ErrorMessage from 'components/@common/ErrorMessage/ErrorMessage';
import { FlexContainer } from 'commonStyles';
import REGEX from 'constants/regex';
import { CONFIRM_MSG, UPLOAD_VALIDATION_MSG } from 'constants/message';
import Styled, { ContentTextArea, Form, StyledButton } from './FeedUploadForm.styles';
import { ButtonStyle, FeedStatus, Tech, FeedToUpload } from 'types';
import TechInput from 'context/techTag/input/TechInput';
import TechTagProvider from 'context/techTag/TechTagProvider';
import TechChip from 'context/techTag/chip/TechChips';
import { except } from 'utils/common';
import useNotification from 'context/notification/useNotification';

type FeedToUploadPartial = Omit<FeedToUpload, 'techs'>;

interface Props {
  onFeedSubmit: (formData: FormData) => void;
  initialFormValue?: Omit<FeedToUpload, 'thumbnailImage'>;
}

const FeedUploadForm = ({ onFeedSubmit, initialFormValue }: Props) => {
  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm<FeedToUploadPartial>({
    shouldUnregister: true,
    defaultValues: initialFormValue && except(initialFormValue, ['techs']),
  });

  const [techs, setTechs] = useState<Tech[]>(initialFormValue?.techs || []);
  const watchThumbnailImage = watch('thumbnailImage');
  const watchStep = watch('step');
  const history = useHistory();
  const notification = useNotification();

  const submitFeed = (data: FeedToUploadPartial) => {
    const formData = new FormData();

    Object.keys(data).forEach((key: keyof typeof data) => {
      if (!data[key]) return;

      if (key === 'thumbnailImage') {
        formData.append(key, data[key]);
      } else {
        formData.append(key, String(data[key]));
      }
    });

    techs.forEach((tech) => {
      formData.append('techs', String(tech.id));
    });

    onFeedSubmit(formData);
  };

  const handleCancelUpload = () => {
    notification.confirm(CONFIRM_MSG.LEAVE_UPLOAD_PAGE, () => {
      history.goBack();
    });
  };

  return (
    <Form onSubmit={handleSubmit(submitFeed)}>
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
        <TechTagProvider initialTechs={techs}>
          <TechChip />
          <TechInput onUpdateTechs={(techs: Tech[]) => setTechs(techs)} />
        </TechTagProvider>
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
          <Styled.LevelWrapper>
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
          </Styled.LevelWrapper>

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
                  value: REGEX.URL,
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
                value: REGEX.URL,
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
        <StyledButton onClick={handleCancelUpload} type="button" buttonStyle={ButtonStyle.OUTLINE}>
          취소
        </StyledButton>
      </Styled.ButtonsWrapper>
    </Form>
  );
};

export default FeedUploadForm;
