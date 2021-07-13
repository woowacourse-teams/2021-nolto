import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.form`
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 0 8px 0 24px;

  background-color: ${PALETTE.WHITE_400};
  border-radius: 25px;
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.25));
`;

const Input = styled.input`
  font-size: 24px;
  flex-grow: 1;
  outline: none;
  border: none;
`;

const Button = styled.button`
  border: none;
  background-color: transparent;
  width: 1.75em;
  height: 1.75em;
`;

export default { Root, Input, Button };
