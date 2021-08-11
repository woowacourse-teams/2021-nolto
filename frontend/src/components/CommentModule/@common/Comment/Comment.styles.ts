import styled, { css, keyframes } from 'styled-components';

import TextButton from 'components/@common/TextButton/TextButton';
import { PALETTE } from 'constants/palette';
import { ButtonStyle } from 'types';
import AutoHeightTextArea from 'components/@common/AutoHeightTextArea/AutoHeightTextArea';

const show = keyframes`
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
`;

const Root = styled.div`
  animation: ${show} 0.5s ease;
  padding-bottom: 0.5rem;
`;

const Author = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 14px;
  font-weight: 600;
  margin: 0.5rem 0;

  & > span {
    color: ${PALETTE.PRIMARY_400};
    font-weight: 400;
  }
`;

const modify = css`
  width: 100%;

  & > span,
  input {
    width: 100%;
  }
`;

const Body = styled.span<{ isModifying: boolean }>`
  display: inline-block;
  padding: 0 1.5rem;
  ${({ isModifying }) => isModifying && modify};
`;

const Content = styled.span<{ isFeedAuthor: boolean }>`
  position: relative;
  display: inline-block;
  padding: 0.5rem 1.5rem;
  box-shadow: 2px 2px 4px 2px rgba(85, 85, 85, 0.1);
  border-radius: 0.75rem;
  min-width: 12rem;
  min-height: 2.5rem;
  line-height: 1.5rem;

  background-color: ${({ isFeedAuthor }) => isFeedAuthor && PALETTE.ORANGE_200};

  & .modified-text {
    color: ${PALETTE.GRAY_500};
    font-size: 12px;
  }
`;

export const ModifyTextArea = styled(AutoHeightTextArea)`
  font-size: 1rem;
  border-bottom: 1px solid ${PALETTE.GRAY_400};
  padding: 0;
  padding-bottom: 2px;
  transition: border-color 0.1s ease;

  &:focus {
    border-color: ${PALETTE.GRAY_500};
  }
`;

const ExhibitText = styled.pre`
  white-space: pre-wrap;
`;

const EditDeleteContainer = styled.div`
  display: flex;
  gap: 4px;
  position: absolute;
  right: 0;
  bottom: -1.7rem;
`;

const Detail = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 0.25rem;
  font-size: 14px;
`;

const ThumbUpWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 2px;
`;

export const CommentTextButton = styled(TextButton.Regular)`
  color: ${PALETTE.BLACK_400};
  padding: 2px;
`;

CommentTextButton.defaultProps = {
  buttonStyle: ButtonStyle.SOLID,
  reverse: true,
};

export default {
  Root,
  Author,
  Content,
  Detail,
  Body,
  ExhibitText,
  EditDeleteContainer,
  ThumbUpWrapper,
};
