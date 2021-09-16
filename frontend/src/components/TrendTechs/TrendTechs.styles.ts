import styled from 'styled-components';

import { hoverUnderline } from 'commonStyles';
import { BREAK_POINTS } from 'constants/mediaQuery';

const Root = styled.div`
  display: flex;
  align-items: center;
  width: 100%;

  & span {
    color: ${({ theme }) => theme.highLightedText};
    line-height: 1rem;
    text-align: center;
  }

  & span.trends {
    font-weight: 700;
  }
`;

const Title = styled.span`
  white-space: nowrap;
`;

const TagsContainer = styled.div`
  display: flex;
  gap: 1rem;
  width: fit-content;
  margin-left: auto;
  margin-right: auto;
  max-width: ${BREAK_POINTS.DESKTOP};

  & > *::after {
    content: '|';
    margin-left: 1rem;
  }

  & > *:last-child::after {
    display: none;
  }
`;

const Tag = styled.span`
  cursor: pointer;
  white-space: nowrap;

  > .trends-text {
    ${hoverUnderline};
  }

  > .trends-bar {
    margin-right: 0.75rem;
  }
`;

export default { Root, Title, TagsContainer, Tag };
