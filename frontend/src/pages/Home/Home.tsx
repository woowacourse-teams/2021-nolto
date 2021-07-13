import React from 'react';

import CroppedEllipse from 'components/CroppedEllipse/CroppedEllipse';
import RegularCard from 'components/RegularCard/RegularCard';
import StretchCard from 'components/StretchCard/StretchCard';
import LevelLinkButton from 'components/LevelLinkButton/LevelLinkButton';
import MoreArrow from 'assets/moreArrow.svg';
import { ButtonStyle } from 'types';
import Styled from './Home.styles';

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

const mockUser = {
  id: 1,
  nickname: 'zigsong',
  imageUrl: 'https://avatars.githubusercontent.com/u/48755175?v=4',
};

const tags = ['JavaScript', 'Java', 'React.js', 'Spring'];

const Home = () => {
  return (
    <Styled.Root>
      <Styled.EllipseWrapper>
        <CroppedEllipse />
      </Styled.EllipseWrapper>
      <Styled.SearchContainer>
        <Styled.SearchTitle>Search for Ideas?</Styled.SearchTitle>
        <Styled.MainSearchbar />
        <Styled.TagsContainer>
          {tags.map((tag) => (
            <Styled.TagButton buttonStyle={ButtonStyle.SOLID} reverse={true}>
              {tag}
            </Styled.TagButton>
          ))}
        </Styled.TagsContainer>
      </Styled.SearchContainer>

      <Styled.ContentArea>
        <Styled.SectionTitle fontSize="32px">Hot Toys</Styled.SectionTitle>
        <Styled.HotToysContainer>
          <Styled.CarouselLeft width="24" />
          <Styled.HotToyCardsContainer>
            {Array.from({ length: 3 }, () => (
              <li>
                <Styled.VerticalAvatar user={mockUser} />
                <RegularCard feed={mockFeed} />
              </li>
            ))}
          </Styled.HotToyCardsContainer>
          <Styled.CarouselRight width="24" />
        </Styled.HotToysContainer>
        <Styled.SectionTitle fontSize="32px">Recent Toys</Styled.SectionTitle>
        <Styled.RecentToysContainer>
          <Styled.LevelButtonsContainer>
            <LevelLinkButton.Progress />
            <LevelLinkButton.Complete />
            <LevelLinkButton.SOS />
          </Styled.LevelButtonsContainer>
          <Styled.RecentToyCardsContainer>
            {Array.from({ length: 4 }, () => (
              <li>
                <Styled.VerticalAvatar user={mockUser} />
                <StretchCard feed={mockFeed} />
              </li>
            ))}
          </Styled.RecentToyCardsContainer>
          <Styled.MoreButton>
            MORE&nbsp;
            <MoreArrow width="10" />
          </Styled.MoreButton>
        </Styled.RecentToysContainer>
      </Styled.ContentArea>
    </Styled.Root>
  );
};

export default Home;
