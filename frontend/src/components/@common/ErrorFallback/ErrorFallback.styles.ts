import { PALETTE } from 'constants/palette';
import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: 'Work Sans', sans-serif;
  color: ${PALETTE.RED_500};
`;

const ErrorText = styled.span`
  color: inherit;
  font-weight: 900;
  font-style: italic;
  font-size: 4rem;
`;

const Message = styled.pre`
  color: inherit;
  margin-top: 0.5rem;
`;

export default { Root, ErrorText, Message };
