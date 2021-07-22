import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.button`
  background: transparent;
  border: none;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
`;

const Button = styled.div<{ selected: boolean }>`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 6rem;
  height: 6rem;
  border-radius: 50%;
  font-size: 3rem;
  box-shadow: ${({ selected }) => selected && 'inset'} 4px 4px 8px rgba(85, 85, 85, 0.25);
  cursor: pointer;
`;

const Progress = styled(Button)`
  background-color: ${PALETTE.PRIMARY_200};
`;

const Complete = styled(Button)`
  background-color: ${PALETTE.PRIMARY_300};
`;

const SOS = styled(Button)`
  background-color: ${PALETTE.PRIMARY_400};
`;

const Text = styled.span`
  color: ${PALETTE.PRIMARY_400};
  margin-top: 1rem;
`;

export default { Root, Progress, Complete, SOS, Text };
