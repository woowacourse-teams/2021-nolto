import React from 'react';
import { useHistory } from 'react-router-dom';

import useFeedDetail from 'hooks/queries/feed/useFeedDetail';
import ViewCountIcon from 'assets/viewCount.svg';
import Chip from 'components/@common/Chip/Chip';
import FeedDropdown from 'components/FeedDropdown/FeedDropdown';
import LikeButton from 'components/LikeButton/LikeButton';
import { STEP_CONVERTER } from 'constants/common';
import { PALETTE } from 'constants/palette';
import ROUTE from 'constants/routes';
import { ButtonStyle } from 'types';
import useSnackBar from 'contexts/snackBar/useSnackBar';
import useMember from 'hooks/queries/useMember';
import Styled, { Tag } from './FeedDetailContent.styles';
import ToggleList from 'components/@common/ToggleList/ToggleList';
import CommentModule from 'components/CommentModule/CommentModule';

interface Props {
  feedId: number;
}

const FeedDetailContent = ({ feedId }: Props) => {
  const history = useHistory();
  const snackBar = useSnackBar();
  const member = useMember();

  const { data: feedDetail } = useFeedDetail({
    errorHandler: (error) => {
      snackBar.addSnackBar('error', error.message);
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

  const isMyFeed = member.userData?.id === feedDetail.author.id;

  return (
    <Styled.Root>
      <Styled.IntroContainer>
        <Styled.ThumbnailContainer>
          <Styled.Thumbnail>
            <img src={feedDetail.thumbnailUrl} />
          </Styled.Thumbnail>
          {feedDetail.sos && <Styled.SOSFlagIcon width="56px" />}

          <Styled.IconsContainer>
            <Styled.IconWrapper>
              <LikeButton feedDetail={feedDetail} />
            </Styled.IconWrapper>
            <Styled.IconWrapper>
              <ViewCountIcon width="22px" fill={PALETTE.PRIMARY_200} />
              <span>{feedDetail.views}</span>
            </Styled.IconWrapper>
          </Styled.IconsContainer>
        </Styled.ThumbnailContainer>

        <Styled.FeedSummaryContainer>
          <Styled.TitleContainer>
            <Styled.TitleWrapper>
              <h2>{feedDetail.title}</h2>
              <Chip.Solid>{STEP_CONVERTER[feedDetail.step]}</Chip.Solid>
            </Styled.TitleWrapper>

            <Styled.UserWrapper>
              <Styled.UserName>{feedDetail.author.nickname}</Styled.UserName>
              <Styled.UserImage src={feedDetail.author.imageUrl} />
              {isMyFeed && <FeedDropdown feedDetail={feedDetail} />}
            </Styled.UserWrapper>
          </Styled.TitleContainer>
          <hr />

          <Styled.DetailsContent>
            {feedDetail.deployedUrl && (
              <Styled.DetailsPair>
                <Styled.DetailsKeyWrapper>
                  <Styled.DetailsKey fontSize="1.5rem">서비스 URL</Styled.DetailsKey>
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
                  <Styled.DetailsKey fontSize="1.5rem">저장소 URL</Styled.DetailsKey>
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
                  <Styled.DetailsKey fontSize="1.5rem">기술스택</Styled.DetailsKey>
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
        <hr />
        <Styled.Description>{feedDetail.content}</Styled.Description>
      </div>

      <CommentModule feedId={feedDetail.id} />
    </Styled.Root>
  );
};

export default FeedDetailContent;
