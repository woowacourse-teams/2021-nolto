import styled from 'styled-components';

import { hoverLayer } from 'commonStyles';
import { PALETTE } from 'constants/palette';

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
    justify-content: center;
    align-items: center;
    color: ${PALETTE.WHITE_400};
    width: 6rem;
    height: 2rem;
    background-color: ${PALETTE.PRIMARY_400};
    border-radius: 4px;
    overflow: hidden;

    ${hoverLayer({})};
  }

  cursor: pointer;
`;

const FileNameText = styled.span`
  font-size: 1rem;
`;

export default { Root, Label, FileNameText };
