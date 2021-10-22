import styled from 'styled-components';

import { hoverUnderline } from 'commonStyles';
import { BREAK_POINTS, MEDIA_QUERY } from 'constants/mediaQuery';
import { PALETTE } from 'constants/palette';

const Root = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  color: ${PALETTE.WHITE_400};
  overflow-x: auto;
  overflow-y: hidden;

  & span {
    color: ${({ theme }) => theme.highLightedText};
    line-height: 1rem;
    text-align: center;
  }

  & span.trends {
    font-weight: 700;
  }
`;

const TagsContainer = styled.div`
  display: flex;
  align-items: center;
  width: fit-content;
  margin: 0 auto;
  max-width: ${BREAK_POINTS.DESKTOP};
  color: inherit;
  gap: 1rem;

  & * {
    color: ${({ theme }) => theme.highLightedText};
  }

  & ul {
    display: flex;
    gap: 1rem;

    @media ${MEDIA_QUERY.MOBILE} {
      gap: 0.5rem;
    }

    & > *::after {
      content: '|';
      margin-left: 1rem;

      @media ${MEDIA_QUERY.MOBILE} {
        margin-left: 0.5rem;
      }
    }

    & > *:last-child::after {
      display: none;
    }
  }
`;

const Title = styled.div`
  white-space: nowrap;
  font-weight: 700;
  color: ${({ theme }) => theme.highLightedText};

  &::after {
    content: '|';
    margin-left: 1rem;
    color: inherit;

    @media ${MEDIA_QUERY.MOBILE} {
      display: none;
    }
  }

  @media ${MEDIA_QUERY.MOBILE} {
    display: none;
  }
`;

const Tag = styled.li`
  cursor: pointer;
  white-space: nowrap;

  & > .trends-text {
    ${hoverUnderline};
  }

  @media ${MEDIA_QUERY.MOBILE} {
    font-size: 14px;
  }
`;

export default { Root, Title, TagsContainer, Tag };
