import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import TextInput from 'components/@common/TextInput/TextInput';
import IconButton from 'components/@common/IconButton/IconButton';

const Author = styled.div`
  font-weight: 600;
`;

export const Form = styled.form`
  display: flex;
  margin: 0.7rem 0 0 2rem;
  flex-direction: column;
  gap: 0.5rem;

  & > input {
    width: 100%;
  }
`;

const FormInputWrapper = styled.div`
  display: flex;
  width: 100%;

  & > input {
    width: 100%;
  }
`;

export const CommentFormInput = styled(TextInput)`
  border-color: ${PALETTE.GRAY_300};
  font-size: 14px;

  &:focus {
    border-color: ${PALETTE.GRAY_300};
  }
`;

export const SendButton = styled(IconButton)`
  padding: 0.25rem;
`;

const Help = styled.div``;

export default { Author, FormInputWrapper, Help };
