import styled from 'styled-components';

import TextButton from 'components/@common/TextButton/TextButton';
import TextInput from 'components/@common/TextInput/TextInput';
import { PALETTE } from 'constants/palette';
import { Link } from 'react-router-dom';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 17.125rem;
`;

const Title = styled.h2`
  font-size: 1.75rem;
  margin-bottom: 2rem;
`;

const OAuthContainer = styled.div`
  margin-top: 3.25rem;
  width: 100%;
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

const SignUpWrapper = styled.div`
  font-size: 12px;
`;

const SignUpLink = styled(Link)`
  font-weight: 600;
  color: ${PALETTE.PRIMARY_400};
`;

export default {
  Root,
  Title,
  OAuthContainer,
  SignUpWrapper,
  SignUpLink,
};
