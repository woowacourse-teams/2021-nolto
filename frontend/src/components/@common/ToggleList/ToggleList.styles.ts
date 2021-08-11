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
    return `calc(${height} * 2.5)`;
  }};
`;

const ContentWrapper = styled.div<{ isToggled: boolean }>`
  overflow-y: ${({ isToggled }) => isToggled && 'scroll'};

  -ms-overflow-style: none;
  scrollbar-width: none;
  &::-webkit-scrollbar {
    display: none;
  }
`;

const Content = styled.div`
  display: flex;
  flex-wrap: wrap;
  height: fit-content;
  gap: 0.25rem;
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

export default { Root, ContentWrapper, Content, ButtonWrapper, StacksMoreIcon };
