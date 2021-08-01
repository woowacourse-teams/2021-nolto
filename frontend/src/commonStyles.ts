import styled from 'styled-components';

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
