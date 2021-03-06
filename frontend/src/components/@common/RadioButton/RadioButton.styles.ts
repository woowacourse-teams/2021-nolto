import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const RadioMark = styled.span`
  position: relative;
  top: 0;
  left: 0;
  height: 1.25rem;
  width: 1.25rem;
  background-color: #ffffff;
  border: 2px solid ${PALETTE.PRIMARY_400};
  border-radius: 50%;
  box-sizing: border-box;

  &::after {
    content: '';
    position: absolute;
    display: none;
    left: 5px;
    top: 3px;
    width: 20%;
    height: 40%;
    border: solid white;
    border-width: 0 3px 3px 0;
    transform: rotate(45deg);
  }
`;

const Label = styled.label`
  display: flex;
  position: relative;
  cursor: pointer;
  font-size: 16px;
  user-select: none;
  align-items: center;

  &:hover ${RadioMark} {
    background-color: #e1f5f4;
  }
`;

const Text = styled.span`
  padding-left: 7px;
  margin-right: 8px;
  font-size: 1rem;
  line-height: 1rem;
`;

const RadioButton = styled.input`
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;

  &:checked {
    & ~ ${RadioMark} {
      background-color: ${PALETTE.PRIMARY_400};
    }

    & ~ ${RadioMark}:after {
      display: block;
    }
  }
`;

export default {
  Label,
  Text,
  RadioButton,
  RadioMark,
};
