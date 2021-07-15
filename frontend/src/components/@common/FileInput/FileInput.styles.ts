import { PALETTE } from 'constants/palette';
import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  align-items: center;
  gap: 1.5rem;
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
    justify-content: center;
    align-items: center;
    color: ${PALETTE.WHITE_400};
    width: 8rem;
    height: 2.75rem;
    background-color: ${PALETTE.PRIMARY_400};
    border-radius: 4px;
  }

  cursor: pointer;
`;

const FileNameText = styled.span`
  font-size: 1.5rem;
`;

export default { Root, Label, FileNameText };
