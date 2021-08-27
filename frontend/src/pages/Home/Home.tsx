import React, { useEffect, useRef } from 'react';

import AsyncBoundary from 'components/AsyncBoundary';
import CroppedEllipse from 'components/CroppedEllipse/CroppedEllipse';
import Header from 'components/Header/Header';
import HotFeedsContent from 'components/HotFeedsContent/HotFeedsContent';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import TrendTechs from 'components/TrendTechs/TrendTechs';
import useOnScreen from 'hooks/@common/useOnScreen';
import ROUTE from 'constants/routes';
import Styled, { ScrollUpButton, SearchBar, MoreButton } from './Home.styles';
import MoreArrow from 'assets/moreArrow.svg';
import HomeFeedsContent from './HomeFeedsContent/HomeFeedsContent';
import { FilterType } from 'types';

const Home = () => {
  const ellipseRef = useRef();
  const isEllipseVisible = useOnScreen(ellipseRef);

  const scrollTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  return (
    <>
      <Header isFolded={isEllipseVisible} />
      <Styled.Root>
        <Styled.EllipseWrapper ref={ellipseRef}>
          <CroppedEllipse />
        </Styled.EllipseWrapper>
        <Styled.SearchContainer>
          <Styled.SearchTitle>Search for Ideas?</Styled.SearchTitle>
          <SearchBar className="search-bar" selectable />
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
          <Styled.HotToysContainer>
            <AsyncBoundary
              rejectedFallback={
                <ErrorFallback message="데이터를 불러올 수 없습니다." queryKey="hotFeeds" />
              }
            >
              <HotFeedsContent />
            </AsyncBoundary>
          </Styled.HotToysContainer>

          <Styled.ToysContainer>
            <Styled.TitleWrapper>
              <Styled.SectionTitle fontSize="1.75rem">Completed Toy Project</Styled.SectionTitle>
              <MoreButton to={ROUTE.RECENT}>
                MORE&nbsp;
                <MoreArrow width="10px" />
              </MoreButton>
            </Styled.TitleWrapper>
            <AsyncBoundary
              rejectedFallback={
                <ErrorFallback message="데이터를 불러올 수 없습니다." queryKey="recentFeeds" />
              }
            >
              <HomeFeedsContent filter={FilterType.COMPLETE} />
            </AsyncBoundary>
          </Styled.ToysContainer>

          <Styled.ToysContainer>
            <Styled.TitleWrapper>
              <Styled.SectionTitle fontSize="1.75rem">Progressive Toy Project</Styled.SectionTitle>
              <MoreButton to={ROUTE.RECENT}>
                MORE&nbsp;
                <MoreArrow width="10px" />
              </MoreButton>
            </Styled.TitleWrapper>
            <AsyncBoundary
              rejectedFallback={
                <ErrorFallback message="데이터를 불러올 수 없습니다." queryKey="recentFeeds" />
              }
            >
              <HomeFeedsContent filter={FilterType.PROGRESS} />
            </AsyncBoundary>
          </Styled.ToysContainer>
        </Styled.ContentArea>
        <ScrollUpButton onClick={scrollTop}>
          <Styled.ArrowUp width="14px" />
        </ScrollUpButton>
      </Styled.Root>
    </>
  );
};

export default Home;
