import React from 'react';
import { useHistory } from 'react-router-dom';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import FeedUploadForm from 'components/FeedUploadForm/FeedUploadForm';
import Header from 'components/Header/Header';
import useFeedUpload from 'hooks/queries/feed/useFeedUpload';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import { ALERT_MSG } from 'constants/message';
import ROUTE from 'constants/routes';
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
    <>
      <Header />
      <Styled.Root>
        <Styled.TitleWrapper>
          <HighLightedText fontSize="1.75rem">🦄 Upload Your Toy Project</HighLightedText>
        </Styled.TitleWrapper>

        <FeedUploadForm onFeedSubmit={uploadFeed} />
      </Styled.Root>
    </>
  );
};

export default Upload;
