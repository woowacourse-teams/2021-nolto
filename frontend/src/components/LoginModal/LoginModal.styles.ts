import styled from 'styled-components';

import TextButton from 'components/@common/TextButton/TextButton';
import { PALETTE } from 'constants/palette';
import { MEDIA_QUERY } from 'constants/mediaQuery';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 16rem;
`;

const Title = styled.h2`
  font-size: 1.75rem;

  @media ${MEDIA_QUERY.MOBILE} {
    font-size: 1.25rem;
  }
`;

const Greeting = styled.div`
  margin-top: 1rem;
  color: ${PALETTE.GRAY_400};
`;

const OAuthContainer = styled.div`
  margin-top: 3.25rem;
  width: 100%;

  @media ${MEDIA_QUERY.MOBILE} {
    margin-top: 2rem;
    padding: 0 0.5rem;
  }
`;

export const OAuthButton = styled(TextButton.Rounded)`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 2.25rem;
  margin-bottom: 0.625rem;

  & > svg {
    margin-right: 1.5rem;
  }
`;

export default {
  Root,
  Title,
  Greeting,
  OAuthContainer,
};
