import styled, { keyframes } from 'styled-components';

import HorseIcon from 'assets/horse.svg';
import { PALETTE } from 'constants/palette';

const Root = styled.div`
  width: fit-content;
  margin: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  color: ${PALETTE.PRIMARY_400};
`;

const Message = styled.div`
  display: flex;
  padding: 0 0.75rem;
  gap: 2rem;
  color: inherit;
  transform: translateY(15%);
`;

const ErrorTitle = styled.div`
  color: inherit;
  font-family: 'Work Sans', sans-serif;
  font-weight: 700;
  font-size: 4.5rem;
  transform: translateY(5%);
`;

const falldown = keyframes`
  0% {
    transform: rotate(0deg);
  }

  50% {
    transform: rotate(240deg);
  }

  100% {
    transform: rotate(180deg);
  }
`;

const Horse = styled(HorseIcon)`
  width: 72px;
  height: auto;

  animation: ${falldown} 1s ease forwards;
`;

const Divider = styled.hr`
  width: 100%;
  background-color: ${PALETTE.PRIMARY_400};
  height: 4px;
  border: none;
`;

const ErrorText = styled.div`
  color: inherit;
  font-family: 'Open Sans', 'Noto Sans KR', sans-serif;
  font-size: 1.25rem;
  margin-top: 1rem;
  padding: 0 0.75rem;
`;

export default { Root, Message, ErrorTitle, Divider, ErrorText, Horse };
