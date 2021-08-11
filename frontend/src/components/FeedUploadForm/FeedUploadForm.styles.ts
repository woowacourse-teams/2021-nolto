import styled, { keyframes } from 'styled-components';

import TextArea from 'components/@common/TextArea/TextArea';
import TextButton from 'components/@common/TextButton/TextButton';
import Tooltip from 'components/@common/Tooltip/Tooltip';
import ToyboxIcon from 'assets/toybox.svg';

export const Form = styled.form`
  margin-bottom: 5rem;
`;

const FormContainer = styled.div`
  padding: 4rem 3rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  box-shadow: 4px 4px 8px 4px rgba(85, 85, 85, 0.2);
  border-radius: 1.5rem;
  margin-bottom: 2.5rem;
`;

const VerticalWrapper = styled.div`
  position: relative;
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

const LevelWrapper = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  min-width: 7rem;
`;

const SOSLabel = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-left: auto;
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

const QuestionMark = styled.span``;

const show = keyframes`
  from {
    opacity: 0;
  }
  
  to {
    opacity: 1;
  }
`;

export const LevelTooltip = styled(Tooltip)<{ visible: boolean }>`
  display: ${({ visible }) => !visible && 'none'};
  position: absolute;
  top: 10%;
  left: 70%;

  animation: ${show} 0.2s ease;
`;

export const SOSTooltip = styled(Tooltip)<{ visible: boolean }>`
  display: ${({ visible }) => !visible && 'none'};
  position: absolute;
  top: 15%;
  right: 105%;

  animation: ${show} 0.2s ease;
`;

const InputCaption = styled.span`
  font-size: 0.75rem;
`;

export const Toybox = styled(ToyboxIcon)`
  position: absolute;
  height: 2rem;
  right: 0;
`;

export default {
  FormContainer,
  VerticalWrapper,
  InputsContainer,
  StretchWrapper,
  LevelWrapper,
  SOSLabel,
  QuestionMark,
  ButtonsWrapper,
  InputCaption,
};
