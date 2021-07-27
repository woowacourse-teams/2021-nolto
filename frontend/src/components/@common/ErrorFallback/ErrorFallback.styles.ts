import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: 'Work Sans', sans-serif;
  color: ${PALETTE.RED_500};
`;

const Image = styled.img`
  border-radius: 50%;
  filter: drop-shadow(0 0 1rem rgba(0, 0, 0, 0.25));
`;

const ErrorText = styled.span`
  color: inherit;
  font-weight: 900;
  font-style: italic;
  font-size: 4rem;
`;

const Message = styled.pre`
  color: inherit;
  font-size: 1.5rem;
  margin-top: 1rem;
`;

export default { Root, Image, ErrorText, Message };