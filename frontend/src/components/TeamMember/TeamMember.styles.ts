import styled from 'styled-components';

import HighLightedTextComponent from 'components/@common/HighlightedText/HighlightedText';
import { PALETTE } from 'constants/palette';

const Root = styled.div<{ reverse: boolean }>`
  display: flex;
  align-items: center;
  position: relative;
  width: 44rem;
  padding: 1rem;
  align-self: ${({ reverse }) => (reverse ? 'flex-end' : 'flex-start')};
  gap: 1.5rem;

  border-radius: 25px;
  box-shadow: 4px 4px 8px 4px rgba(85, 85, 85, 0.2);
`;

const Image = styled.img`
  filter: drop-shadow(0 0 1rem rgba(0, 0, 0, 0.25));
  transition: transform 0.3s ease;

  &:hover {
    transform: scale(1.1);
  }
`;

const TextWrapper = styled.div<{ reverse: boolean }>`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  align-items: ${({ reverse }) => (reverse ? 'flex-end' : 'flex-start')};
  color: ${PALETTE.GRAY_500};
`;

const Name = styled.span`
  font-size: 1.5rem;
  font-weight: 700;
`;

const Intro = styled.pre`
  color: inherit;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`;

export const HighLightedText = styled(HighLightedTextComponent)``;

const UrlContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-top: 1.5rem;
`;

const UrlBar = styled.div`
  width: 2px;
  height: 3rem;
  background-color: ${PALETTE.GRAY_500};
`;

const UrlWrapper = styled.div<{ reverse: boolean }>`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  align-items: ${({ reverse }) => (reverse ? 'flex-end' : 'flex-start')};

  ${HighLightedText} {
    margin-right: 0.4rem;
  }

  a {
    color: inherit;

    &:hover {
      text-decoration: underline;
    }
  }
`;

export default { Root, Image, TextWrapper, Name, Intro, UrlContainer, UrlBar, UrlWrapper };
