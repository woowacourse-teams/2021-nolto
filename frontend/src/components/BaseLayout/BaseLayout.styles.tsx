import { HEIGHT } from 'constants/common';
import { BREAK_POINTS } from 'constants/mediaQuery';
import styled from 'styled-components';

const Root = styled.div`
  padding-top: ${HEIGHT.HEADER};
`;

export const BaseLayoutMain = styled.main`
  max-width: ${BREAK_POINTS.DESKTOP};
  padding: 2rem 1rem;
  margin: 0 auto;
  overflow: auto;
  width: 100%;
`;

export default { Root };
