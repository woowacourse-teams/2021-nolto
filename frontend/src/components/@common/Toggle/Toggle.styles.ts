import styled, { css } from 'styled-components';

import { PALETTE } from 'constants/palette';

const Label = styled.label`
  display: flex;
  align-items: center;
  gap: 12px;
`;

const Text = styled.span`
  font-size: 24px;
  line-height: 24px;
  user-select: none;
`;

const ToggleMark = styled.span`
  position: relative;
  width: 60px;
  height: 32px;
  border-radius: 25px;
  display: flex;
  align-items: center;
  border: 2px solid ${PALETTE.PRIMARY_400};
  cursor: pointer;

  &::after {
    content: '';
    position: absolute;
    display: inline-block;
    width: 24px;
    height: 24px;
    border-radius: 50%;
    box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.25);

    transition: transform 0.5s ease, backgroud-color 0.5s ease;
  }
`;

const switchOn = css`
  background-color: ${PALETTE.PRIMARY_400};
  border: none;

  &::after {
    background-color: ${PALETTE.WHITE_400};
    transform: translateX(32px);
  }
`;

const switchOff = css`
  background-color: ${PALETTE.WHITE_400};

  &::after {
    background-color: ${PALETTE.PRIMARY_400};
    transform: translateX(3px);
  }
`;

const Input = styled.input`
  display: none;

  & ~ ${ToggleMark} {
    ${switchOff}
  }

  &:checked {
    & ~ ${ToggleMark} {
      ${switchOn}
    }
  }
`;

export default { Label, Text, Input, ToggleMark };
