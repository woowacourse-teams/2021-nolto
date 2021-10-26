import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { defaultShadow } from 'commonStyles';

const Root = styled.div<{ reverse: boolean }>`
  display: flex;
  align-items: center;
  flex-direction: ${({ reverse }) => (reverse ? 'row-reverse' : 'row')};
  align-self: ${({ reverse }) => (reverse ? 'flex-end' : 'flex-start')};
  position: relative;
  max-width: 44rem;
  width: 100%;
  padding: 1rem;
  gap: 1.5rem;
  border-radius: 25px;
  ${defaultShadow};

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    max-width: 500px;
    margin: 0 auto;
    flex-direction: column;
    align-items: center;
    gap: 0;
  }
`;

const Picture = styled.picture`
  max-width: 180px;
  width: 100%;
  flex-shrink: 0;

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    width: 100px;
  }

  * {
    filter: drop-shadow(0 0 1rem rgba(0, 0, 0, 0.25));
    transition: transform 0.3s ease;

    &:hover {
      transform: scale(1.1);
    }
  }

  & img {
    width: 100%;
  }
`;

const TextWrapper = styled.div<{ reverse: boolean }>`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  align-items: ${({ reverse }) => (reverse ? 'flex-end' : 'flex-start')};
  color: ${PALETTE.GRAY_500};

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    align-items: center;
  }
`;

const Name = styled.span`
  font-size: 1.5rem;
  font-weight: 700;
`;

const Intro = styled.p`
  color: inherit;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    width: 100%;
    text-align: center;
  }
`;

const UrlContainer = styled.div<{ reverse: boolean }>`
  display: flex;
  flex-direction: ${({ reverse }) => (reverse ? 'row-reverse' : 'row')};
  align-items: center;
  gap: 0.75rem;
  margin-top: 1.5rem;

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    margin-top: 0;
  }
`;

const UrlBar = styled.div`
  width: 2px;
  height: 3rem;
  background-color: ${PALETTE.GRAY_500};
  display: flex;

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    display: none;
  }
`;

const UrlWrapper = styled.div<{ reverse: boolean }>`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  align-items: ${({ reverse }) => (reverse ? 'flex-end' : 'flex-start')};

  & a {
    color: inherit;
    display: inline-block;
    vertical-align: top;
    max-width: 15rem;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;

    &:hover {
      text-decoration: underline;
    }
  }

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    align-items: center;
  }
`;

export default { Root, Picture, TextWrapper, Name, Intro, UrlContainer, UrlBar, UrlWrapper };
