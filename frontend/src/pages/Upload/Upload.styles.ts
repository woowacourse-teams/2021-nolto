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

const VerticalWrapper = styled.div`
  display: flex;
  flex-direction: column;
  border-bottom: 2rem;
  width: 100%;
  margin-bottom: 0.75rem;
`;

export const ContentTextArea = styled(TextArea)`
  height: 31.25rem;
`;

const InputsContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  margin-bottom: 1rem;
`;

const StretchWrapper = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 1.5rem;
  border-bottom: 2rem;
  width: 100%;
  margin-bottom: 1rem;

  & > .stretch-label {
    width: 10rem;
  }
`;

export const StyledButton = styled(TextButton.Regular)`
  width: 5rem;
  height: 2rem;
  font-size: 1rem;
  border-radius: 7px;
`;

const ButtonsWrapper = styled.div`
  display: flex;
  gap: 1.5rem;
  margin: 2rem 0 5.5rem;
  justify-content: flex-end;
`;

export default {
  Root,
  TitleWrapper,
  VerticalWrapper,
  InputsContainer,
  StretchWrapper,
  ButtonsWrapper,
};
