import styled, { css } from 'styled-components';

import { PALETTE } from 'constants/palette';

const Label = styled.label`
  display: flex;
  align-items: center;
  gap: 12px;
`;

const Text = styled.span`
  font-size: 1rem;
  line-height: 1rem;
  user-select: none;
`;

const ToggleMark = styled.span`
  position: relative;
  width: 2.5rem;
  min-width: 2.5rem;
  height: 1.5rem;
  border-radius: 25px;
  display: flex;
  align-items: center;
  border: 2px solid ${PALETTE.PRIMARY_400};
  cursor: pointer;

  &::after {
    content: '';
    position: absolute;
    display: inline-block;
    width: 1rem;
    height: 1rem;
    border-radius: 50%;
    box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.25);

    transition: transform 0.2s ease, backgroud-color 0.5s ease;
  }
`;

const switchOn = css`
  background-color: ${PALETTE.PRIMARY_400};
  border: none;

  &::after {
    background-color: ${PALETTE.WHITE_400};
    transform: translateX(1.25rem);
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
