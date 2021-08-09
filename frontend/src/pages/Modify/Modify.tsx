import React from 'react';
import { useHistory, useLocation } from 'react-router-dom';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import FeedUploadForm from 'components/FeedUploadForm/FeedUploadForm';
import Header from 'components/Header/Header';
import ROUTE from 'constants/routes';
import { ALERT_MSG } from 'constants/message';
import useDialog from 'context/dialog/useDialog';
import useSnackBar from 'context/snackBar/useSnackBar';
import useFeedModify from 'hooks/mutations/useFeedModify';
import Styled from './Modify.styles';
import { FeedDetail } from 'types';

const Modify = () => {
  const location = useLocation<{ feedDetail: FeedDetail }>();
  const history = useHistory();

  const dialog = useDialog();
  const snackbar = useSnackBar();

  const modifyMutation = useFeedModify();

  if (location.state?.feedDetail === undefined) {
    dialog.alert('잘못된 접근입니다.');
    history.push(ROUTE.HOME);

    return null;
  }

  const {
    id: feedId,
    title,
    content,
    sos,
    step,
    techs,
    deployedUrl,
    storageUrl,
  } = location.state.feedDetail;

  const modifyFeed = (formData: FormData) => {
    modifyMutation.mutate(
      { feedId, formData },
      {
        onSuccess: () => {
          snackbar.addSnackBar('success', ALERT_MSG.SUCCESS_MODIFY_FEED);

          history.push(`${ROUTE.FEEDS}/${feedId}`);
        },
      },
    );
  };

  return (
    <>
      <Header />
      <Styled.Root>
        <Styled.TitleWrapper>
          <HighLightedText fontSize="1.75rem">🔧 Modify Your Toy</HighLightedText>
        </Styled.TitleWrapper>

        <FeedUploadForm
          onFeedSubmit={modifyFeed}
          initialFormValue={{
            title,
            content,
            sos,
            step,
            techs,
            deployedUrl,
            storageUrl,
          }}
        />
      </Styled.Root>
    </>
  );
};

export default Modify;
