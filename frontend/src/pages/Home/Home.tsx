import React, { useEffect, useRef } from 'react';
import { Helmet } from 'react-helmet-async';

import MoreArrow from 'assets/moreArrow.svg';
import Page from 'pages';
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
import { PALETTE } from 'constants/palette';
import hasWindow from 'constants/windowDetector';
import useOnScreen from 'hooks/@common/useOnScreen';
import { FeedStep } from 'types';
import HomeFeedsContent from './HomeFeedsContent/HomeFeedsContent';
import Styled, { MoreButton, ScrollUpButton, Searchbar } from './Home.styles';

interface Props {
  toggleTheme: () => void;
}

const Home = ({ toggleTheme }: Props) => {
  const ellipseRef = useRef();
  const isEllipseVisible = useOnScreen(ellipseRef);

  const scrollTop = () => {
    if (hasWindow) {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  const localSettingTheme = hasWindow ? localStorage.getItem('theme') : 'default';

  const searchTitle =
    localSettingTheme === 'default' ? 'Search for Ideas?' : 'π Trick or Treat! π»';

  useEffect(() => {
    if (!hasWindow) {
      window.scrollTo(0, 0);
    }
  }, []);

  return (
    <BaseLayout header={<Header isFolded={isEllipseVisible} />}>
      <Helmet>
        <title>λν : λλ¬μ€μΈμ ν μ΄νλ‘μ νΈ</title>
        <link rel="canonical" href="https://www.nolto.app" />
        <meta
          name="description"
          content="λͺ¨λμ μκ³  μμ€ν ν μ΄νλ‘μ νΈλ₯Ό νλμ, λλ¬μ€μΈμ ν μ΄νλ‘μ νΈ!"
        />
      </Helmet>
      <Styled.EllipseWrapper ref={ellipseRef}>
        <CroppedEllipse toggleTheme={toggleTheme} />
      </Styled.EllipseWrapper>
      <Styled.SearchContainer>
        <Styled.SearchTitle>{searchTitle}</Styled.SearchTitle>
        <Searchbar className="search-bar" selectable />
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
            <h2>
              <Styled.SectionTitle>π§© μ§νμ€μΈ νλ‘μ νΈ</Styled.SectionTitle>
            </h2>
            <MoreButton
              to={{
                pathname: ROUTE.RECENT,
                search:
                  '?' +
                  new URLSearchParams({
                    step: FeedStep.PROGRESS,
                  }),
              }}
              onMouseOver={() => Page.RecentFeeds.preload()}
            >
              MORE&nbsp;
              <span className="visually-hidden">μ§νμ€ νλ‘μ νΈ λλ³΄κΈ°</span>
              <MoreArrow width="10px" />
            </MoreButton>
          </Styled.TitleWrapper>
          <AsyncBoundary
            rejectedFallback={
              <ErrorFallback message={ERROR_MSG.LOAD_DATA} queryKey={QUERY_KEYS.RECENT_FEEDS} />
            }
          >
            <HomeFeedsContent feedsCountToShow={4} step={FeedStep.PROGRESS} />
          </AsyncBoundary>
        </Styled.ToysContainer>

        <Styled.ToysContainer>
          <Styled.TitleWrapper>
            <h2>
              <Styled.SectionTitle>π¦ μμ±λ νλ‘μ νΈ</Styled.SectionTitle>
            </h2>
            <MoreButton
              to={{
                pathname: ROUTE.RECENT,
                search:
                  '?' +
                  new URLSearchParams({
                    step: FeedStep.COMPLETE,
                  }),
              }}
              onMouseOver={() => Page.RecentFeeds.preload()}
            >
              MORE&nbsp;
              <span className="visually-hidden">μμ±λ νλ‘μ νΈ λλ³΄κΈ°</span>
              <MoreArrow width="10px" />
            </MoreButton>
          </Styled.TitleWrapper>
          <AsyncBoundary
            rejectedFallback={
              <ErrorFallback message={ERROR_MSG.LOAD_DATA} queryKey={QUERY_KEYS.RECENT_FEEDS} />
            }
          >
            <HomeFeedsContent feedsCountToShow={4} step={FeedStep.COMPLETE} />
          </AsyncBoundary>
        </Styled.ToysContainer>
      </Styled.ContentArea>
      <ScrollUpButton size="3rem" onClick={scrollTop} aria-label="νμ΄μ§ μλ¨μΌλ‘ μ΄λ">
        <Styled.ArrowUp width="14px" fill={PALETTE.PRIMARY_400} />
      </ScrollUpButton>
    </BaseLayout>
  );
};

export default Home;
