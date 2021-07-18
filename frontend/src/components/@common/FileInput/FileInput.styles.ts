import { PALETTE } from 'constants/palette';
import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  width: 100%;
`;

const Label = styled.label`
  display: flex;
  align-items: center;

  & > input {
    position: absolute;
    opacity: 0;
    width: 0;
    height: 0;
  }

  & > span {
    display: flex;
    position: relative;
    justify-content: center;
    align-items: center;
    color: ${PALETTE.WHITE_400};
    width: 6rem;
    height: 2rem;
    background-color: ${PALETTE.PRIMARY_400};
    border-radius: 4px;
    overflow: hidden;

    &:hover::after {
      content: '';
      display: block;
      position: absolute;
      width: 100%;
      height: 100%;
      background-color: ${PALETTE.BLACK_400};
      opacity: 0.1;
    }
  }

  cursor: pointer;
`;

const FileNameText = styled.span`
  font-size: 1rem;
`;

export default { Root, Label, FileNameText };
