import React from 'react';
import { useParams } from 'react-router-dom';

import Chip from 'components/@common/Chip/Chip';
import Header from 'components/Header/Header';
import LikeHeartIcon from 'assets/likeHeart.svg';
import ViewCountIcon from 'assets/viewCount.svg';
import useFeedDetail from 'hooks/queries/useFeedDetail';
import { ButtonStyle } from 'types';
import { STEP_CONVERTER } from 'constants/common';
import Styled, { Tag, StacksMoreButton } from './FeedDetail.styles';

const FeedDetail = () => {
  const params = useParams<{ id: string }>();
  const id = Number(params.id);
  const { data: feedDetail } = useFeedDetail(id);

  return (
    <>
      <Header />
      <Styled.Root>
        {feedDetail && (
          <>
            <Styled.IntroContainer>
              <div>
                <Styled.ThumbnailContainer>
                  <Styled.Thumbnail src={feedDetail.thumbnailUrl} />
                  {feedDetail.sos && <Styled.SOSFlagIcon width="56" />}
                </Styled.ThumbnailContainer>
                <Styled.IconsContainer>
                  <Styled.IconWrapper>
                    <LikeHeartIcon width="30" />
                    <span>{feedDetail.likes}</span>
                  </Styled.IconWrapper>
                  <Styled.IconWrapper>
                    <ViewCountIcon width="28" />
                    <span>{feedDetail.views}</span>
                  </Styled.IconWrapper>
                </Styled.IconsContainer>
              </div>

              <Styled.DetailsContainer>
                <Styled.TitleContainer>
                  <Styled.TitleWrapper>
                    <h2>{feedDetail.title}</h2>
                    <Chip.Solid>{STEP_CONVERTER[feedDetail.step]}</Chip.Solid>
                  </Styled.TitleWrapper>

                  <Styled.UserWrapper>
                    <Styled.UserName>{feedDetail.author.nickname}</Styled.UserName>
                    <Styled.UserImage src={feedDetail.author.imageUrl} />
                  </Styled.UserWrapper>
                </Styled.TitleContainer>
                <hr />

                <Styled.DetailsContent>
                  <Styled.DetailsPair>
                    <Styled.DetailsKey fontSize="1.5rem">서비스 URL</Styled.DetailsKey>
                    <Styled.DetailsValue>
                      <a href={feedDetail.deployedUrl} target="_blank">
                        {feedDetail.deployedUrl}
                      </a>
                    </Styled.DetailsValue>
                  </Styled.DetailsPair>
                  <Styled.DetailsPair>
                    <Styled.DetailsKey fontSize="1.5rem">저장소 URL</Styled.DetailsKey>
                    <Styled.DetailsValue>
                      <a href={feedDetail.storageUrl} target="_blank">
                        {feedDetail.storageUrl}
                      </a>
                    </Styled.DetailsValue>
                  </Styled.DetailsPair>
                  <Styled.DetailsPair>
                    <Styled.DetailsKey fontSize="1.5rem">기술스택</Styled.DetailsKey>
                    <Styled.DetailsValue>
                      {feedDetail.techs.map((tech) => (
                        <li key={tech.id}>
                          <Tag buttonStyle={ButtonStyle.SOLID}>{tech.text}</Tag>
                        </li>
                      ))}
                      <StacksMoreButton width="28" />
                    </Styled.DetailsValue>
                  </Styled.DetailsPair>
                </Styled.DetailsContent>
              </Styled.DetailsContainer>
            </Styled.IntroContainer>

            <Styled.DotsDivider width="60" />

            <Styled.Description>{feedDetail.content}</Styled.Description>
          </>
        )}
      </Styled.Root>
    </>
  );
};

export default FeedDetail;
