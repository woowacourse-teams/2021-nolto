import styled, { css, keyframes } from 'styled-components';

import { PALETTE } from 'constants/palette';
import { MEDIA_QUERY } from 'constants/mediaQuery';

export interface FlexContainerProps {
  children: React.ReactNode;
  flexDirection?: 'column' | 'row';
  justifyContent?: 'center' | 'flex-start' | 'flex-end' | 'space-between';
  alignItems?: 'center' | 'flex-start' | 'flex-end' | 'space-between';
  gap?: string;
  flexGrow?: string;
  width?: string;
  flexBasis?: string;
  flexShrink?: string;
}

export const defaultShadow = css`
  box-shadow: 2px 2px 4px 2px rgba(0, 0, 0, 0.1);
`;

export const Card = styled.div`
  ${defaultShadow};
  border-radius: 0.75rem;
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
  justifyContent: 'flex-start',
  alignItems: 'flex-start',
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
  overflow: hidden;

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

export const hoverZoomImg = css`
  & img {
    transition: all 0.2s ease;
  }

  &:hover img {
    transform: scale(1.05);
  }
`;

export const DefaultPageRoot = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;

  @media ${MEDIA_QUERY.MOBILE} {
    padding-top: 0;
  }
`;

export const Divider = styled.hr`
  width: 100%;
  height: 1px;
  background-color: ${PALETTE.BLACK_200};
  border: none;
`;

export const visuallyHidden = css`
  width: 1px;
  height: 1px;
  clip: rect(0 0 0 0);
  overflow: hidden;
  position: absolute;
`;
