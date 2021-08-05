import styled, { css } from 'styled-components';

import TextButton from 'components/@common/TextButton/TextButton';
import TextInput from 'components/@common/TextInput/TextInput';
import { PALETTE } from 'constants/palette';
import { ButtonStyle } from 'types';

const Author = styled.div`
  display: flex;
  align-items: flex-end;
  gap: 0.5rem;
  font-size: 14px;
  font-weight: 600;

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
  padding: 0.7rem 2rem 0;
  ${({ isModifying }) => isModifying && modify};
`;

const Content = styled.span<{ isFeedAuthor: boolean }>`
  display: inline-block;
  padding: 0.7rem 2rem 0;
  box-shadow: 2px 2px 4px 2px rgba(85, 85, 85, 0.1);
  border-radius: 16px;
  min-width: 12rem;
  height: 3rem;

  background-color: ${({ isFeedAuthor }) => isFeedAuthor && PALETTE.ORANGE_200};
`;

export const ModifyTextInput = styled(TextInput)`
  font-size: 1rem;
  border-color: ${PALETTE.GRAY_300};

  &:focus {
    border-color: ${PALETTE.GRAY_300};
  }
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
  gap: 2px;
`;

const ReplyFromWrapper = styled.div`
  padding: 0.7rem 0 0 2rem;
`;

export const CommentTextButton = styled(TextButton.Regular)`
  color: ${PALETTE.BLACK_400};
  padding: 2px;
`;

CommentTextButton.defaultProps = {
  buttonStyle: ButtonStyle.SOLID,
  reverse: true,
};

export default { Author, Content, Detail, Body, ThumbUpWrapper, ReplyFromWrapper };
