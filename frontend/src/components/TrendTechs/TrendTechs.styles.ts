import styled from 'styled-components';

import { hoverUnderline } from 'commonStyles';
import { BREAK_POINTS, MEDIA_QUERY } from 'constants/mediaQuery';
import { PALETTE } from 'constants/palette';

const Root = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  color: ${PALETTE.WHITE_400};

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

  & ul {
    display: flex;
    gap: 1rem;

    @media ${MEDIA_QUERY.MOBILE} {
      gap: 0.5rem;
    }

    & > *::after {
      content: '|';
      color: ${PALETTE.WHITE_400};
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
  color: inherit;
  font-weight: 700;

  &::after {
    content: '|';
    color: ${PALETTE.WHITE_400};
    margin-left: 1rem;

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
