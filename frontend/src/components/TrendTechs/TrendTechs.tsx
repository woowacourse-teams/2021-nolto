import React from 'react';
import { useHistory } from 'react-router-dom';

import ROUTE from 'constants/routes';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useTrendTechs from 'hooks/queries/useTrendTechs';
import Styled from './TrendTechs.styles';
import { Tech } from 'types';

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
      <span className="trends">💎 Trends</span>
      {tags?.map((tag) => (
        <Styled.Tag key={tag.id} onClick={() => searchByTrend(tag)}>
          <span className="trends-bar">|</span>
          <span className="trends-text">{tag.text}</span>
        </Styled.Tag>
      ))}
    </Styled.Root>
  );
};

export default TrendTechs;
