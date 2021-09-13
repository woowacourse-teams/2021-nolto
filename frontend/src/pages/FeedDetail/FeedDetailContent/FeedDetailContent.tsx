import React, { SyntheticEvent, useEffect, useState } from 'react';
import { useHistory, useLocation } from 'react-router-dom';

import { ButtonStyle } from 'types';
import ViewCountIcon from 'assets/viewCount.svg';
import ShareIcon from 'assets/share.svg';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useFeedDetail from 'hooks/queries/feed/useFeedDetail';
import useMember from 'hooks/queries/useMember';
import { PALETTE } from 'constants/palette';
import ROUTE from 'constants/routes';
import QUERY_KEYS from 'constants/queryKeys';
import { ERROR_MSG } from 'constants/message';
import { DEFAULT_IMG } from 'constants/common';
import { Divider } from 'commonStyles';
import ToggleList from 'components/@common/ToggleList/ToggleList';
import FeedDropdown from 'components/FeedDropdown/FeedDropdown';
import LikeButton from 'components/LikeButton/LikeButton';
import CommentModule from 'components/CommentModule/CommentModule';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import StepChip from 'components/StepChip/StepChip';
import Styled, { Tag, SOSFlag } from './FeedDetailContent.styles';

interface Props {
  feedId: number;
}

const FeedDetailContent = ({ feedId }: Props) => {
  const [isKakaoLoaded, setKakaoLoaded] = useState(false);

  const history = useHistory();
  const location = useLocation<{ commentId: number }>();

  const snackbar = useSnackbar();
  const member = useMember();

  const { data: feedDetail } = useFeedDetail({
    errorHandler: (error) => {
      snackbar.addSnackbar('error', error.message);
    },
    feedId,
  });

  const searchByTag = (tech: string) => {
    const queryParams = new URLSearchParams({
      query: '',
      techs: tech,
    });

    history.push({
      pathname: ROUTE.SEARCH,
      search: '?' + queryParams,
    });
  };

  const createKakaoShare = () => {
    window.Kakao.Link.createDefaultButton({
      container: '#create-kakao-link-btn',
      objectType: 'feed',
      content: {
        title: feedDetail.title,
        description: '🧸 놀토에서 친구가 공유한 프로젝트를 확인해 보세요!',
        imageUrl: feedDetail.thumbnailUrl,
        link: {
          mobileWebUrl: window.location.href,
          webUrl: window.location.href,
        },
      },
      buttons: [
        {
          title: '프로젝트 구경가기',
          link: {
            mobileWebUrl: window.location.href,
            webUrl: window.location.href,
          },
        },
      ],
    });
  };

  const isMyFeed = member.userData?.id === feedDetail.author.id;

  const thumbnailElement: React.ReactNode = (
    <>
      {feedDetail.sos && <SOSFlag />}
      <Styled.Thumbnail>
        <img
          src={feedDetail.thumbnailUrl}
          onError={(event: SyntheticEvent<HTMLImageElement>) => {
            event.currentTarget.src = DEFAULT_IMG.FEED;
          }}
        />
      </Styled.Thumbnail>
      <Styled.IconsContainer>
        <Styled.IconWrapper>
          <LikeButton feedDetail={feedDetail} />
        </Styled.IconWrapper>
        <Styled.IconWrapper>
          <ViewCountIcon width="22px" fill={PALETTE.PRIMARY_400} />
          <span>{feedDetail.views}</span>
        </Styled.IconWrapper>
      </Styled.IconsContainer>
    </>
  );

  const handleKakaoShare = () => {
    if (isKakaoLoaded) return;

    snackbar.addSnackbar('error', ERROR_MSG.FAIL_KAKAO_SHARE);
  };

  useEffect(() => {
    if (process.env.KAKAO_API_KEY) {
      window.Kakao.init(process.env.KAKAO_API_KEY);
      createKakaoShare();
      setKakaoLoaded(true);
    }
  }, []);

  // TODO: 댓글 로딩 부분 스켈레톤으로 리팩토링
  return (
    <Styled.Root>
      <Styled.IntroContainer>
        <Styled.ThumbnailContainer>{thumbnailElement}</Styled.ThumbnailContainer>
        <Styled.FeedSummaryContainer>
          <Styled.TitleContainer>
            <Styled.TitleWrapper>
              <h2>{feedDetail.title}</h2>
              <StepChip step={feedDetail.step} />
              <ShareIcon width="20px" />
              <a id="create-kakao-link-btn" onClick={handleKakaoShare}>
                <img
                  src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_small.png"
                  width="24px"
                />
              </a>
            </Styled.TitleWrapper>

            <Styled.UserWrapper>
              <Styled.UserName>{feedDetail.author.nickname}</Styled.UserName>
              <Styled.UserImage src={feedDetail.author.imageUrl} />
              {isMyFeed && <FeedDropdown feedDetail={feedDetail} />}
            </Styled.UserWrapper>
          </Styled.TitleContainer>
          <Divider />

          <Styled.MobileThumbnailContainer>{thumbnailElement}</Styled.MobileThumbnailContainer>
          <Styled.DetailsContent>
            {feedDetail.deployedUrl && (
              <Styled.DetailsPair>
                <Styled.DetailsKeyWrapper>
                  <Styled.DetailsKey>서비스 URL</Styled.DetailsKey>
                </Styled.DetailsKeyWrapper>
                <Styled.DetailsValue>
                  <a href={feedDetail.deployedUrl} target="_blank">
                    {feedDetail.deployedUrl}
                  </a>
                </Styled.DetailsValue>
              </Styled.DetailsPair>
            )}
            {feedDetail.storageUrl && (
              <Styled.DetailsPair>
                <Styled.DetailsKeyWrapper>
                  <Styled.DetailsKey>저장소 URL</Styled.DetailsKey>
                </Styled.DetailsKeyWrapper>
                <Styled.DetailsValue>
                  <a href={feedDetail.storageUrl} target="_blank">
                    {feedDetail.storageUrl}
                  </a>
                </Styled.DetailsValue>
              </Styled.DetailsPair>
            )}
            {feedDetail.techs.length > 0 && (
              <Styled.DetailsPair>
                <Styled.DetailsKeyWrapper>
                  <Styled.DetailsKey>기술스택</Styled.DetailsKey>
                </Styled.DetailsKeyWrapper>
                <Styled.DetailsValue>
                  <ToggleList width="100%" height="1.75rem">
                    {feedDetail.techs.map((tech) => (
                      <li key={tech.id}>
                        <Tag buttonStyle={ButtonStyle.SOLID} onClick={() => searchByTag(tech.text)}>
                          {tech.text}
                        </Tag>
                      </li>
                    ))}
                  </ToggleList>
                </Styled.DetailsValue>
              </Styled.DetailsPair>
            )}
          </Styled.DetailsContent>
        </Styled.FeedSummaryContainer>
      </Styled.IntroContainer>
      <div>
        <h3>프로젝트 소개</h3>
        <Divider />
        <Styled.Description>{feedDetail.content}</Styled.Description>
      </div>

      <AsyncBoundary
        rejectedFallback={
          <ErrorFallback message={ERROR_MSG.LOAD_COMMENTS} queryKey={QUERY_KEYS.COMMENTS} />
        }
      >
        <CommentModule feedId={feedDetail.id} focusedCommentId={location.state?.commentId} />
      </AsyncBoundary>
    </Styled.Root>
  );
};

export default FeedDetailContent;
