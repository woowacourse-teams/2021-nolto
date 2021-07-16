import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import TextButton from 'components/@common/TextButton/TextButton';

const Root = styled.div`
  position: relative;
`;

const Dropdown = styled.ul`
  position: absolute;
  background-color: ${PALETTE.WHITE_400};
  width: 100%;
  margin-top: 2px;
  max-height: 20rem;
  overflow-y: scroll;
`;

const TechOption = styled.li<{ focused?: boolean }>`
  font-size: 1rem;
  width: 100%;
  padding: 8px 12px;
  border: 1px solid ${PALETTE.PRIMARY_400};
  background-color: ${({ focused }) => focused && PALETTE.PRIMARY_400};
  color: ${({ focused }) => focused && PALETTE.WHITE_400};

  &:hover {
    background-color: ${PALETTE.PRIMARY_400};
    color: ${PALETTE.WHITE_400};
  }

  &:first-child {
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;
  }

  &:last-child {
    border-bottom-left-radius: 8px;
    border-bottom-right-radius: 8px;
  }
`;

const TechButtonsContainer = styled.div`
  margin-bottom: 0.25rem;
  width: 100%;
  display: flex;
  flex-wrap: wrap;
`;

export const TechButton = styled(TextButton.Rounded)`
  width: fit-content;
  height: 1.5rem;
  padding: 0 0.5rem;
  margin-right: 0.25rem;
`;

export default { Root, Dropdown, TechOption, TechButtonsContainer };
