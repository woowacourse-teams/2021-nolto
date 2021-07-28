import React from 'react';
import { useHistory } from 'react-router-dom';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import FeedUploadForm from 'components/FeedUploadForm/FeedUploadForm';
import Header from 'components/Header/Header';
import Styled from './Upload.styles';
import useFeedUpload from 'hooks/mutations/useFeedUpload';
import { ALERT_MSG } from 'constants/message';
import ROUTE from 'constants/routes';
import useNotification from 'context/notification/useNotification';

const Upload = () => {
  const uploadMutation = useFeedUpload();
  const notification = useNotification();
  const history = useHistory();

  const uploadFeed = (formData: FormData) => {
    uploadMutation.mutate(formData, {
      onSuccess: () => {
        notification.alert(ALERT_MSG.SUCCESS_UPLOAD_FEED);
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
