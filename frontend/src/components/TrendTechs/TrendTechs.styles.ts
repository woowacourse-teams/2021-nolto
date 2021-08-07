import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.div`
  display: flex;
  gap: 0.75rem;
  align-items: center;

  & span {
    color: ${PALETTE.WHITE_400};
    line-height: 1rem;
  }

  & span.trends {
    font-weight: 700;
  }
`;

const Tag = styled.span`
  cursor: pointer;

  > .trends-text {
    &:hover {
      text-decoration: underline;
    }
  }

  > .trends-bar {
    margin-right: 0.75rem;
  }
`;

export default { Root, Tag };
