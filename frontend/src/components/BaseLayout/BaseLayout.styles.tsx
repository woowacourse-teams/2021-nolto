import { BREAK_POINTS, MEDIA_QUERY } from 'constants/mediaQuery';
import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 92px;
`;

const BaseLayoutMain = styled.main`
  max-width: ${BREAK_POINTS.LAPTOP};
  padding: 2rem 1rem 0;

  @media ${MEDIA_QUERY.LAPTOP} {
    width: 100%;
  }
`;

export default { Root, BaseLayoutMain };
