import React from 'react';
import { useHistory } from 'react-router-dom';
import { Helmet } from 'react-helmet-async';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import FeedUploadForm from 'components/FeedUploadForm/FeedUploadForm';
import BaseLayout from 'components/BaseLayout/BaseLayout';
import useFeedUpload from 'hooks/queries/feed/useFeedUpload';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import { ALERT_MSG } from 'constants/message';
import ROUTE from 'constants/routes';
import { FONT_SIZE } from 'constants/styles';
import Styled from './Upload.styles';

const Upload = () => {
  const uploadMutation = useFeedUpload();
  const history = useHistory();
  const snackbar = useSnackbar();

  const uploadFeed = (formData: FormData) => {
    uploadMutation.mutate(formData, {
      onSuccess: () => {
        snackbar.addSnackbar('success', ALERT_MSG.SUCCESS_UPLOAD_FEED);
        history.push(ROUTE.HOME);
      },
    });
  };

  return (
    <BaseLayout>
      <Helmet>
        <title>놀토: 토이 프로젝트 등록</title>
      </Helmet>
      <Styled.Root>
        <Styled.TitleWrapper>
          <HighLightedText fontSize={FONT_SIZE.X_LARGE}>🦄 토이 프로젝트 등록</HighLightedText>
        </Styled.TitleWrapper>

        <FeedUploadForm onFeedSubmit={uploadFeed} />
      </Styled.Root>
    </BaseLayout>
  );
};

export default Upload;
