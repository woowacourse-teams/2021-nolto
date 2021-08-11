import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import TextInput from 'components/@common/TextInput/TextInput';
import IconButton from 'components/@common/IconButton/IconButton';

const Author = styled.div`
  font-weight: 600;
`;

export const Form = styled.form`
  display: flex;
  margin: 0.25rem 2rem;
  flex-direction: column;
  gap: 0.5rem;

  & > input {
    width: 100%;
  }
`;

const FormInputWrapper = styled.div`
  display: flex;
  gap: 0.5rem;
  width: 100%;

  & > input {
    width: 100%;
  }
`;

export const CommentFormInput = styled(TextInput)`
  border-color: ${PALETTE.GRAY_300};
  font-size: 1rem;
  padding: 0;
  padding-bottom: 2px;
  transition: border-color 0.1s ease;

  &:focus {
    border-color: ${PALETTE.GRAY_400};
  }
`;

export const SendButton = styled(IconButton)`
  padding: 0.25rem;
`;

export default { Author, FormInputWrapper };
