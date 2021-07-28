import React from 'react';

import Styled, { Tag, StacksMoreButton } from './FeedDetailContent.styles';
import useFeedDetail from 'hooks/queries/useFeedDetail';
import ViewCountIcon from 'assets/viewCount.svg';
import Chip from 'components/@common/Chip/Chip';
import FeedDropdown from 'components/FeedDropdown/FeedDropdown';
import LikeButton from 'components/LikeButton/LikeButton';
import { STEP_CONVERTER } from 'constants/common';
import { PALETTE } from 'constants/palette';
import { ButtonStyle } from 'types';
import useSnackBar from 'context/snackBar/useSnackBar';
import useMember from 'hooks/queries/useMember';

interface Props {
  id: number;
}

const FeedDetailContent = ({ id }: Props) => {
  const snackbar = useSnackBar();
  const member = useMember();

  const { data: feedDetail } = useFeedDetail({
    errorHandler: (error) => {
      snackbar.addSnackBar('error', error.message);
    },
    id,
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
              {member.userData?.id === feedDetail.author.id && (
                <FeedDropdown feedDetail={feedDetail} />
              )}
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
