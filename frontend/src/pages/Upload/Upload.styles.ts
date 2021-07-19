import styled from 'styled-components';

import TextArea from 'components/@common/TextArea/TextArea';
import TextButton from 'components/@common/TextButton/TextButton';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 30rem;
  margin-right: auto;
  margin-left: auto;
  padding-top: 7.75rem;

  & > form {
    width: 100%;
  }
`;

const TitleWrapper = styled.h2`
  margin-bottom: 3rem;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 5rem;
`;

const VerticalWrapper = styled.div`
  display: flex;
  flex-direction: column;
  border-bottom: 2rem;
  width: 100%;

  & input,
  textarea {
    box-shadow: 2px 2px 2px 0 rgba(253, 161, 133, 0.5);
  }
`;

export const ContentTextArea = styled(TextArea)`
  height: 31.25rem;
`;

const InputsContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: space-between;
`;

const StretchWrapper = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  border-bottom: 2rem;
  width: 100%;

  & > .stretch-label {
    min-width: 7rem;
  }

  & input {
    box-shadow: 2px 2px 2px 0 rgba(253, 161, 133, 0.5);
  }
`;

const levelWrapper = styled(StretchWrapper)`
  margin-bottom: 0;
  width: auto;
`;

export const StyledButton = styled(TextButton.Regular)`
  width: 12rem;
  height: 2.25rem;
  font-size: 1rem;
  border-radius: 7px;
  margin-top: 1rem;
`;

const ButtonsWrapper = styled.div`
  display: flex;
  gap: 1.5rem;
  justify-content: center;
`;

export default {
  Root,
  TitleWrapper,
  VerticalWrapper,
  InputsContainer,
  StretchWrapper,
  levelWrapper,
  ButtonsWrapper,
};
