import styled from 'styled-components';

import { MEDIA_QUERY } from 'constants/mediaQuery';

const Root = styled.div`
  width: 100%;
  display: flex;
  gap: 1.5rem;
  justify-content: center;
  padding: 2rem;

  @media ${MEDIA_QUERY.DESKTOP_SMALL} {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    padding: 1rem;
  }
`;

const FlexContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  max-width: 36rem;
  width: 100%;
`;

export default { Root, FlexContainer };
