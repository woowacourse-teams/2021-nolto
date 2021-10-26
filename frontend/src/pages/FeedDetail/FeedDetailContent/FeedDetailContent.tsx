import React, { useEffect, useState } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { Helmet } from 'react-helmet-async';

import { ButtonStyle } from 'types';
import ViewCountIcon from 'assets/viewCount.svg';
import ShareIcon from 'assets/share.svg';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useFeedDetail from 'hooks/queries/feed/useFeedDetail';
import useMember from 'contexts/member/useMember';
import hasWindow from 'constants/windowDetector';
import { PALETTE } from 'constants/palette';
import ROUTE from 'constants/routes';
import QUERY_KEYS from 'constants/queryKeys';
import { ERROR_MSG } from 'constants/message';
import { Divider } from 'commonStyles';
import FeedDropdown from 'components/FeedDropdown/FeedDropdown';
import LikeButton from 'components/LikeButton/LikeButton';
import CommentModule from 'components/CommentModule/CommentModule';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import StepChip from 'components/StepChip/StepChip';
import Thumbnail from 'components/Thumbnail/Thumbnail';
import Markdown from 'components/@common/Markdown/Markdown';
import Styled, { Tag, SOSFlag } from './FeedDetailContent.styles';
import { removeMarkdown } from 'utils/common';

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
    if (!hasWindow) return;

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

  const isMyFeed = member.userInfo?.id === feedDetail.author.id;

  const thumbnailElement: React.ReactNode = (
    <>
      {feedDetail.sos && <SOSFlag />}
      <Styled.ThumbnailWrapper>
        <Thumbnail thumbnailUrl={feedDetail.thumbnailUrl} alt={`${feedDetail.content} 이미지`} />
      </Styled.ThumbnailWrapper>
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
    if (hasWindow && window.Kakao.isInitialized()) {
      if (!isKakaoLoaded) {
        createKakaoShare();
        setKakaoLoaded(true);
      }
    }

    return () => setKakaoLoaded(false);
  }, []);

  // TODO: 댓글 로딩 부분 스켈레톤으로 리팩토링
  return (
    <Styled.Root>
      <Helmet>
        <title>놀토: 토이프로젝트 - {feedDetail.title}</title>
        <link rel="canonical" href="https://www.nolto.app/feeds" />
        <meta
          name="description"
          content={removeMarkdown(feedDetail.content).replace(/\n+/g, ' ')}
        />
      </Helmet>
      <Styled.IntroContainer>
        <Styled.ThumbnailContainer>{thumbnailElement}</Styled.ThumbnailContainer>
        <Styled.FeedSummaryContainer>
          <Styled.TitleContainer>
            <Styled.TitleWrapper>
              <h2>{feedDetail.title}</h2>
              <StepChip className="step-chip" step={feedDetail.step} />
              <ShareIcon width="20px" />
              <a id="create-kakao-link-btn" onClick={handleKakaoShare}>
                <img
                  src="https://developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_small.png"
                  width="24px"
                  alt="카카오톡 공유"
                />
              </a>
            </Styled.TitleWrapper>

            <Styled.UserWrapper>
              <Styled.UserName>{feedDetail.author.nickname}</Styled.UserName>
              <Styled.UserThumbnail
                thumbnailUrl={feedDetail.author.imageUrl}
                alt={feedDetail.author.nickname}
              />
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
                  <ul>
                    {feedDetail.techs.map((tech) => (
                      <li key={tech.id}>
                        <Tag buttonStyle={ButtonStyle.SOLID} onClick={() => searchByTag(tech.text)}>
                          {tech.text}
                        </Tag>
                      </li>
                    ))}
                  </ul>
                </Styled.DetailsValue>
              </Styled.DetailsPair>
            )}
          </Styled.DetailsContent>
        </Styled.FeedSummaryContainer>
      </Styled.IntroContainer>
      <div>
        <h3>프로젝트 소개</h3>
        <Divider />
        {hasWindow && (
          <Styled.MarkdownWrapper>
            <Markdown children={feedDetail.content} />
          </Styled.MarkdownWrapper>
        )}
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
