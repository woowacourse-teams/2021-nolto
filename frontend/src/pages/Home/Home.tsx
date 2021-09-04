import React, { useEffect, useRef } from 'react';

import MoreArrow from 'assets/moreArrow.svg';
import AsyncBoundary from 'components/AsyncBoundary';
import BaseLayout from 'components/BaseLayout/BaseLayout';
import CroppedEllipse from 'components/CroppedEllipse/CroppedEllipse';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import Header from 'components/Header/Header';
import HotFeedsContent from 'components/HotFeedsContent/HotFeedsContent';
import TrendTechs from 'components/TrendTechs/TrendTechs';
import ROUTE from 'constants/routes';
import { ERROR_MSG } from 'constants/message';
import QUERY_KEYS from 'constants/queryKeys';
import useOnScreen from 'hooks/@common/useOnScreen';
import { FilterType } from 'types';
import HomeFeedsContent from './HomeFeedsContent/HomeFeedsContent';
import Styled, { MoreButton, ScrollUpButton, SearchBar } from './Home.styles';

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
    <BaseLayout header={<Header isFolded={isEllipseVisible} />}>
      <Styled.EllipseWrapper ref={ellipseRef}>
        <CroppedEllipse />
      </Styled.EllipseWrapper>
      <Styled.SearchContainer>
        <Styled.SearchTitle>Search for Ideas?</Styled.SearchTitle>
        <SearchBar className="search-bar" selectable />
        <AsyncBoundary
          rejectedFallback={
            <ErrorFallback message={ERROR_MSG.LOAD_TRENDS} queryKey={QUERY_KEYS.TREND_TECHS} />
          }
        >
          <TrendTechs />
        </AsyncBoundary>
      </Styled.SearchContainer>

      <Styled.ContentArea>
        <Styled.HotToysContainer>
          <AsyncBoundary
            rejectedFallback={
              <ErrorFallback message={ERROR_MSG.LOAD_DATA} queryKey={QUERY_KEYS.HOT_FEEDS} />
            }
          >
            <HotFeedsContent />
          </AsyncBoundary>
        </Styled.HotToysContainer>

        <Styled.ToysContainer>
          <Styled.TitleWrapper>
            <Styled.SectionTitle>🦄 완성된 프로젝트</Styled.SectionTitle>
            <MoreButton to={ROUTE.RECENT}>
              MORE&nbsp;
              <MoreArrow width="10px" />
            </MoreButton>
          </Styled.TitleWrapper>
          <AsyncBoundary
            rejectedFallback={
              <ErrorFallback message={ERROR_MSG.LOAD_DATA} queryKey={QUERY_KEYS.RECENT_FEEDS} />
            }
          >
            <HomeFeedsContent feedsCountToShow={4} filter={FilterType.COMPLETE} />
          </AsyncBoundary>
        </Styled.ToysContainer>

        <Styled.ToysContainer>
          <Styled.TitleWrapper>
            <Styled.SectionTitle>🧩 진행중인 프로젝트</Styled.SectionTitle>
            <MoreButton to={ROUTE.RECENT}>
              MORE&nbsp;
              <MoreArrow width="10px" />
            </MoreButton>
          </Styled.TitleWrapper>
          <AsyncBoundary
            rejectedFallback={
              <ErrorFallback message={ERROR_MSG.LOAD_DATA} queryKey={QUERY_KEYS.RECENT_FEEDS} />
            }
          >
            <HomeFeedsContent feedsCountToShow={4} filter={FilterType.PROGRESS} />
          </AsyncBoundary>
        </Styled.ToysContainer>
      </Styled.ContentArea>
      <ScrollUpButton size="3rem" onClick={scrollTop}>
        <Styled.ArrowUp width="14px" />
      </ScrollUpButton>
    </BaseLayout>
  );
};

export default Home;
