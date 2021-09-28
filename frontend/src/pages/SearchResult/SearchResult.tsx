import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';

import AsyncBoundary from 'components/AsyncBoundary';
import BaseLayout from 'components/BaseLayout/BaseLayout';
import StepChip from 'components/StepChip/StepChip';
import Toggle from 'components/@common/Toggle/Toggle';
import SearchResultContent from 'pages/SearchResult/SearchResultContent/SearchResultContent';
import SearchResultHeader from 'pages/SearchResult/SearchResultHeader/SearchResultHeader';
import { FONT_SIZE } from 'constants/styles';
import { FeedStep, Tech } from 'types';
import Styled from './SearchResult.styles';

interface LocationState {
  query: string;
  techs: Tech[];
}

const SearchResult = () => {
  const location = useLocation<LocationState>();

  const [query, setQuery] = useState('');
  const [techs, setTechs] = useState('');
  const [step, setStep] = useState<FeedStep>(null);
  const [help, setHelp] = useState(false);

  const toggleLevel = (feedStep: FeedStep) => {
    if (step === feedStep) setStep(null);
    else setStep(feedStep);
  };

  return (
    <BaseLayout>
      <Styled.TopContainer>
        <Styled.SectionTitle fontSize={FONT_SIZE.X_LARGE}>Toys About</Styled.SectionTitle>
        <AsyncBoundary rejectedFallback={<div>게시물 검색에 실패했습니다.</div>}>
          <SearchResultHeader
            searchParams={location.search}
            query={query}
            setQuery={setQuery}
            techs={techs}
            setTechs={setTechs}
          />
        </AsyncBoundary>

        <Styled.StepChipsContainer>
          <Styled.Button type="button" onClick={() => toggleLevel(FeedStep.PROGRESS)}>
            <StepChip step={FeedStep.PROGRESS} selected={step === FeedStep.PROGRESS} />
          </Styled.Button>
          <Styled.Button type="button" onClick={() => toggleLevel(FeedStep.COMPLETE)}>
            <StepChip step={FeedStep.COMPLETE} selected={step === FeedStep.COMPLETE} />
          </Styled.Button>
          <Toggle labelText="🚨도움요청" onChange={() => setHelp(!help)} fontSize="14px" />
        </Styled.StepChipsContainer>
      </Styled.TopContainer>

      <AsyncBoundary rejectedFallback={<div>게시물 검색에 실패했습니다.</div>}>
        <SearchResultContent searchParams={{ query, techs }} step={step} help={help} />
      </AsyncBoundary>
    </BaseLayout>
  );
};

export default SearchResult;
