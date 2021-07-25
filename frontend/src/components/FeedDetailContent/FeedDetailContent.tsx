import useFeedDetail from 'hooks/queries/useFeedDetail';
import React from 'react';

import Styled, { Tag, StacksMoreButton } from './FeedDetailContent.styles';
import LikeHeartIcon from 'assets/likeHeart.svg';
import ViewCountIcon from 'assets/viewCount.svg';
import Chip from 'components/@common/Chip/Chip';
import { ButtonStyle } from 'types';
import { STEP_CONVERTER } from 'constants/common';
import FeedDropdown from 'components/FeedDropdown/FeedDropdown';

interface Props {
  id: number;
}

const FeedDetailContent = ({ id }: Props) => {
  const { data: feedDetail } = useFeedDetail(id, {
    onError: (error) => {
      alert(error.message);
    },
  });

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
              <LikeHeartIcon width="24px" />
              <span>{feedDetail.likes}</span>
            </Styled.IconWrapper>
            <Styled.IconWrapper>
              <ViewCountIcon width="22px" />
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
              <FeedDropdown feedId={id} />
            </Styled.UserWrapper>
          </Styled.TitleContainer>
          <hr />

          <Styled.DetailsContent>
            {feedDetail.deployedUrl && (
              <Styled.DetailsPair>
                <Styled.DetailsKey fontSize="1.5rem">서비스 URL</Styled.DetailsKey>
                <Styled.DetailsValue>
                  <a href={feedDetail.deployedUrl} target="_blank">
                    {feedDetail.deployedUrl}
                  </a>
                </Styled.DetailsValue>
              </Styled.DetailsPair>
            )}
            {feedDetail.storageUrl && (
              <Styled.DetailsPair>
                <Styled.DetailsKey fontSize="1.5rem">저장소 URL</Styled.DetailsKey>
                <Styled.DetailsValue>
                  <a href={feedDetail.storageUrl} target="_blank">
                    {feedDetail.storageUrl}
                  </a>
                </Styled.DetailsValue>
              </Styled.DetailsPair>
            )}
            <Styled.DetailsPair>
              <Styled.DetailsKey fontSize="1.5rem">기술스택</Styled.DetailsKey>
              <Styled.DetailsValue>
                {feedDetail.techs.map((tech) => (
                  <li key={tech.id}>
                    <Tag buttonStyle={ButtonStyle.SOLID}>{tech.text}</Tag>
                  </li>
                ))}
                <StacksMoreButton width="24px" />
              </Styled.DetailsValue>
            </Styled.DetailsPair>
          </Styled.DetailsContent>
        </Styled.FeedSummaryContainer>
      </Styled.IntroContainer>

      <Styled.DotsDivider width="52px" />

      <Styled.Description>{feedDetail.content}</Styled.Description>
    </Styled.Root>
  );
};

export default FeedDetailContent;
