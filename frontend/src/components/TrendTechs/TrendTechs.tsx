import React from 'react';
import { useHistory } from 'react-router-dom';

import ROUTE from 'constants/routes';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useTrendTechs from 'hooks/queries/useTrendTechs';
import { Tech } from 'types';
import Styled from './TrendTechs.styles';

const TrendTechs = () => {
  const history = useHistory();

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

  const snackbar = useSnackbar();

  const { data: tags } = useTrendTechs({
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
    suspense: false,
  });

  return (
    <Styled.Root>
      <Styled.TagsContainer>
        <Styled.Title className="trends">💎 Trends</Styled.Title>
        <ul>
          {tags?.map((tag) => (
            <Styled.Tag key={tag.id} onClick={() => searchByTrend(tag)}>
              <span className="trends-text">{tag.text}</span>
            </Styled.Tag>
          ))}
        </ul>
      </Styled.TagsContainer>
    </Styled.Root>
  );
};

export default TrendTechs;
