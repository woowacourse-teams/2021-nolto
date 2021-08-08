import styled from 'styled-components';

import TextButton from 'components/@common/TextButton/TextButton';
import CrossMark from 'assets/crossMark.svg';
import { PALETTE } from 'constants/palette';

const Root = styled.div`
  margin: 0.25rem 0;
  width: 100%;
  display: flex;
  flex-wrap: wrap;
`;

export const TechButton = styled(TextButton.Rounded)`
  display: flex;
  align-items: center;
  width: fit-content;
  height: 1.5rem;
  padding: 0 0.5rem;
  margin: 0.125rem 0.125rem;
`;

const DeleteMark = styled(CrossMark)<{ reverse: boolean }>`
  margin-left: 0.35rem;
  display: block;
  fill: ${({ reverse }) => (reverse ? PALETTE.PRIMARY_400 : PALETTE.WHITE_400)};
`;

export default { Root, DeleteMark };
