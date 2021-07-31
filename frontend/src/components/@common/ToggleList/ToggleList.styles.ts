import styled from 'styled-components';

import StacksMoreSvg from 'assets/stacksMore.svg';

const Root = styled.div<{ width: string; height: string; isToggled: boolean }>`
  display: flex;
  gap: 1rem;
  overflow: hidden;
  transition: all 0.3s ease-in;

  width: ${({ width }) => width && width};
  height: ${({ height, isToggled }) => {
    if (!height) return;

    if (!isToggled) {
      return height;
    }
    return `calc(${height} * 2)`;
  }};
`;

const Content = styled.div<{ isToggled: boolean }>`
  display: flex;
  flex-wrap: wrap;
  gap: 0.25rem;
  overflow-y: ${({ isToggled }) => isToggled && 'scroll'};
  -ms-overflow-style: none;
  scrollbar-width: none;

  &::-webkit-scrollbar {
    display: none;
  }
`;

const ButtonWrapper = styled.div`
  display: inline-block;
  width: fit-content;
  height: fit-content;
  align-items: center;
`;

const StacksMoreIcon = styled(StacksMoreSvg)<{ isToggled: boolean }>`
  transition: all 0.3s ease;

  transform: ${({ isToggled }) => isToggled && 'rotate(180deg)'};
`;

export default { Root, Content, ButtonWrapper, StacksMoreIcon };
