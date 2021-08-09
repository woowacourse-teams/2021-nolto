import React from 'react';
import { useHistory } from 'react-router-dom';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import FeedUploadForm from 'components/FeedUploadForm/FeedUploadForm';
import Header from 'components/Header/Header';
import useFeedUpload from 'hooks/mutations/useFeedUpload';
import useSnackBar from 'context/snackBar/useSnackBar';
import { ALERT_MSG } from 'constants/message';
import ROUTE from 'constants/routes';
import Styled from './Upload.styles';

const Upload = () => {
  const uploadMutation = useFeedUpload();
  const history = useHistory();
  const snackbar = useSnackBar();

  const uploadFeed = (formData: FormData) => {
    uploadMutation.mutate(formData, {
      onSuccess: () => {
        snackbar.addSnackBar('success', ALERT_MSG.SUCCESS_UPLOAD_FEED);
        history.push(ROUTE.HOME);
      },
    });
  };

  return (
    <>
      <Header />
      <Styled.Root>
        <Styled.TitleWrapper>
          <HighLightedText fontSize="1.75rem">🦄 Upload Your Toy</HighLightedText>
        </Styled.TitleWrapper>

        <FeedUploadForm onFeedSubmit={uploadFeed} />
      </Styled.Root>
    </>
  );
};

export default Upload;
