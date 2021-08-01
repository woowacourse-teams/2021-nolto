import React, { useRef } from 'react';
import { useHistory } from 'react-router-dom';

import AsyncBoundary from 'components/AsyncBoundary';
import CroppedEllipse from 'components/CroppedEllipse/CroppedEllipse';
import Header from 'components/Header/Header';
import RecentFeedsContent from 'components/RecentFeedsContent/RecentFeedsContent';
import HotFeedsContent from 'components/HotFeedsContent/HotFeedsContent';
import useOnScreen from 'hooks/@common/useOnScreen';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import ROUTE from 'constants/routes';
import Styled, { ScrollUpButton, SearchBar, MoreButton } from './Home.styles';
import MoreArrow from 'assets/moreArrow.svg';
import { Tech } from 'types';

const tags: Tech[] = [
  {
    id: 25,
    text: 'ReactJS',
  },
  {
    id: 882,
    text: 'Java',
  },
  {
    id: 655,
    text: 'JavaScript',
  },
  {
    id: 67,
    text: 'Spring',
  },
];

const Home = () => {
  const history = useHistory();

  const ellipseRef = useRef();
  const isEllipseVisible = useOnScreen(ellipseRef);

  const RECENT_FEED_LENGTH = 4;

  const scrollTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const searchByTrend = (tech: Tech) => {
    const queryParams = new URLSearchParams({
      query: '',
      techs: tech.text,
    });

    history.push({
      pathname: ROUTE.SEARCH,
      search: '?' + queryParams,
      state: { techs: [tech] },
    });
  };

  return (
    <>
      <Header isFolded={isEllipseVisible} />
      <Styled.Root>
        <Styled.EllipseWrapper ref={ellipseRef}>
          <CroppedEllipse />
        </Styled.EllipseWrapper>
        <Styled.SearchContainer>
          <Styled.SearchTitle>Search for Ideas?</Styled.SearchTitle>
          <SearchBar selectable />
          <Styled.TrendContainer>
            <span className="trends">💎 Trends</span>
            {tags.map((tag) => (
              <Styled.TrendTag key={tag.id} onClick={() => searchByTrend(tag)}>
                <span className="trends-bar">|</span>
                <span className="trends-text">{tag.text}</span>
              </Styled.TrendTag>
            ))}
          </Styled.TrendContainer>
        </Styled.SearchContainer>

        <Styled.ContentArea>
          <Styled.SectionTitle fontSize="1.75rem">Hot Toys</Styled.SectionTitle>
          <Styled.HotToysContainer>
            <AsyncBoundary
              rejectedFallback={
                <ErrorFallback message="데이터를 불러올 수 없습니다." queryKey="hotFeeds" />
              }
            >
              <HotFeedsContent />
            </AsyncBoundary>
          </Styled.HotToysContainer>

          <Styled.SectionTitle fontSize="1.75rem">Recent Toys</Styled.SectionTitle>
          <Styled.RecentToysContainer>
            <AsyncBoundary
              rejectedFallback={
                <ErrorFallback message="데이터를 불러올 수 없습니다." queryKey="recentFeeds" />
              }
            >
              <RecentFeedsContent feedsCountToShow={RECENT_FEED_LENGTH} />
            </AsyncBoundary>
            <MoreButton to={ROUTE.RECENT}>
              MORE&nbsp;
              <MoreArrow width="10px" />
            </MoreButton>
          </Styled.RecentToysContainer>
        </Styled.ContentArea>
        <ScrollUpButton onClick={scrollTop}>
          <Styled.ArrowUp width="14px" />
        </ScrollUpButton>
      </Styled.Root>
    </>
  );
};

export default Home;
