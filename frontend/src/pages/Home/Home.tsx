import React, { useRef } from 'react';

import AsyncBoundary from 'components/AsyncBoundary';
import CroppedEllipse from 'components/CroppedEllipse/CroppedEllipse';
import Header from 'components/Header/Header';
import RecentFeedsContent from 'components/RecentFeedsContent/RecentFeedsContent';
import HotFeedsContent from 'components/HotFeedsContent/HotFeedsContent';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import TrendTechs from 'components/TrendTechs/TrendTechs';
import useOnScreen from 'hooks/@common/useOnScreen';
import ROUTE from 'constants/routes';
import Styled, { ScrollUpButton, SearchBar, MoreButton } from './Home.styles';
import MoreArrow from 'assets/moreArrow.svg';

const Home = () => {
  const ellipseRef = useRef();
  const isEllipseVisible = useOnScreen(ellipseRef);

  const RECENT_FEED_LENGTH = 4;

  const scrollTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
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
          <AsyncBoundary
            rejectedFallback={
              <ErrorFallback
                message="트렌드 기술 스택을 불러올 수 없습니다."
                queryKey="trendTechs"
              />
            }
          >
            <TrendTechs />
          </AsyncBoundary>
        </Styled.SearchContainer>

        <Styled.ContentArea>
          <Styled.SectionTitle fontSize="1.75rem">Hot Toy Project</Styled.SectionTitle>
          <Styled.HotToysContainer>
            <AsyncBoundary
              rejectedFallback={
                <ErrorFallback message="데이터를 불러올 수 없습니다." queryKey="hotFeeds" />
              }
            >
              <HotFeedsContent />
            </AsyncBoundary>
          </Styled.HotToysContainer>

          <Styled.SectionTitle fontSize="1.75rem">Recent Toy Project</Styled.SectionTitle>
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
