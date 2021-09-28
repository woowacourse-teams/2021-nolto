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
import { THUMBNAIL_EXTENSION } from 'constants/common';
import { CONFIRM_MSG, UPLOAD_VALIDATION_MSG } from 'constants/message';
import TechInput from 'contexts/techTag/input/TechInput';
import TechTagProvider from 'contexts/techTag/TechTagProvider';
import TechChip from 'contexts/techTag/chip/TechChips';
import useDialog from 'contexts/dialog/useDialog';
import { except } from 'utils/common';
import QuestionIcon from 'assets/questionMark.svg';
import { ButtonStyle, FeedStep, Tech, FeedToUpload } from 'types';
import Styled, {
  ContentTextArea,
  Form,
  StyledButton,
  LevelTooltip,
  SOSTooltip,
  Toybox,
} from './FeedUploadForm.styles';
import Markdown from 'components/@common/Markdown/Markdown';

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
  const [isLevelTooltipVisible, setIsLevelTooltipVisible] = useState(false);
  const [isSOSTooltipVisible, setIsSOSTooltipVisible] = useState(false);

  const watchThumbnailImage = watch('thumbnailImage');
  const watchStep = watch('step');
  const watchContent = watch('content');
  const history = useHistory();
  const dialog = useDialog();

  const setFileInput = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (!THUMBNAIL_EXTENSION.includes(event.currentTarget.files[0].type)) {
      dialog.alert('잘못된 확장자입니다.');

      return;
    }

    setValue('thumbnailImage', event.currentTarget.files[0]);
  };

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
    dialog.confirm(CONFIRM_MSG.LEAVE_UPLOAD_PAGE, () => {
      history.goBack();
    });
  };

  return (
    <Form onSubmit={handleSubmit(submitFeed)}>
      <Styled.FormContainer>
        <Styled.VerticalWrapper>
          <Label text="제목" htmlFor="title" required={true} />
          <FormInput
            id="title"
            {...register('title', {
              required: UPLOAD_VALIDATION_MSG.TITLE_REQUIRED,
            })}
          />
          <ErrorMessage targetError={errors.title} />
        </Styled.VerticalWrapper>

        <Styled.VerticalWrapper>
          <Label text="사용 스택" htmlFor="techs" />
          <TechTagProvider initialTechs={techs}>
            <TechChip />
            <TechInput id="techs" onUpdateTechs={(techs: Tech[]) => setTechs(techs)} />
          </TechTagProvider>
        </Styled.VerticalWrapper>

        <Styled.VerticalWrapper>
          <Label text="내용" htmlFor="content" required={true} />
          <Styled.MarkdownContainer>
            <Toybox width="32px" />
            <ContentTextArea
              id="content"
              {...register('content', { required: UPLOAD_VALIDATION_MSG.CONTENT_REQUIRED })}
            />
            <Styled.MarkdownWrapper>
              <Markdown children={watchContent} />
            </Styled.MarkdownWrapper>
          </Styled.MarkdownContainer>
          <ErrorMessage targetError={errors.content} />
        </Styled.VerticalWrapper>

        <div>
          <Styled.InputsContainer>
            <Styled.LevelWrapper>
              <Label text="레벨" required={true} />
              <span
                onMouseOver={() => setIsLevelTooltipVisible(true)}
                onMouseOut={() => setIsLevelTooltipVisible(false)}
              >
                <QuestionIcon width="20px" />
              </span>
              <LevelTooltip visible={isLevelTooltipVisible}>
                <pre>
                  <strong>프로젝트 단계</strong> <br />
                  <br />
                  🧩 진행중: 프로젝트가 완성되지 않았어요 <br />
                  🦄 전시중: 프로젝트가 완성됐어요
                </pre>
              </LevelTooltip>
            </Styled.LevelWrapper>
            <FlexContainer>
              <RadioButton
                name="step"
                labelText="🧩 진행중"
                value={FeedStep.PROGRESS}
                {...register('step', { required: UPLOAD_VALIDATION_MSG.STEP_REQUIRED })}
              />
              <RadioButton
                name="step"
                labelText="🦄 전시중"
                value={FeedStep.COMPLETE}
                {...register('step')}
              />
            </FlexContainer>

            <Styled.SOSLabel>
              <SOSTooltip visible={isSOSTooltipVisible}>
                <pre>
                  프로젝트를 완성하는 데<br /> 도움이 필요하신가요?
                </pre>
              </SOSTooltip>
              <span
                onMouseOver={() => setIsSOSTooltipVisible(true)}
                onMouseOut={() => setIsSOSTooltipVisible(false)}
              >
                <QuestionIcon width="20px" />
              </span>
              <Toggle labelText="🚨 SOS" {...register('sos')} />
            </Styled.SOSLabel>
          </Styled.InputsContainer>
          <ErrorMessage targetError={errors.step} />
        </div>

        {watchStep === FeedStep.COMPLETE && (
          <div>
            <Styled.StretchWrapper>
              <Label
                className="stretch-label"
                htmlFor="deployed-url"
                text="배포 URL"
                required={true}
              />
              <div className="input-box">
                <FormInput
                  id="deployed-url"
                  {...register('deployedUrl', {
                    required: UPLOAD_VALIDATION_MSG.DEPLOY_URL_REQUIRED,
                    pattern: {
                      value: REGEX.URL,
                      message: UPLOAD_VALIDATION_MSG.INVALID_URL,
                    },
                  })}
                />
                <Styled.InputCaption>
                  http:// 또는 https://의 형태로 입력해주세요
                </Styled.InputCaption>
              </div>
            </Styled.StretchWrapper>
            <ErrorMessage targetError={errors.deployedUrl} />
          </div>
        )}
        <div>
          <Styled.StretchWrapper>
            <Label className="stretch-label" htmlFor="github-url" text="github URL" />
            <div className="input-box">
              <FormInput
                id="github-url"
                {...register('storageUrl', {
                  pattern: {
                    value: REGEX.URL,
                    message: UPLOAD_VALIDATION_MSG.INVALID_URL,
                  },
                })}
              />
              <Styled.InputCaption>http:// 또는 https://의 형태로 입력해주세요</Styled.InputCaption>
            </div>
          </Styled.StretchWrapper>
          <ErrorMessage targetError={errors.storageUrl} />
        </div>

        <Styled.StretchWrapper>
          <Label className="stretch-label" htmlFor="thumbnail-image" text="대표 이미지" />
          <div>
            <FileInput
              id="thumbnail-image"
              fileName={watchThumbnailImage?.name}
              onChange={setFileInput}
              accept={THUMBNAIL_EXTENSION.join(',')}
            />
            <Styled.InputCaption>최대 10MB의 이미지를 업로드할 수 있습니다</Styled.InputCaption>
          </div>
        </Styled.StretchWrapper>
      </Styled.FormContainer>

      <Styled.ButtonsWrapper>
        <StyledButton buttonStyle={ButtonStyle.SOLID}>
          {initialFormValue ? '수정' : '등록'}
        </StyledButton>
        <StyledButton onClick={handleCancelUpload} type="button" buttonStyle={ButtonStyle.OUTLINE}>
          취소
        </StyledButton>
      </Styled.ButtonsWrapper>
    </Form>
  );
};

export default FeedUploadForm;
