import React from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { Helmet } from 'react-helmet-async';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import FeedUploadForm from 'components/FeedUploadForm/FeedUploadForm';
import BaseLayout from 'components/BaseLayout/BaseLayout';
import ROUTE from 'constants/routes';
import { FONT_SIZE } from 'constants/styles';
import { ALERT_MSG } from 'constants/message';
import useDialog from 'contexts/dialog/useDialog';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useFeedModify from 'hooks/queries/feed/useFeedModify';
import { FeedDetail } from 'types';
import Styled from './Modify.styles';

const Modify = () => {
  const location = useLocation<{ feedDetail: FeedDetail }>();
  const history = useHistory();

  const dialog = useDialog();
  const snackbar = useSnackbar();

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
          snackbar.addSnackbar('success', ALERT_MSG.SUCCESS_MODIFY_FEED);

          history.push(`${ROUTE.FEEDS}/${feedId}`);
        },
      },
    );
  };

  return (
    <BaseLayout>
      <Helmet>
        <title>놀토: 토이 프로젝트 수정</title>
      </Helmet>
      <Styled.Root>
        <Styled.TitleWrapper>
          <HighLightedText fontSize={FONT_SIZE.X_LARGE}>🔧 토이 프로젝트 수정</HighLightedText>
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
    </BaseLayout>
  );
};

export default Modify;
