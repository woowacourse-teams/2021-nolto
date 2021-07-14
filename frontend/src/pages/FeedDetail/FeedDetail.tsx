import React from 'react';

import Chip from 'components/@common/Chip/Chip';
import LikeHeartIcon from 'assets/likeHeart.svg';
import ViewCountIcon from 'assets/viewCount.svg';
import { ButtonStyle } from 'types';
import Styled from './FeedDetail.styles';

const mockFeed = {
  id: 1,
  user: {
    id: 1,
    nickname: 'zigsong',
    imageUrl: 'https://avatars.githubusercontent.com/u/48755175?v=4',
  },
  title: 'Good Toy',
  content: 'Good Nice Perfect Gorgeous Wonderful!',
  thumbnailUrl: 'https://i.pinimg.com/236x/f5/45/6e/f5456e14993cac65828e289048a89f3e.jpg',
  sos: false,
};

const FeedDetail = () => {
  return (
    <Styled.Root>
      <Styled.IntroContainer>
        <div>
          <Styled.ThumbnailContainer>
            <Styled.Thumbnail src={mockFeed.thumbnailUrl} />
            <Styled.SOSFlagIcon width="56" />
          </Styled.ThumbnailContainer>
          <Styled.IconsContainer>
            <Styled.IconWrapper>
              <LikeHeartIcon width="30" />
              <caption>77</caption>
            </Styled.IconWrapper>
            <Styled.IconWrapper>
              <ViewCountIcon width="28" />
              <caption>88</caption>
            </Styled.IconWrapper>
          </Styled.IconsContainer>
        </div>

        <Styled.DetailsContainer>
          <Styled.TitleContainer>
            <Styled.TitleWrapper>
              <h2>조영상의 최고 프로젝트</h2>
              <Chip.Solid>전시중</Chip.Solid>
            </Styled.TitleWrapper>

            <Styled.UserWrapper>
              <Styled.UserName>Joel Jo</Styled.UserName>
              <Styled.UserImage src={mockFeed.user.imageUrl} />
            </Styled.UserWrapper>
          </Styled.TitleContainer>
          <hr />

          <Styled.DetailsContent>
            <Styled.DetailsPair>
              <Styled.DetailsKey fontSize="1.5rem">서비스 URL</Styled.DetailsKey>
              <Styled.DetailsValue>http://www.woowa.techcourse</Styled.DetailsValue>
            </Styled.DetailsPair>
            <Styled.DetailsPair>
              <Styled.DetailsKey fontSize="1.5rem">저장소 URL</Styled.DetailsKey>
              <Styled.DetailsValue>http://www.woowa.techcourse</Styled.DetailsValue>
            </Styled.DetailsPair>
            <Styled.DetailsPair>
              <Styled.DetailsKey fontSize="1.5rem">기술스택</Styled.DetailsKey>
              <Styled.DetailsValue>
                <Styled.Tag buttonStyle={ButtonStyle.SOLID}>React.js</Styled.Tag>
                <Styled.Tag buttonStyle={ButtonStyle.SOLID}>React.js</Styled.Tag>
                <Styled.Tag buttonStyle={ButtonStyle.SOLID}>React.js</Styled.Tag>
                <Styled.StacksMoreButton width="28" />
              </Styled.DetailsValue>
            </Styled.DetailsPair>
          </Styled.DetailsContent>
        </Styled.DetailsContainer>
      </Styled.IntroContainer>

      <Styled.DotsDivider width="60" />

      <Styled.Description>
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sollicitudin sapien curabitur
        sagittis. In elementum ullamcorper facilisi dui enim. Nisl eu sed est, in ultrices donec.
        Commodo dictumst neque, egestas egestas amet in enim arcu. Turpis ut est non nulla.
        Dignissim molestie lobortis laoreet faucibus ullamcorper. Etiam amet arcu, tortor, nisl at
        cras commodo, placerat commodo. Aliquam consectetur orci urna consequat sed. Cursus ultrices
        et dui nibh lectus congue aliquam lorem eu. Nunc, malesuada sed ut pulvinar amet tellus
        libero. Tortor cras risus porta blandit.
      </Styled.Description>
    </Styled.Root>
  );
};

export default FeedDetail;
