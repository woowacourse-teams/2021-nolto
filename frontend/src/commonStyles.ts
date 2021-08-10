import styled, { css, keyframes } from 'styled-components';

import { PALETTE } from 'constants/palette';
import { MEDIA_QUERY } from 'constants/mediaQuery';

export interface FlexContainerProps {
  children: React.ReactNode;
  flexDirection?: 'column' | 'row';
  justifyContent?: 'center' | 'start' | 'end' | 'space-between';
  alignItems?: 'center' | 'start' | 'end' | 'space-between';
  gap?: string;
  flexGrow?: string;
  width?: string;
  flexBasis?: string;
  flexShrink?: string;
}

export const Card = styled.div`
  box-shadow: 4px 4px 8px 4px rgba(85, 85, 85, 0.2);
  border-radius: 0.75rem;

  @media ${MEDIA_QUERY.TABLET} {
    box-shadow: 2px 2px 4px 2px rgba(85, 85, 85, 0.2);
  }
`;

export const FlexContainer = styled.div<FlexContainerProps>`
  display: flex;

  flex-direction: ${({ flexDirection }) => flexDirection};
  justify-content: ${({ justifyContent }) => justifyContent};
  align-items: ${({ alignItems }) => alignItems};
  gap: ${({ gap }) => gap};
  flex-grow: ${({ flexGrow }) => flexGrow};
  flex-basis: ${({ flexBasis }) => flexBasis};
  flex-shrink: ${({ flexShrink }) => flexShrink};
  width: ${({ width }) => width};
`;

FlexContainer.defaultProps = {
  flexDirection: 'row',
  justifyContent: 'start',
  alignItems: 'start',
};

const hoverLayerAnimation = keyframes`
  from {
    opacity: 0;
  }

  to {
    opacity : 0.2;
  }
`;

interface hoverLayerProps {
  borderRadius?: string;
}

export const hoverLayer = ({ borderRadius }: hoverLayerProps) => css`
  position: relative;

  &:hover::after {
    content: '';
    display: block;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: ${PALETTE.BLACK_400};
    border-radius: ${borderRadius};
    opacity: 0.2;
    animation: ${hoverLayerAnimation} 0.2s ease;
  }
`;

export const hoverUnderline = css`
  position: relative;

  &::after {
    content: '';
    position: absolute;
    width: 100%;
    transform: scaleX(0);
    height: 2px;
    bottom: 0;
    left: 0;
    background-color: ${PALETTE.WHITE_400};
    transform-origin: bottom center;
    transition: transform 0.1s ease-out;
  }

  &:hover::after {
    transform: scaleX(1);
  }
`;
